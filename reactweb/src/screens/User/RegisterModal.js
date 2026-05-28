import React, { useState, useRef } from 'react';
import { Modal, Form, Button, Row, Col, Alert } from 'react-bootstrap';
import 'bootstrap-icons/font/bootstrap-icons.css';
import MySpinner from "../../components/MySpinner";
import Apis, { endpoints } from "../../configs/Apis";
import { AuthSwitchFooter, ModalCloseButton, SharedModalStyle } from './UserStyle';

const RegisterModal = ({ show, handleClose, showLogin }) => {
    const userInfo = [
        { field: "username", label: "Tên đăng nhập", type: "text", col: 6 },
        { field: "phone", label: "Số điện thoại", type: "tel", col: 6 },
        { field: "email", label: "Email", type: "email", col: 6 },
        { field: "password", label: "Mật khẩu", type: "password", col: 6 },
        { field: "confirm", label: "Xác nhận mật khẩu", type: "password", col: 6 }
    ];

    const [user, setUser] = useState({});
    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(false);
    const avatar = useRef();

    const onHide = () => {
        setUser({});
        setErrors({});
        if (avatar.current) {
            avatar.current.value = "";
        }
        handleClose();
    };

    const handleChange = (e, field) => {
        setUser({ ...user, [field]: e.target.value });
        setErrors({ ...errors, [field]: null });
    };

    const handleRegister = async (e) => {
        e.preventDefault();

        const cleanedUser = {};
        Object.keys(user).forEach(key => {
            cleanedUser[key] = typeof user[key] === 'string' ? user[key].trim() : user[key];
        });

        if (cleanedUser.password !== cleanedUser.confirm) {
            setErrors({ confirm: "Mật khẩu không khớp!" });
            return;
        }

        let form = new FormData();
        for (let key of Object.keys(cleanedUser)) {
            if (key !== 'confirm') {
                form.append(key, cleanedUser[key]);
            }
        }

        if (avatar.current && avatar.current.files.length > 0) {
            form.append('file', avatar.current.files[0]);
        }

        try {
            setLoading(true);
            const res = await Apis.post(endpoints['register'], form, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });

            if (res.status === 201) {
                alert("Đăng ký thành công!");
                onHide();
                if (showLogin) showLogin();
            }
        } catch (ex) {
           console.error(ex);
            if (ex.response && ex.response.data) {
                const responseData = ex.response.data;
                if (ex.response.status === 400 && responseData.errors) {
                    setErrors(responseData.errors); 
                } 
                else if (responseData.message) {
                    setErrors({ general: responseData.message });
                } 
                else {
                    setErrors({ general: "Dữ liệu không hợp lệ!" });
                }
            } 
            else {
                setErrors({ general: "Không thể kết nối đến máy chủ. Vui lòng thử lại sau!" });
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <SharedModalStyle />

            <Modal show={show} onHide={onHide} centered size="lg" contentClassName="bg-transparent border-0" >
                <Modal.Body className="p-0 position-relative">
                    <img src="https://cdn3d.iconscout.com/3d/free/thumb/free-gift-box-4061633-3363945.png" alt="Quà tặng" className="position-absolute d-none d-sm-block"
                        style={{ top: '-35px', right: '15px', width: '130px', zIndex: 1060 }}
                    />
                    <div className="bg-white shadow-lg position-relative" style={{ borderRadius: '24px', overflow: 'hidden' }}>

                        <ModalCloseButton onClick={onHide} />

                        <div className="p-4" style={{
                            background: 'linear-gradient(135deg, #e3f2fd 0%, #ffffff 100%)',
                            borderBottom: '1px solid #eee'
                        }}>
                            <h4 className="fw-bold text-primary mb-1">Gia nhập Hotel Booking</h4>
                            <p className="text-muted small mb-0">Hàng ngàn ưu đãi đang chờ đón thành viên mới.</p>
                        </div>

                        <div className="p-4 p-md-5">
                            {errors.general && <Alert variant="danger" className="py-2 small">{errors.general}</Alert>}

                            <Form onSubmit={handleRegister}>
                                <Row className="g-3">
                                    {userInfo.map(u => (
                                        <Col xs={u.col} key={u.field}>
                                            <Form.Group>
                                                <Form.Label className="fw-semibold text-dark mb-1 small">{u.label}</Form.Label>
                                                <Form.Control
                                                    type={u.type}
                                                    placeholder={`Nhập ${u.label.toLowerCase()}...`}
                                                    className="rounded-3 py-2 shadow-sm border-light"
                                                    style={{ fontSize: '0.85rem' }}
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

                                    <Col xs={6}>
                                        <Form.Group>
                                            <Form.Label className="fw-semibold text-dark mb-1 small">Ảnh đại diện (Không bắt buộc)</Form.Label>
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
                                    {loading ? <MySpinner /> :
                                        <Button
                                            type="submit"
                                            className="w-100 rounded-pill border-0 py-2 fw-bold shadow"
                                            style={{ backgroundColor: '#0194f3', fontSize: '1rem' }}
                                        >
                                            Đăng ký tài khoản
                                        </Button>
                                    }
                                </div>
                            </Form>

                            <AuthSwitchFooter
                                text="Đã có tài khoản?"
                                linkText="Đăng nhập ngay"
                                onClick={() => { onHide(); if (showLogin) showLogin(); }}
                            />
                        </div>
                    </div>
                </Modal.Body>
            </Modal>
        </>
    );
}

export default RegisterModal;