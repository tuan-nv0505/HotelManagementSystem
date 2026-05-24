import React, { useState } from 'react';
import { Modal, Form, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';

const LoginModal = ({ show, handleClose }) => {
    const [isEmailMode, setIsEmailMode] = useState(false);
    const [credentials, setCredentials] = useState({ email: '', password: '' });

    const handleLogin = (e) => {
        e.preventDefault();
        console.log("Đăng nhập với:", credentials);
        alert("Đăng nhập thành công!");
        handleClose();
    };

    const onHide = () => {
        setIsEmailMode(false);
        handleClose();
    };

    return (
        <>
            <style>
                {`
                    .traveloka-modal {
                        border-radius: 24px !important;
                        border: none !important;
                        overflow: hidden;
                    }
                `}
            </style>

            <Modal
                show={show}
                onHide={onHide}
                centered
                contentClassName="traveloka-modal shadow-lg"
            >
                <div className="position-absolute top-0 end-0 p-3" style={{ zIndex: 1050 }}>
                    <button type="button" className="btn-close" aria-label="Close" onClick={onHide}></button>
                </div>

                <div style={{ backgroundColor: '#fff' }}>
                    {!isEmailMode ? (
                        <div className="d-flex flex-column">
                            <div className="position-relative p-4" style={{
                                background: 'linear-gradient(135deg, #e0f7fa 0%, #e8f5e9 100%)',
                                height: '140px'
                            }}>
                                <h4 className="fw-bold text-dark mt-2" style={{ width: '65%', lineHeight: '1.4', fontSize: '1.3rem' }}>
                                    Ưu đãi không thể chối từ!
                                </h4>

                                <div className="position-absolute" style={{ top: '20px', right: '25px', fontSize: '3rem', color: '#0194f3' }}>
                                    <i className="bi bi-gift"></i>
                                </div>
                            </div>

                            <div className="p-4 px-5 text-center mt-2">

                                <div className="d-flex gap-3 mb-4">
                                    <Button variant="light" className="flex-grow-1 rounded-pill border py-2 fw-bold text-dark bg-white shadow-sm d-flex justify-content-center align-items-center">
                                        <i className="bi bi-google me-2 fs-5" style={{ color: '#DB4437' }}></i> Google
                                    </Button>
                                    <Button variant="light" className="flex-grow-1 rounded-pill border py-2 fw-bold text-dark bg-white shadow-sm d-flex justify-content-center align-items-center">
                                        <i className="bi bi-facebook me-2 fs-5" style={{ color: '#4267B2' }}></i> Facebook
                                    </Button>
                                </div>

                                <a href="#!" className="fw-bold text-primary text-decoration-none fs-6" onClick={(e) => { e.preventDefault(); setIsEmailMode(true); }}>
                                    Tùy chọn khác
                                </a>

                                <p className="text-muted mt-3 mb-4 px-2" style={{ fontSize: '0.85rem' }}>
                                    Mức giá thấp hơn và phần thưởng đang chờ đợi. Mở khóa chúng bằng cách đăng nhập!
                                </p>

                                <p className="text-muted mt-4 mb-3" style={{ fontSize: '0.7rem' }}>
                                    Bằng cách tiếp tục, bạn đồng ý với <a href="#!" className="text-decoration-none fw-semibold">Điều khoản & Điều kiện</a> và xác nhận rằng bạn đã được thông báo về <a href="#!" className="text-decoration-none fw-semibold">Chính sách bảo mật</a>.
                                </p>

                                <a href="#!" className="fw-bold text-primary text-decoration-none mt-2 d-inline-block" onClick={(e) => { e.preventDefault(); onHide(); }}>
                                    Tiếp tục dưới dạng khách
                                </a>
                            </div>
                        </div>
                    ) : (
                        <div className="p-4 p-md-5 bg-white">
                            <div className="mb-4 text-center position-relative">
                                <a href="#!" className="text-muted text-decoration-none position-absolute start-0 top-0 mt-1" onClick={(e) => { e.preventDefault(); setIsEmailMode(false); }}>
                                    <i className="bi bi-arrow-left fs-5"></i>
                                </a>
                                <h3 className="fw-bold text-primary">Hotel Booking</h3>
                                <p className="text-muted small">Đăng nhập bằng tài khoản</p>
                            </div>

                            <Form onSubmit={handleLogin}>
                                <Form.Group className="mb-3">
                                    <Form.Label className="fw-semibold small">Email đăng nhập</Form.Label>
                                    <Form.Control
                                        type="email"
                                        placeholder="name@example.com"
                                        className="rounded-3 py-2"
                                        required
                                        onChange={(e) => setCredentials({ ...credentials, email: e.target.value })}
                                    />
                                </Form.Group>

                                <Form.Group className="mb-4">
                                    <div className="d-flex justify-content-between">
                                        <Form.Label className="fw-semibold small">Mật khẩu</Form.Label>
                                    </div>
                                    <Form.Control
                                        type="password"
                                        placeholder="••••••••"
                                        className="rounded-3 py-2"
                                        required
                                        onChange={(e) => setCredentials({ ...credentials, password: e.target.value })}
                                    />
                                </Form.Group>

                                <Button type="submit" size="lg" className="w-100 rounded-pill mb-3 border-0 py-2 fs-6 fw-bold" style={{ backgroundColor: '#0194f3' }}>
                                    Đăng nhập
                                </Button>
                            </Form>

                            <div className="text-center mt-4">
                                <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                                    Chưa có tài khoản? <Link to="/register" className="fw-bold text-primary text-decoration-none" onClick={onHide}>Đăng ký ngay</Link>
                                </p>
                            </div>
                        </div>
                    )}
                </div>
            </Modal>
        </>
    );
}

export default LoginModal;