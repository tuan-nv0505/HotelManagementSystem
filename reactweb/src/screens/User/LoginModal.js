import React, { useState, useContext } from 'react';
import { Modal, Form, Button, Alert } from 'react-bootstrap';
import 'bootstrap-icons/font/bootstrap-icons.css';
import MySpinner from "../../components/MySpinner";
import Apis, { authApis, endpoints } from "../../configs/Apis";
import { useNavigate, useSearchParams } from "react-router-dom";
import cookies from 'react-cookies';
import { MyUserContext } from "../../configs/Contexts";
import { AuthSwitchFooter, ModalCloseButton, SharedModalStyle } from './UserStyle';
import { useGoogleLogin } from '@react-oauth/google';
import FacebookLogin from '@greatsumini/react-facebook-login';

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

    const handleGoogleSuccess = async (credentialResponse) => {
        try {
            setLoading(true);
            setErr("");
            let res = await Apis.post(endpoints['google'], null, {
                params: { idToken: credentialResponse.credential }
            });

            const jwtToken = res.data.token;
            const userData = res.data.user;

            cookies.save('token', jwtToken, { path: '/' });
            cookies.save('user', userData, { path: '/' });

            dispatch({ "type": "LOGIN", "payload": userData });
            onHide();
            let next = q.get('next');
            if (next) nav(next); else nav('/');
        } catch (ex) {
            console.error("Google Login Error:", ex);
            setErr("Xác thực Google thất bại. Vui lòng thử lại!");
        } finally {
            setLoading(false);
        }
    };

    const handleFacebookSuccess = async (response) => {
        if (response && response.accessToken) {
            try {
                setLoading(true);
                setErr("");
                let res = await Apis.post(endpoints['facebook'], null, {
                    params: { accessToken: response.accessToken }
                });

                const jwtToken = res.data.token;
                const userData = res.data.user;

                cookies.save('token', jwtToken, { path: '/' });
                cookies.save('user', userData, { path: '/' });

                dispatch({ "type": "LOGIN", "payload": userData });
                onHide();
                let next = q.get('next');
                if (next) nav(next); else nav('/');
            } catch (ex) {
                console.error("Facebook Login Error:", ex);
                setErr("Xác thực Facebook thất bại. Vui lòng thử lại!");
            } finally {
                setLoading(false);
            }
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
                <ModalCloseButton onClick={onHide} />

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
                            <div className="p-4 px-md-5 px-3 text-center mt-2">
                                {err && <Alert variant="danger" className="small py-2">{err}</Alert>}
                                {loading && <MySpinner />}

                                <div className="d-flex justify-content-center gap-3 mb-4" style={{ display: loading ? 'none' : 'flex' }}>

                                    {/* Nút Google (Custom) */}
                                    <button
                                        type="button"
                                        onClick={() => loginGoogle()}
                                        style={{
                                            width: '180px',
                                            height: '40px',
                                            borderRadius: '20px',
                                            backgroundColor: '#ffffff',
                                            color: '#3c4043',
                                            border: '1px solid #dadce0',
                                            fontWeight: '500',
                                            fontSize: '14px',
                                            fontFamily: '"Google Sans", Roboto, Arial, sans-serif',
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                            gap: '10px',
                                            cursor: 'pointer',
                                            boxShadow: '0 1px 2px 0 rgba(60,64,67,0.3)',
                                            transition: 'background-color 0.2s',
                                        }}
                                        onMouseOver={(e) => e.currentTarget.style.backgroundColor = '#f8f9fa'}
                                        onMouseOut={(e) => e.currentTarget.style.backgroundColor = '#ffffff'}
                                    >
                                        <img
                                            src="https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg"
                                            alt="Google"
                                            style={{ width: '20px', height: '20px' }}
                                        />
                                        <span>Google</span>
                                    </button>

                                    {/* Nút Facebook (Custom) */}
                                    <FacebookLogin
                                        appId="1325779246184552"
                                        onSuccess={handleFacebookSuccess}
                                        onFail={(error) => {
                                            console.error('Lỗi đăng nhập Facebook:', error);
                                            setErr("Đăng nhập Facebook bị hủy hoặc thất bại.");
                                        }}
                                        render={({ onClick }) => (
                                            <button
                                                type="button"
                                                onClick={onClick}
                                                style={{
                                                    width: '180px',
                                                    height: '40px',
                                                    backgroundColor: '#fff',
                                                    border: '1px solid #dadce0',
                                                    borderRadius: '20px',
                                                    boxShadow: '0 1px 2px 0 rgba(60,64,67,0.3)',
                                                    display: 'flex',
                                                    alignItems: 'center',
                                                    justifyContent: 'center',
                                                    gap: '8px',
                                                    cursor: 'pointer',
                                                    transition: 'background-color 0.2s'
                                                }}
                                                onMouseOver={(e) => e.currentTarget.style.backgroundColor = '#f8f9fa'}
                                                onMouseOut={(e) => e.currentTarget.style.backgroundColor = '#fff'}
                                            >
                                                <img
                                                    src="https://upload.wikimedia.org/wikipedia/commons/b/b8/2021_Facebook_icon.svg"
                                                    alt="Facebook"
                                                    style={{ width: '20px', height: '20px' }}
                                                />
                                                <span style={{
                                                    color: '#3c4043',
                                                    fontSize: '14px',
                                                    fontWeight: '500',
                                                    fontFamily: '"Google Sans", Roboto, Arial, sans-serif'
                                                }}>
                                                    Facebook
                                                </span>
                                            </button>
                                        )}
                                    />
                                </div>

                                <a href="#!" className="fw-bold text-primary text-decoration-none" onClick={(e) => { e.preventDefault(); setErr(""); setIsEmailMode(true); }}>
                                    Tùy chọn đăng nhập khác
                                </a>
                            </div>
                        </div>
                    ) : (
                        <div className="p-4 p-md-5 bg-white">
                            <div className="mb-4 text-center position-relative">
                                <a href="#!" className="text-muted position-absolute start-0 top-0 mt-1" onClick={(e) => { e.preventDefault(); setErr(""); setIsEmailMode(false); }}>
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