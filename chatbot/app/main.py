from fastapi import FastAPI
from starlette.middleware.cors import CORSMiddleware
import os

from app.apis.apis import router
from app.db.session import SessionLocal
from app.rag.create_data import load_document

session = SessionLocal()
load_document('data/documents', session, 'data/vector_databases/faiss.index')

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=['http://localhost:3000', 'https://hotel-system-pi-brown.vercel.app/'],
    allow_credentials=True,
    allow_methods=['*'],
    allow_headers=['*'],
)

app.include_router(router)