import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';

const Footer = () => {
    return (
        <footer className="bg-light pt-5 pb-4 mt-5 border-top">
            <Container>
                <Row>
                    <Col md={4} className="mb-4">
                        <h5 className="fw-bold mb-3" style={{ color: '#0194f3' }}>Hotel Booking System</h5>
                        <p className="text-muted small">Uy tín - Chất lượng - Tận tâm. Chúng tôi mang đến cho bạn không gian nghỉ dưỡng hoàn hảo nhất.</p>
                    </Col>

                    <Col md={3} className="mb-4">
                        <h6 className="fw-bold mb-3">Kết nối với chúng tôi</h6>
                        <div className="d-flex gap-3">
                            <a href="https://www.facebook.com/nguyen.tuan.777041" target="_blank" rel="noopener noreferrer">
                                <i className="bi bi-facebook fs-4 text-primary"></i>
                            </a>

                            <a href="https://www.instagram.com/hw4h4p.yeong/" target="_blank" rel="noopener noreferrer">
                                <i className="bi bi-instagram fs-4 text-danger"></i>
                            </a>

                            <a href="https://mail.google.com/mail/u/1/#inbox?compose=CllgCJfqcQndMmRXTmKBPQhDtSFHrDfzkzvQKbzplpJMmcsdXKnQlnLhzFBkwsjBwRRRfkMQmwg" className="text-decoration-none text-muted"  target="_blank" rel="noopener noreferrer">
                                <i className="bi bi-envelope-fill fs-4"></i>
                            </a>
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