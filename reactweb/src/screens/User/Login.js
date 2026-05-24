import React, { useState } from 'react';
import { Container, Row, Col, Form, Button } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';

const Login = () => {
    const navigate = useNavigate();

    const [credentials, setCredentials] = useState({ email: '', password: '' });

    const handleLogin = (e) => {
        e.preventDefault();
        console.log("Đăng nhập với:", credentials);
        navigate('/');
    };

    return (
        <Container fluid className="min-vh-100 p-0">
            <Row className="g-0 min-vh-100">

                <Col md={6} className="d-none d-md-flex align-items-center justify-content-center position-relative"
                    style={{
                        backgroundImage: 'url("https://images.unsplash.com/photo-1571896349842-33c89424de2d?q=80&w=2000&auto=format&fit=crop")',
                        backgroundSize: 'cover',
                        backgroundPosition: 'center'
                    }}
                >
                    <div className="position-absolute top-0 start-0 w-100 h-100" style={{ backgroundColor: 'rgba(0, 0, 0, 0.3)' }}></div>
                    <div className="position-relative text-white text-center p-5">
                        <h2 className="fw-bold" style={{ textShadow: '2px 2px 4px rgba(0,0,0,0.6)' }}>Chào mừng trở lại!</h2>
                        <p className="fs-6" style={{ textShadow: '1px 1px 2px rgba(0,0,0,0.6)' }}>Đăng nhập để quản lý đơn đặt phòng của bạn và nhận những ưu đãi độc quyền.</p>
                    </div>
                </Col>


                <Col md={6} className="d-flex align-items-center justify-content-center p-4">
                    <div style={{ width: '100%', maxWidth: '400px' }}>
                        <div className="text-center mb-4">
                            <h2 className="fw-bold text-primary">Hotel Booking</h2>
                            <p className="text-muted">Đăng nhập vào tài khoản của bạn</p>
                        </div>

                        <Form onSubmit={handleLogin}>
                            <Form.Group className="mb-3">
                                <Form.Label className="fw-semibold">Email đăng nhập</Form.Label>
                                <Form.Control
                                    size="lg"
                                    type="email"
                                    placeholder="name@example.com"
                                    className="rounded-3"
                                    required
                                    onChange={(e) => setCredentials({ ...credentials, email: e.target.value })}
                                />
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <div className="d-flex justify-content-between">
                                    <Form.Label className="fw-semibold">Mật khẩu</Form.Label>
                                    <Link to="/forgot-password" className="text-decoration-none" style={{ fontSize: '0.85rem' }}>Quên mật khẩu?</Link>
                                </div>
                                <Form.Control
                                    size="lg"
                                    type="password"
                                    placeholder="••••••••"
                                    className="rounded-3"
                                    required
                                    onChange={(e) => setCredentials({ ...credentials, password: e.target.value })}
                                />
                            </Form.Group>

                            <Button type="submit" size="lg" className="w-100 rounded-3 mb-3 border-0" style={{ backgroundColor: '#0194f3' }}>
                                Đăng nhập
                            </Button>
                        </Form>

                        <div className="d-flex align-items-center my-4">
                            <hr className="flex-grow-1" />
                            <span className="px-3 text-muted small">Hoặc tiếp tục với</span>
                            <hr className="flex-grow-1" />
                        </div>

                        <div className="d-flex gap-3">
                            <Button
                                variant="light"
                                className="flex-grow-1 rounded-3 py-2 border-1"
                                style={{ borderColor: '#DB4437', color: '#DB4437', fontWeight: '600' }}
                            >
                                <i className="bi bi-google me-2"></i>Google
                            </Button>

                            <Button
                                variant="primary"
                                className="flex-grow-1 rounded-3 py-2 border-0"
                                style={{ backgroundColor: '#4267B2', color: '#fff', fontWeight: '600' }}
                            >
                                <i className="bi bi-facebook me-2"></i>Facebook
                            </Button>
                        </div>

                        <div className="text-center mt-4">
                            Chưa có tài khoản? <Link to="/register" className="fw-bold text-primary text-decoration-none">Đăng ký ngay</Link>
                        </div>
                    </div>
                </Col>
            </Row>
        </Container>
    );
}

export default Login;