from sqlalchemy import Column, Integer, String, Text, DateTime
from app.db.base_class import Base

class Document(Base):
    __tablename__ = 'document'

    id = Column(Integer, primary_key=True)
    file_name = Column(String(255))
    file_path = Column(Text)
    file_type = Column(String(50))
    created_at = Column(DateTime)
