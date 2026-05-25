import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const Footer = () => {
    return (
        <footer className="bg-light pt-5 pb-4 mt-5 border-top">
            <Container>
                <Row>
                    <Col md={4} className="mb-4">
                        <h5 className="fw-bold mb-3" style={{ color: '#0194f3' }}>Hotel Booking System</h5>
                        <p className="text-muted small">Uy tín - Chất lượng - Tận tâm. Chúng tôi mang đến cho bạn không gian nghỉ dưỡng hoàn hảo nhất.</p>
                    </Col>
                    
                    <Col md={2} className="mb-4">
                        <h6 className="fw-bold mb-3">Đặt phòng</h6>
                        <ul className="list-unstyled text-muted small">
                            <li className="mb-2"><Link to="/booking" className="text-decoration-none text-muted">Đặt phòng mới</Link></li>
                            <li className="mb-2"><Link to="/my-booking" className="text-decoration-none text-muted">Đơn đặt của tôi</Link></li>
                        </ul>
                    </Col>
                    
                    <Col md={3} className="mb-4">
                        <h6 className="fw-bold mb-3">Hỗ trợ khách hàng</h6>
                        <ul className="list-unstyled text-muted small">
                            <li className="mb-2"><Link to="/faq" className="text-decoration-none text-muted">Câu hỏi thường gặp</Link></li>
                            <li className="mb-2"><Link to="/payment-guide" className="text-decoration-none text-muted">Hướng dẫn thanh toán</Link></li>
                            <li className="mb-2"><Link to="/cancellation" className="text-decoration-none text-muted">Chính sách hủy phòng</Link></li>
                        </ul>
                    </Col>
                    
                    <Col md={3} className="mb-4">
                        <h6 className="fw-bold mb-3">Kết nối với chúng tôi</h6>
                        <div className="d-flex gap-3">
                            <i className="bi bi-facebook fs-4 text-primary"></i>
                            <i className="bi bi-instagram fs-4 text-danger"></i>
                            <i className="bi bi-envelope-fill fs-4 text-muted"></i>
                        </div>
                    </Col>
                </Row>
                
                <hr className="mb-4" />
                <div className="text-center text-muted small">
                    © 2026 Hotel Booking System. Đã đăng ký bản quyền.
                </div>
            </Container>
        </footer>
    );
}

export default Footer;