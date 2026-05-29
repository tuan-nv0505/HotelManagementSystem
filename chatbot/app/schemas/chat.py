from pydantic import BaseModel, Field


class ChatRequest(BaseModel):
    question: str = Field(max_length=100)
