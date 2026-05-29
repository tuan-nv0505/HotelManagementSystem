from openai import AsyncOpenAI, OpenAI
from app.core.config import setting

async_client = AsyncOpenAI(api_key=setting.OPENAI_API_KEY)
client = OpenAI(api_key=setting.OPENAI_API_KEY)