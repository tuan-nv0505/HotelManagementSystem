import React, { useState, useContext } from 'react';
import { Modal, Form, Button, Alert } from 'react-bootstrap';
import 'bootstrap-icons/font/bootstrap-icons.css';
import MySpinner from "../../components/MySpinner";
import Apis, { authApis, endpoints } from "../../configs/Apis";
import { useNavigate, useSearchParams } from "react-router-dom";
import cookies from 'react-cookies';
import { MyUserContext } from "../../configs/Contexts";
import { AuthSwitchFooter, ModalCloseButton, SharedModalStyle } from './UserStyle';

const LoginModal = ({ show, handleClose, showRegister }) => {
    const [isEmailMode, setIsEmailMode] = useState(false);
    const [user, setUser] = useState({});
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");

    const [, dispatch] = useContext(MyUserContext);
    const [q] = useSearchParams();
    const nav = useNavigate();

    const userInfo = [
        { field: "username", label: "Tên đăng nhập", type: "text" },
        { field: "password", label: "Mật khẩu", type: "password" }
    ];

    const login = async (e) => {
        e.preventDefault();
        setErr("");

        try {
            setLoading(true);

            let res = await Apis.post(endpoints['login'], { ...user });
            const token = res.data.token;
            cookies.save('token', token, { path: '/' });

            let p = await authApis().get(endpoints['profile']);
            cookies.save('user', p.data, { path: '/' });

            dispatch({
                "type": "LOGIN",
                "payload": p.data
            });

            onHide();
            // Xu ly tiep tuc trang thai neu chua dang nhap
            let next = q.get('next');
            if (next) nav(next);
            else nav('/');

        } catch (ex) {
            console.error(ex);
            setErr("Tên đăng nhập hoặc mật khẩu không chính xác!");
        } finally {
            setLoading(false);
        }
    };

    const onHide = () => {
        setIsEmailMode(false);
        setUser({});
        setErr("");
        handleClose();
    };

    return (
        <>
            <SharedModalStyle />

            <Modal show={show} onHide={onHide} centered contentClassName="traveloka-modal shadow-lg">
                <ModalCloseButton onClick={onHide}/>

                <div style={{ backgroundColor: '#fff' }}>
                    {!isEmailMode ? (
                        <div className="d-flex flex-column">
                            <div className="p-4" style={{ background: 'linear-gradient(135deg, #e0f7fa 0%, #e8f5e9 100%)', height: '140px' }}>
                                <h4 className="fw-bold text-dark mt-2" style={{ width: '65%', lineHeight: '1.4', fontSize: '1.3rem' }}>
                                    Ưu đãi không thể chối từ!
                                </h4>
                                <div className="position-absolute" style={{ top: '20px', right: '25px', fontSize: '3rem', color: '#0194f3' }}>
                                    <i className="bi bi-gift"></i>
                                </div>
                            </div>
                            <div className="p-4 px-5 text-center mt-2">
                                <div className="d-flex gap-3 mb-4">
                                    <Button variant="light" className="flex-grow-1 rounded-pill border py-2 fw-bold text-dark bg-white shadow-sm">
                                        <i className="bi bi-google me-2" style={{ color: '#DB4437' }}></i> Google
                                    </Button>
                                    <Button variant="light" className="flex-grow-1 rounded-pill border py-2 fw-bold text-dark bg-white shadow-sm">
                                        <i className="bi bi-facebook me-2" style={{ color: '#4267B2' }}></i> Facebook
                                    </Button>
                                </div>
                                <a href="#!" className="fw-bold text-primary text-decoration-none" onClick={(e) => { e.preventDefault(); setIsEmailMode(true); }}>
                                    Tùy chọn khác
                                </a>
                            </div>
                        </div>
                    ) : (
                        <div className="p-4 p-md-5 bg-white">
                            <div className="mb-4 text-center position-relative">
                                <a href="#!" className="text-muted position-absolute start-0 top-0 mt-1" onClick={(e) => { e.preventDefault(); setIsEmailMode(false); }}>
                                    <i className="bi bi-arrow-left fs-5"></i>
                                </a>
                                <h3 className="fw-bold text-primary">Hotel Booking</h3>
                            </div>

                            {err && <Alert variant="danger" className="small py-2">{err}</Alert>}

                            <Form onSubmit={login}>
                                {userInfo.map(u => (
                                    <Form.Group className={u.field === 'password' ? "mb-4" : "mb-3"} key={u.field}>
                                        <Form.Label className="small fw-semibold">{u.label}</Form.Label>
                                        <Form.Control
                                            type={u.type}
                                            placeholder={u.type === 'password' ? "••••••••" : `Nhập ${u.label.toLowerCase()}...`}
                                            className="rounded-3 py-2 shadow-sm border-light"
                                            style={{ fontSize: '0.85rem' }}
                                            value={user[u.field] || ""}
                                            onChange={(e) => setUser({ ...user, [u.field]: e.target.value })}
                                            required
                                        />
                                    </Form.Group>
                                ))}

                                {loading ? <MySpinner /> :
                                    <Button type="submit" className="w-100 rounded-pill border-0 py-2 fw-bold" style={{ backgroundColor: '#0194f3' }}>
                                        Đăng nhập
                                    </Button>
                                }
                            </Form>

                            <AuthSwitchFooter 
                                text="Chưa có tài khoản?" 
                                linkText="Đăng ký ngay" 
                                onClick={() => { onHide(); if (showRegister) showRegister(); }} 
                            />
                        </div>
                    )}
                </div>
            </Modal>
        </>
    );
}

export default LoginModal;