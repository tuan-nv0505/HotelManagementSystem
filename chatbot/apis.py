from fastapi import FastAPI

app = FastAPI()

@app.get("/chat")
def chat():
    pass