from fastapi import APIRouter, Depends
from fastapi.responses import StreamingResponse
from sqlalchemy.orm import Session
from starlette.middleware.cors import CORSMiddleware
import logging

from app.rag.rag import ask_llm
from app.schemas.chat import ChatRequest
from app.apis.deps import get_db

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)

router = APIRouter()

@router.post('/chat')
async def chat(request: ChatRequest, session: Session = Depends(get_db)):
    logging.info(f'Question: {request.question}')
    return StreamingResponse(
        ask_llm(
            question=request.question,
            session=session,
            faiss_index_path='/Users/tuan-nv0505/Projects/School/Hotel-Management-System/chatbot/data/vector_databases/faiss.index'
        ),
        media_type='text/plain'
    )