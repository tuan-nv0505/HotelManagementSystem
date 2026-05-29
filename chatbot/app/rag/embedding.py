import numpy as np

from app.rag import async_client, client


async def async_embed_text(text: str):
    res = await async_client.embeddings.create(
        model="text-embedding-3-small",
        input=text
    )
    return np.array([res.data[0].embedding]).astype("float32")


async def async_embed_batch(texts: list[str]):
    res = await async_client.embeddings.create(
        model="text-embedding-3-small",
        input=texts
    )
    return np.array([item.embedding for item in res.data]).astype("float32")

def embed_text(text: str):
    res = client.embeddings.create(
        model="text-embedding-3-small",
        input=text
    )
    return np.array([res.data[0].embedding]).astype("float32")


def embed_batch(texts: list[str]):
    res = client.embeddings.create(
        model="text-embedding-3-small",
        input=texts
    )
    return np.array([item.embedding for item in res.data]).astype("float32")
