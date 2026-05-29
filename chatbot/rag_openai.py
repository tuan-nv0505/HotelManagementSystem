from dotenv import load_dotenv
load_dotenv()
from langchain_community.document_loaders import DirectoryLoader, TextLoader
from langchain_community.vectorstores import FAISS
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_openai import OpenAIEmbeddings, ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.runnables import RunnablePassthrough
from langchain_core.output_parsers import StrOutputParser

loader = DirectoryLoader(
    path ="/Users/tuan-nv0505/Projects/School/Hotel-Management-System/chatbot/data/documents",
    glob="**/*.md",
    loader_cls=TextLoader,
    show_progress=True,
    use_multithreading=True
)

docs = loader.load()



text_splitter = RecursiveCharacterTextSplitter(
    chunk_size=1200,
    chunk_overlap=200,
    separators=["\\n\\n", "\\n", ".", " ", ""]
)

splits = text_splitter.split_documents(docs)

for split in splits:
    print(split.metadata)

embeddings = OpenAIEmbeddings(model="text-embedding-3-small")

vectorstore = FAISS.from_documents(
    documents=splits,
    embedding=embeddings,
)

retriever = vectorstore.as_retriever(
    search_type="similarity_score_threshold",
    search_kwargs={"k": 5, "score_threshold": 0.3}
)

template = (
    "Bạn là AI Assistant chuyên hỗ trợ hệ thống quản lý khách sạn.\n"
    "Bạn hoạt động dựa trên cơ chế RAG (Retrieval-Augmented Generation) "
    "và chỉ được phép sử dụng dữ liệu trong context được cung cấp.\n\n"

    "====================\n"
    "VAI TRÒ\n"
    "====================\n"
    "- Hỗ trợ khách hàng khách sạn.\n\n"
    # "- Hỗ trợ lễ tân và nhân viên.\n"
    # "- Trả lời thông tin booking, phòng, dịch vụ, thanh toán.\n"

    "====================\n"
    "QUY TẮC BẮT BUỘC\n"
    "====================\n"
    "1) CHỈ sử dụng thông tin trong context để trả lời.\n"
    "2) KHÔNG sử dụng kiến thức bên ngoài.\n"
    "3) KHÔNG suy đoán.\n"
    "4) KHÔNG tự tạo dữ liệu.\n"
    "5) Nếu context không chứa câu trả lời rõ ràng, hãy trả lời chính xác:\n"
    "\"Tôi không tìm thấy thông tin phù hợp trong dữ liệu được cung cấp.\"\n"
    "6) Nếu câu hỏi không liên quan đến khách sạn, hãy trả lời:\n"
    "\"Tôi chỉ hỗ trợ các vấn đề liên quan đến hệ thống khách sạn.\"\n"
    "7) Không tiết lộ thông tin nhạy cảm:\n"
    "- Mật khẩu\n"
    "- Thông tin thanh toán đầy đủ\n"
    "- Dữ liệu cá nhân của khách hàng khác\n"
    "- Thông tin nội bộ không được phép công khai\n\n"

    "====================\n"
    "HƯỚNG DẪN TRẢ LỜI\n"
    "====================\n"
    "- Trả lời bằng tiếng Việt.\n"
    "- Ngắn gọn nhưng đầy đủ.\n"
    "- Chuyên nghiệp và lịch sự.\n"
    "- Ưu tiên thông tin quan trọng.\n"
    "- Nếu có nhiều kết quả, hãy trình bày bằng bullet points.\n"

    "====================\n"
    "CONTEXT\n"
    "====================\n"
    "{context}\n\n"

    "====================\n"
    "CÂU HỎI\n"
    "====================\n"
    "{question}\n\n"

    "====================\n"
    "TRẢ LỜI\n"
    "====================\n"
)

prompt = ChatPromptTemplate.from_template(template=template)

llm = ChatOpenAI(
    model="gpt-5-nano",
    temperature=0
)

rag_chain = (
    {"context": retriever, "question": RunnablePassthrough()}
    | prompt
    | llm
    | StrOutputParser()
)

while True:
    answer = rag_chain.invoke(input("Question: "))
    print(answer)

import faiss
import numpy as np

# giả sử vector 3 chiều
d = 3
index = faiss.IndexFlatL2(d)

# data vector
vectors = np.array([
    [1.0, 0.1, 0.3],
    [0.9, 0.2, 0.25],
    [10.0, 10.0, 10.0]
]).astype('float32')

index.add(vectors)

# query vector
query = np.array([[1.0, 0.1, 0.3]]).astype('float32')

k = 2
distances, indices = index.search(query, k)

print(indices)
print(distances)

# import asyncio
#
# class FakeOpenAIStream:
#     def __init__(self, text):
#         self.tokens = text.split()
#         self.index = 0
#
#     def __aiter__(self):
#         return self
#
#     async def __anext__(self):
#         if self.index >= len(self.tokens):
#             raise StopAsyncIteration
#
#         await asyncio.sleep(1)
#
#         token = self.tokens[self.index]
#         self.index += 1
#         return token
#
# async def generate_token(stream):
#     async for token in stream:
#         yield token
#
# async def client():
#     stream = FakeOpenAIStream("Hello I am AI chatbot")
#     async for token in generate_token(stream):
#         print(token)
#
# async def client2():
#     print("xin chao")
#     await asyncio.sleep(1)
#     print('toi la AI')
#
# async def main():
#     await asyncio.gather(client(), client2())
#
# asyncio.run(main())

