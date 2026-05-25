from pathlib import Path
from langchain_community.document_loaders import (
    DirectoryLoader,
    TextLoader
)
from langchain_community.vectorstores import FAISS
from langchain_text_splitters import (
    RecursiveCharacterTextSplitter,
)
from langchain_ollama import (
    OllamaEmbeddings,
    ChatOllama,
)
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.runnables import RunnablePassthrough
from langchain_core.output_parsers import StrOutputParser


loader = DirectoryLoader(
    path="knowledge_base",
    glob="**/*.md",
    loader_cls=TextLoader,
    show_progress=True,
    use_multithreading=True,
)
docs = loader.load()

text_splitter = RecursiveCharacterTextSplitter(
    chunk_size=1200,
    chunk_overlap=200,
    separators=["\n\n", "\n", ".", " ", ""],
)
splits = text_splitter.split_documents(docs)

embeddings = OllamaEmbeddings(
    model="nomic-embed-text"
)


VECTORSTORE_PATH = "faiss_index"
if Path(VECTORSTORE_PATH).exists():
    vectorstore = FAISS.load_local(
        VECTORSTORE_PATH,
        embeddings,
        allow_dangerous_deserialization=True,
    )
else:
    vectorstore = FAISS.from_documents(
        documents=splits,
        embedding=embeddings,
    )
    vectorstore.save_local(VECTORSTORE_PATH)

retriever = vectorstore.as_retriever(
    search_type="similarity_score_threshold",
    search_kwargs={
        "k": 3,
        "score_threshold": 0.5,
    },
)

def format_docs(docs):
    if not docs:
        return "Không có thông tin phù hợp."

    return "\n\n".join(
        doc.page_content
        for doc in docs
    )


template = (
    "Bạn là AI Assistant chuyên hỗ trợ hệ thống quản lý khách sạn.\n"
    "Bạn hoạt động dựa trên cơ chế RAG (Retrieval-Augmented Generation) "
    "và chỉ được phép sử dụng dữ liệu trong context được cung cấp.\n\n"

    "====================\n"
    "VAI TRÒ\n"
    "====================\n"
    "- Hỗ trợ khách hàng khách sạn.\n\n"

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
prompt = ChatPromptTemplate.from_template(template)

llm = ChatOllama(
    model="qwen2.5:0.5b",
    temperature=0,
    num_predict=256,
)


rag_chain = (
    {
        "context": retriever | format_docs,
        "question": RunnablePassthrough(),
    }
    | prompt
    | llm
    | StrOutputParser()
)

while True:
    user_input = input("Question: ")
    if user_input.lower() == "exit":
        break
    answer = rag_chain.invoke(user_input)
    print("\nAnswer:")
    print(answer)
    print()