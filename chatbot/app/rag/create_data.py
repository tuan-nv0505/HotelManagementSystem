import os

from sqlalchemy.orm import Session

from app.rag.embedding import embed_batch
from app.db.models.chunk import Chunk
from app.db.models.document import Document

import faiss
import numpy as np
from pathlib import Path
from datetime import datetime
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_community.document_loaders import DirectoryLoader, TextLoader, PyMuPDFLoader, Docx2txtLoader

import logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)

def create_chunk(directory: str, session: Session):
    docs = []
    docs += DirectoryLoader(directory, "**/*.md", loader_cls=TextLoader).load()
    docs += DirectoryLoader(directory, "**/*.txt", loader_cls=TextLoader).load()
    docs += DirectoryLoader(directory, "**/*.pdf", loader_cls=PyMuPDFLoader).load()
    docs += DirectoryLoader(directory, "**/*.docx", loader_cls=Docx2txtLoader).load()

    if not docs:
        logging.info("Not found any documents")
        chunks = session.query(Chunk.content, Chunk.id).all()
        return zip(*chunks)

    res = session.query(Document.file_path).all()
    db_document_paths = {r[0] for r in res}

    valid_docs = []
    for doc in docs:
        file_path = str(os.path.abspath(doc.metadata["source"]))
        if file_path in db_document_paths:
            continue
        valid_docs.append(doc)

    if not valid_docs:
        logging.info("No new documents to update chunk")
        chunks = session.query(Chunk.content, Chunk.id).all()
        return zip(*chunks)

    logging.info(f'Start updating chunks from {directory}')
    for doc in valid_docs:
        file = Path(doc.metadata["source"])

        document = Document(
            file_path=os.path.abspath(str(file)),
            file_name=file.stem,
            file_type=file.suffix,
            created_at=datetime.now()
        )
        session.add(document)
        session.flush()

        doc.metadata["document_id"] = document.id

    splitter = RecursiveCharacterTextSplitter(
        chunk_size=1200,
        chunk_overlap=200
    )
    chunks = splitter.split_documents(valid_docs)

    chunk_ids = []
    chunk_contents = []
    for chunk in chunks:
        document_id = chunk.metadata["document_id"]

        db_chunk = Chunk(
            document_id=document_id,
            content=chunk.page_content,
            token_count=len(chunk.page_content.split()),
            created_at=datetime.now()
        )

        session.add(db_chunk)
        session.flush()
        chunk_ids.append(db_chunk.id)
        chunk_contents.append(chunk.page_content)

    session.commit()
    logging.info(f'Update chunks success')
    return chunk_contents, chunk_ids

def create_vector_database(chunk_contents, chunk_ids, index_path: str, dimension: int = 1536, embedder=embed_batch):
    os.makedirs('./app/data/vector_databases', exist_ok=True)

    if os.path.exists(index_path):
        faiss_index = faiss.read_index(index_path)
    else:
        base_index = faiss.IndexFlatIP(dimension)
        faiss_index = faiss.IndexIDMap(base_index)

    chunk_ids = np.array(chunk_ids)
    existing_ids = faiss.vector_to_array(faiss_index.id_map)
    chunk_ids = chunk_ids[~np.isin(chunk_ids, existing_ids)]
    if chunk_ids.shape[0] < 1:
        logging.info("No new chunks to update FAISS index")
        return

    logging.info(f'Start updating vector database')
    vectors = embedder(chunk_contents)
    faiss.normalize_L2(vectors)
    faiss_index.add_with_ids(vectors, chunk_ids)

    os.makedirs(os.path.dirname(index_path), exist_ok=True)
    faiss.write_index(faiss_index, index_path)
    logging.info(f'Update vector database success')

