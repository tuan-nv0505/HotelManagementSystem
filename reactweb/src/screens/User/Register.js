import React, { useState, useRef } from 'react';
import { Container, Row, Col, Form, Button, Alert } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';
import MySpinner from "../../components/MySpinner";
import Apis, { endpoints } from "../../configs/Apis";

const Register = () => {
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
        if (user.password !== user.confirm) {
            setErrors({ confirm: "Mật khẩu nhập lại không khớp!" });
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
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            if (res.status === 201) {
                alert("Đăng ký thành công!");
                nav('/login');
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
        <Container fluid className="min-vh-100 p-0">
            <Row className="g-0 min-vh-100">
                <Col md={6} className="d-flex align-items-center justify-content-center p-4 bg-white">
                    <div style={{ width: '100%', maxWidth: '600px' }}>
                        <div className="text-center mb-4">
                            <h2 className="fw-bold text-primary">Tạo tài khoản mới</h2>
                            <p className="text-muted">Tham gia ngay để nhận ưu đãi đặt phòng tốt nhất</p>
                        </div>

                        <Form onSubmit={handleRegister}>
                            {errors.general && <Alert variant="danger">{errors.general}</Alert>}
                            
                            <Row className="g-3 mb-3">
                                {userInfo.map(u => (
                                    <Col md={u.col} key={u.field}>
                                        <Form.Group>
                                            <Form.Label className="fw-semibold text-dark mb-1" style={{ fontSize: '0.9rem' }}>
                                                {u.label}
                                            </Form.Label>
                                            <Form.Control
                                                type={u.type}
                                                placeholder={`Nhập ${u.label.toLowerCase()}`}
                                                className="rounded-3 py-2" 
                                                style={{ fontSize: '0.9rem' }} 
                                                value={user[u.field] || ""}
                                                onChange={(e) => handleChange(e, u.field)}
                                                required
                                                isInvalid={!!errors[u.field]}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors[u.field]}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                ))}

                                <Col md={12}>
                                    <Form.Group>
                                        <Form.Label className="fw-semibold text-dark mb-1" style={{ fontSize: '0.9rem' }}>
                                            Ảnh đại diện (Không bắt buộc)
                                        </Form.Label>
                                        <Form.Control
                                            type="file"
                                            accept="image/*"
                                            className="rounded-3 py-2"
                                            style={{ fontSize: '0.9rem' }}
                                            ref={avatar}
                                        />
                                    </Form.Group>
                                </Col>
                            </Row>

                            <div className="mt-4 mb-3">
                                {loading ? (
                                    <MySpinner />
                                ) : (
                                    <Button type="submit" size="lg" className="w-100 rounded-3 border-0 py-2 fw-bold" style={{ backgroundColor: '#0194f3' }}>
                                        Đăng ký tài khoản
                                    </Button>
                                )}
                            </div>
                        </Form>

                        <div className="text-center mt-3" style={{ fontSize: '0.95rem' }}>
                            Đã có tài khoản? <Link to="/login" className="fw-bold text-primary text-decoration-none">Đăng nhập tại đây</Link>
                        </div>
                    </div>
                </Col>

                <Col md={6} className="d-none d-md-flex align-items-center justify-content-center position-relative"
                    style={{
                        backgroundImage: 'url("https://images.unsplash.com/photo-1566073771259-6a8506099945?q=80&w=2000&auto=format&fit=crop")',
                        backgroundSize: 'cover',
                        backgroundPosition: 'center'
                    }}
                >
                    <div className="position-absolute top-0 start-0 w-100 h-100" style={{ background: 'linear-gradient(to right, rgba(0,0,0,0.5), rgba(0,0,0,0.1))' }}></div>
                    <div className="position-relative text-white text-center p-5">
                        <h2 className="fw-bold display-4 mb-3" style={{ textShadow: '2px 2px 4px rgba(0,0,0,0.5)' }}>Bắt đầu hành trình<br />của bạn</h2>
                        <p className="fs-5" style={{ textShadow: '1px 1px 2px rgba(0,0,0,0.5)' }}>Hàng ngàn khách sạn cao cấp đang chờ đón.</p>
                    </div>
                </Col>
            </Row>
        </Container>
    );
}

export default Register;