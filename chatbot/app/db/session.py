from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from app.core.config import setting

engine = create_engine(setting.get_database_url(), pool_pre_ping=True, echo=False)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)