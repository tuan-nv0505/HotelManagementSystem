import React, { useState, useRef, useEffect } from 'react';
import { Card, Button, Form, InputGroup } from 'react-bootstrap';
import 'bootstrap-icons/font/bootstrap-icons.css';

const Chatbot = () => {
    const [isOpen, setIsOpen] = useState(false);
    const [question, setQuestion] = useState('');
    const [messages, setMessages] = useState([
        { sender: 'ai', text: 'Xin chào! Tôi là Chatbot hỗ trợ tư vấn, tôi có thể giúp gì cho bạn hôm nay?' }
    ]);
    const [isTyping, setIsTyping] = useState(false);
    
    const messagesEndRef = useRef(null);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!question.trim()) 
            return;

        const userMessage = question;
        setQuestion('');
        
        setMessages(prev => [...prev, { sender: 'user', text: userMessage }]);
        
        setMessages(prev => [...prev, { sender: 'ai', text: '' }]);
        setIsTyping(true);

        try {
            const response = await fetch('http://127.0.0.1:8000/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ question: userMessage }),
            });

            if (!response.ok) 
                throw new Error('Lỗi kết nối API');

            const reader = response.body.getReader();
            const decoder = new TextDecoder('utf-8');
            let done = false;

            while (done === false) {
                const { value, done: readerDone } = await reader.read();
                done = readerDone;

                if (value) {
                    const getToken = (oldMessages) => {
                        const token = decoder.decode(value, { stream: true });
                        const newMessages = [...oldMessages];
                        const lastIndex = newMessages.length - 1;
                        newMessages[lastIndex] = { ...newMessages[lastIndex], text: newMessages[lastIndex].text + token };
                        return newMessages
                    }
                    setMessages(prev => getToken(prev));
                }
            }
        } catch (error) {
            console.error('Lỗi:', error);
            setMessages(prev => {
                const newMessages = [...prev];
                newMessages[newMessages.length - 1].text = 'Xin lỗi, tôi đang gặp sự cố kết nối tới máy chủ.';
                return newMessages;
            });
        } finally {
            setIsTyping(false);
        }
    };

    return (
        <div style={{ position: 'fixed', bottom: '20px', right: '20px', zIndex: 1000 }}>
            {!isOpen && (
                <Button 
                    onClick={() => setIsOpen(true)}
                    style={{
                        height: '60px', 
                        padding: '0 25px',
                        borderRadius: '30px',
                        backgroundColor: '#ff5e1f', 
                        border: 'none',
                        boxShadow: '0 4px 8px rgba(0,0,0,0.2)',
                        display: 'flex', 
                        alignItems: 'center', 
                        justifyContent: 'center',
                        gap: '10px'
                    }}
                >
                    <i className="bi bi-chat-dots-fill text-white fs-4"></i>
                </Button>
            )}

            {isOpen && (
                <Card className="shadow-lg border-0" style={{ width: '350px', height: '500px', display: 'flex', flexDirection: 'column' }}>
                    <Card.Header 
                        className="text-white d-flex justify-content-between align-items-center py-3"
                        style={{ backgroundColor: '#ff5e1f', borderTopLeftRadius: 'calc(0.375rem - 1px)', borderTopRightRadius: 'calc(0.375rem - 1px)' }}
                    >
                        <div className="fw-bold"><i className="bi bi-robot me-2"></i>Hỗ trợ trực tuyến</div>
                        <i 
                            className="bi bi-x-lg" 
                            style={{ cursor: 'pointer' }} 
                            onClick={() => setIsOpen(false)}
                        ></i>
                    </Card.Header>

                    <Card.Body style={{ overflowY: 'auto', backgroundColor: '#f8f9fa', flex: 1 }}>
                        {messages.map((msg, index) => (
                            <div key={index} className={`d-flex mb-3 ${msg.sender === 'user' ? 'justify-content-end' : 'justify-content-start'}`}>
                                <div 
                                    style={{
                                        maxWidth: '80%', padding: '10px 15px', borderRadius: '15px',
                                        backgroundColor: msg.sender === 'user' ? '#0d6efd' : '#e9ecef',
                                        color: msg.sender === 'user' ? 'white' : 'black',
                                        whiteSpace: 'pre-wrap'
                                    }}
                                >
                                    {msg.text || (msg.sender === 'ai' && isTyping && "...")}
                                </div>
                            </div>
                        ))}
                        <div ref={messagesEndRef} />
                    </Card.Body>

                    <Card.Footer className="bg-white border-0 p-3">
                        <Form onSubmit={handleSubmit}>
                            <InputGroup>
                                <Form.Control
                                    type="text"
                                    placeholder="Nhập câu hỏi..."
                                    value={question}
                                    onChange={(e) => setQuestion(e.target.value)}
                                    disabled={isTyping}
                                    style={{ borderRadius: '20px 0 0 20px' }}
                                />
                                <Button 
                                    type="submit" 
                                    disabled={isTyping || !question}
                                    style={{ backgroundColor: '#ff5e1f', border: 'none', borderRadius: '0 20px 20px 0' }}
                                >
                                    <i className="bi bi-send-fill"></i>
                                </Button>
                            </InputGroup>
                        </Form>
                    </Card.Footer>
                </Card>
            )}
        </div>
    );
};

export default Chatbot;