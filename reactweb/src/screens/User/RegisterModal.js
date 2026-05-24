import React, { useState, useRef } from 'react';
import { Modal, Form, Button, Row, Col, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';
import MySpinner from "../../components/MySpinner";
import Apis, { endpoints } from "../../configs/Apis";

const RegisterModal = ({ show, handleClose }) => {
    // Luồng xử lý dữ liệu động của bạn
    const userInfo = [
        { field: "name", label: "Họ và tên", type: "text", col: 6 },
        { field: "phone", label: "Số điện thoại", type: "tel", col: 6 },
        { field: "email", label: "Email", type: "email", col: 12 },
        { field: "username", label: "Tên đăng nhập", type: "text", col: 12 },
        { field: "password", label: "Mật khẩu", type: "password", col: 6 },
        { field: "confirm", label: "Xác nhận mật khẩu", type: "password", col: 6 }
    ];

    const [user, setUser] = useState({});
    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(false);
    const avatar = useRef();
    const nav = useNavigate();

    const handleChange = (e, field) => {
        setUser({ ...user, [field]: e.target.value });
        setErrors({ ...errors, [field]: null });
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        
        // Validate password
        if (user.password !== user.confirm) {
            setErrors({ confirm: "Mật khẩu không khớp!" });
            return;
        }

        let form = new FormData();
        for (let key of Object.keys(user)) {
            if (key !== 'confirm') {
                form.append(key, user[key]);
            }
        }

        if (avatar.current && avatar.current.files.length > 0) {
            form.append('avatar', avatar.current.files[0]);
        }

        try {
            setLoading(true);
            const res = await Apis.post(endpoints['register'], form, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });

            if (res.status === 201) {
                alert("Đăng ký thành công!");
                handleClose(); // Đóng modal
                nav('/login'); // Hoặc bật Modal Login nếu muốn
            }
        } catch (ex) {
            console.error(ex);
            if (ex.response && ex.response.status === 400) {
                setErrors(ex.response.data.errors || { general: "Dữ liệu không hợp lệ!" });
            } else {
                setErrors({ general: "Đã có lỗi xảy ra từ máy chủ." });
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <style>
                {`
                    .register-modal-content {
                        border-radius: 24px !important;
                        border: none !important;
                        overflow: hidden;
                    }
                    .modal.show .modal-dialog {
                        transform: none !important;
                        animation: zoomIn 0.3s ease-out forwards;
                    }
                    @keyframes zoomIn {
                        from { opacity: 0; transform: scale(0.8); }
                        to { opacity: 1; transform: scale(1); }
                    }
                `}
            </style>

            <Modal 
                show={show} 
                onHide={handleClose} 
                centered 
                size="lg" 
                contentClassName="register-modal-content shadow-lg"
            >
                {/* Nút X đóng modal */}
                <div className="position-absolute top-0 end-0 p-3" style={{ zIndex: 1050 }}>
                    <button type="button" className="btn-close" onClick={handleClose}></button>
                </div>

                <Modal.Body className="p-0 bg-white">
                    {/* Header giống Traveloka: Gradient và Icon */}
                    <div className="p-4 position-relative" style={{ 
                        background: 'linear-gradient(135deg, #e3f2fd 0%, #ffffff 100%)',
                        borderBottom: '1px solid #eee'
                    }}>
                        <h4 className="fw-bold text-primary mb-1">Gia nhập Hotel Booking</h4>
                        <p className="text-muted small mb-0">Hàng ngàn ưu đãi đang chờ đón thành viên mới.</p>
                        <div className="position-absolute" style={{ top: '15px', right: '40px', fontSize: '3.5rem', color: '#90caf9', opacity: 0.6 }}>
                            <i className="bi bi-person-plus-fill"></i>
                        </div>
                    </div>

                    <div className="p-4 p-md-5">
                        {errors.general && <Alert variant="danger" className="py-2 small">{errors.general}</Alert>}

                        <Form onSubmit={handleRegister}>
                            <Row className="g-3">
                                {userInfo.map(u => (
                                    <Col md={u.col} key={u.field}>
                                        <Form.Group>
                                            <Form.Label className="fw-semibold text-dark mb-1 small">{u.label}</Form.Label>
                                            <Form.Control
                                                type={u.type}
                                                placeholder={`Nhập ${u.label.toLowerCase()}...`}
                                                className="rounded-3 py-2 shadow-sm border-light"
                                                style={{ fontSize: '0.85rem' }} // Placeholder nhỏ gọn chuyên nghiệp
                                                value={user[u.field] || ""}
                                                onChange={(e) => handleChange(e, u.field)}
                                                required
                                                isInvalid={!!errors[u.field]}
                                            />
                                            <Form.Control.Feedback type="invalid" className="small">
                                                {errors[u.field]}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                ))}

                                <Col md={12}>
                                    <Form.Group>
                                        <Form.Label className="fw-semibold text-dark mb-1 small">Ảnh đại diện</Form.Label>
                                        <Form.Control
                                            type="file"
                                            accept="image/*"
                                            className="rounded-3 py-2 shadow-sm border-light"
                                            style={{ fontSize: '0.85rem' }}
                                            ref={avatar}
                                        />
                                    </Form.Group>
                                </Col>
                            </Row>

                            <div className="mt-4">
                                {loading ? (
                                    <MySpinner />
                                ) : (
                                    <Button 
                                        type="submit" 
                                        className="w-100 rounded-pill border-0 py-2 fw-bold shadow" 
                                        style={{ backgroundColor: '#0194f3', fontSize: '1rem' }}
                                    >
                                        Đăng ký tài khoản
                                    </Button>
                                )}
                            </div>
                        </Form>

                        <div className="text-center mt-4 pt-2">
                            <span className="text-muted small">Đã có tài khoản? </span>
                            <a href="#!" className="fw-bold text-primary text-decoration-none small" onClick={(e) => { e.preventDefault(); handleClose(); nav('/login'); }}>
                                Đăng nhập ngay
                            </a>
                        </div>
                    </div>
                </Modal.Body>
            </Modal>
        </>
    );
}

export default RegisterModal;