import faiss
from sqlalchemy.orm import Session

from app.db.models.chunk import Chunk
from app.rag import async_client
from app.rag.embedding import async_embed_text


def create_context(chunks):
    return f"\n\n".join(f"[Chunk {c.id}]\n{c.content}" for c in chunks)


async def similarity_search(query: str, session: Session, faiss_index_path: str):
    faiss_index = faiss.read_index(faiss_index_path)
    query_vector = await async_embed_text(query)

    faiss.normalize_L2(query_vector)
    cosine_similarity, indices = faiss_index.search(query_vector, 5)
    chunk_ids = [int(idx) for idx in list(indices[0][indices[0] >= 0.5])]

    chunks = session.query(Chunk).filter(Chunk.id.in_(chunk_ids)).all()
    chunk_map = {c.id: c for c in chunks}
    return [chunk_map[id] for id in chunk_ids]


def create_prompt(question, context):
    return f"""
            Bạn là AI Assistant chuyên hỗ trợ hệ thống quản lý khách sạn.
            Bạn hoạt động dựa trên cơ chế RAG (Retrieval-Augmented Generation)
            và chỉ được phép sử dụng dữ liệu trong context được cung cấp.
    
            ====================
            VAI TRÒ
            ====================
            - Hỗ trợ khách hàng khách sạn.
    
            ====================
            QUY TẮC BẮT BUỘC
            ====================
            1) CHỈ sử dụng thông tin trong context để trả lời.
            2) KHÔNG sử dụng kiến thức bên ngoài.
            3) KHÔNG suy đoán.
            4) KHÔNG tự tạo dữ liệu.
            5) Nếu context không chứa câu trả lời rõ ràng, hãy trả lời chính xác:
            "Tôi không tìm thấy thông tin phù hợp trong dữ liệu được cung cấp."
            6) Nếu câu hỏi không liên quan đến khách sạn, hãy trả lời:
            "Tôi chỉ hỗ trợ các vấn đề liên quan đến hệ thống khách sạn."
            7) Không tiết lộ thông tin nhạy cảm:
            - Mật khẩu
            - Thông tin thanh toán đầy đủ
            - Dữ liệu cá nhân của khách hàng khác
            - Thông tin nội bộ không được phép công khai
            8) Câu hỏi ngôn ngữ gì thì câu trả lời phải là ngôn ngữ đó.
    
            ====================
            HƯỚNG DẪN TRẢ LỜI
            ====================
            - Trả lời bằng tiếng Việt.
            - Ngắn gọn nhưng đầy đủ.
            - Chuyên nghiệp và lịch sự.
            - Ưu tiên thông tin quan trọng.
            - Nếu có nhiều kết quả, hãy trình bày bằng bullet points.
    
            ====================
            CONTEXT
            ====================
            {context}
    
            ====================
            CÂU HỎI
            ====================
            {question}
    
            ====================
            TRẢ LỜI
            ====================
            """


async def ask_llm(question: str, session: Session, faiss_index_path):
    chunks = await similarity_search(query=question, session=session, faiss_index_path=faiss_index_path)
    context = create_context(chunks)
    prompt = create_prompt(question=question, context=context)

    stream = await async_client.responses.create(
        model='gpt-5-nano',
        input=prompt,
        stream=True
    )

    async for event in stream:
        if hasattr(event, 'type') and event.type == 'response.output_text.delta':
            text_chunk = event.delta
            if text_chunk:
                yield text_chunk
