from fastapi import FastAPI
from starlette.middleware.cors import CORSMiddleware

from app.apis.apis import router
from app.db.session import SessionLocal
from app.rag.create_data import create_chunk, create_vector_database

session = SessionLocal()
chunk_contents, chunk_ids = create_chunk('./data/documents', session)
create_vector_database(chunk_contents, chunk_ids, './data/vector_databases/faiss.index')

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=['http://localhost:3000', 'https://hotel-system-pi-brown.vercel.app/'],
    allow_credentials=True,
    allow_methods=['*'],
    allow_headers=['*'],
)

app.include_router(router)