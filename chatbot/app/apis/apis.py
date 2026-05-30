from fastapi import APIRouter, Depends
from fastapi.responses import StreamingResponse
from sqlalchemy.orm import Session
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
            faiss_index_path='./data/vector_databases/faiss.index'
        ),
        media_type='text/plain',
        headers={
            "Cache-Control": "no-cache",
            "Connection": "keep-alive",
            "X-Accel-Buffering": "no"
        }
    )

@router.get('/chat')
def hello():
    return {
        'message': 'Hello World!'
    }