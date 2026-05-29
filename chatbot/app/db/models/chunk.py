from sqlalchemy import Column, Integer, ForeignKey, Text, DateTime
from sqlalchemy.orm import relationship
from app.db.base_class import Base


class Chunk(Base):
    __tablename__ = 'chunk'

    id = Column(Integer, primary_key=True)
    document_id = Column(Integer, ForeignKey("document.id"))
    content = Column(Text)
    chunk_index = Column(Integer)
    token_count = Column(Integer)
    created_at = Column(DateTime)
