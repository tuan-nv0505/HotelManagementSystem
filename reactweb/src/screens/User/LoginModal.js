import React, { useState, useContext } from 'react';
import { Modal, Form, Button, Alert } from 'react-bootstrap';
import 'bootstrap-icons/font/bootstrap-icons.css';
import MySpinner from "../../components/MySpinner";
import Apis, { authApis, endpoints } from "../../configs/Apis";
import { useNavigate, useSearchParams } from "react-router-dom";
import cookies from 'react-cookies';
import { MyUserContext } from "../../configs/Contexts";
import { AuthSwitchFooter, ModalCloseButton, SharedModalStyle, SocialButton } from './UserStyle';
import { GoogleLogin } from '@react-oauth/google';
import FacebookLogin from '@greatsumini/react-facebook-login';

const LoginModal = ({ show, handleClose, showRegister }) => {
    const [isEmailMode, setIsEmailMode] = useState(false);
    const [user, setUser] = useState({});
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");

    const [, dispatch] = useContext(MyUserContext);
    const [q] = useSearchParams();
    const nav = useNavigate();

    const handleLoginComplete = (userData) => {
        dispatch({ "type": "LOGIN", "payload": userData });
        onHide();
        const phone = String(userData.phone).trim();
        if (phone === '0000000000' || phone === '0') {
            nav('/profile', { state: { showUpdateAlert: true } });
        } else {
            let next = q.get('next');
            nav(next ? next : '/');
        }
    };

    const handleGoogleSuccess = async (credentialResponse) => {
        try {
            setLoading(true); setErr("");
            let res = await Apis.post(endpoints['google'], { idToken: credentialResponse.credential });
            cookies.save('token', res.data.token, { path: '/' });
            cookies.save('user', res.data.user, { path: '/' });
            handleLoginComplete(res.data.user);
        } catch (ex) { setErr("Xác thực Google thất bại!"); } finally { setLoading(false); }
    };

    const handleFacebookSuccess = async (response) => {
        if (response && response.accessToken) {
            try {
                setLoading(true); setErr("");
                let res = await Apis.post(endpoints['facebook'], { accessToken: response.accessToken });
                cookies.save('token', res.data.token, { path: '/' });
                cookies.save('user', res.data.user, { path: '/' });
                handleLoginComplete(res.data.user);
            } catch (ex) { setErr("Xác thực Facebook thất bại!"); } finally { setLoading(false); }
        }
    };

    const login = async (e) => {
        e.preventDefault(); setErr("");
        try {
            setLoading(true);
            let res = await Apis.post(endpoints['login'], { ...user });
            cookies.save('token', res.data.token, { path: '/' });
            let p = await authApis().get(endpoints['profile']);
            cookies.save('user', p.data, { path: '/' });
            handleLoginComplete(p.data);
        } catch (ex) { setErr("Tên đăng nhập hoặc mật khẩu không chính xác!"); } finally { setLoading(false); }
    };

    const onHide = () => { setIsEmailMode(false); setUser({}); setErr(""); handleClose(); };

    return (
        <>
            <SharedModalStyle />
            <Modal show={show} onHide={onHide} centered contentClassName="traveloka-modal shadow-lg">
                <ModalCloseButton onClick={onHide} />
                <div style={{ backgroundColor: '#fff' }}>

                    <div className="p-4 d-flex justify-content-between align-items-center" style={{ background: '#f8fbff' }}>
                        <div>
                            <h4 className="fw-bold text-primary m-0" style={{ fontSize: '1.25rem' }}>Hotel Booking</h4>
                            <p className="text-muted small m-0 mt-1">Chúng tôi có một thỏa thuận mà bạn không thể cưỡng lại</p>
                        </div>
                        <img
                            src="https://cdn-icons-png.flaticon.com/512/3063/3063823.png"
                            alt="Welcome"
                            style={{ width: '65px', height: '65px', objectFit: 'contain' }}
                        />
                    </div>
                    <hr className="my-0" style={{ color: '#eee' }} />

                    {!isEmailMode ? (
                        <div className="p-4 px-md-5 px-3 text-center">
                            {err && <Alert variant="danger" className="small py-2">{err}</Alert>}
                            {loading && <div className="mb-3"><MySpinner /></div>}

                            <div className="d-flex justify-content-center gap-3 mb-4 mt-3">
                                <div style={{ position: 'relative', width: '150px', height: '40px' }}>
                                    <div style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', zIndex: 1 }}>
                                        <SocialButton label="Google" iconSrc="https://cdn1.iconfinder.com/data/icons/google-s-logo/150/Google_Icons-09-512.png" />
                                    </div>
                                    <GoogleLogin
                                        onSuccess={handleGoogleSuccess}
                                        onError={() => setErr("Lỗi Google")}
                                        containerProps={{
                                            style: { position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', opacity: 0, zIndex: 2, cursor: 'pointer' }
                                        }}
                                    />
                                </div>
                                <div style={{ width: '150px', height: '40px' }}>
                                    <FacebookLogin appId={process.env.REACT_APP_FACEBOOK_APP_ID} onSuccess={handleFacebookSuccess} render={({ onClick }) => (
                                        <SocialButton onClick={onClick} iconSrc="https://upload.wikimedia.org/wikipedia/commons/b/b8/2021_Facebook_icon.svg" label="Facebook" />
                                    )} />
                                </div>
                            </div>

                            <hr />
                            <a href="#!" className="fw-bold text-primary text-decoration-none small" onClick={(e) => { e.preventDefault(); setIsEmailMode(true); setErr(""); }}>
                                Đăng nhập bằng tài khoản Hotel Booking
                            </a>
                        </div>
                    ) : (
                        <div className="p-4 p-md-5 bg-white">
                            <div className="d-flex align-items-center mb-4">
                                <a href="#!" className="text-muted me-3" onClick={(e) => { e.preventDefault(); setIsEmailMode(false); setErr(""); }}>
                                    <i className="bi bi-arrow-left fs-4"></i>
                                </a>
                                <h4 className="fw-bold text-primary m-0">Đăng nhập</h4>
                            </div>

                            {err && <Alert variant="danger" className="small py-2">{err}</Alert>}
                            {loading && <div className="mb-3 text-center"><MySpinner /></div>}

                            <Form onSubmit={login}>
                                <Form.Group className="mb-3">
                                    <Form.Label className="small fw-bold">Tên đăng nhập</Form.Label>
                                    <Form.Control type="text" placeholder="Nhập tên đăng nhập..." value={user.username || ""} onChange={(e) => setUser({ ...user, username: e.target.value })} required />
                                </Form.Group>
                                <Form.Group className="mb-4">
                                    <Form.Label className="small fw-bold">Mật khẩu</Form.Label>
                                    <Form.Control type="password" placeholder="Nhập mật khẩu..." value={user.password || ""} onChange={(e) => setUser({ ...user, password: e.target.value })} required />
                                </Form.Group>
                                <Button type="submit" className="w-100 rounded-pill border-0 py-2 fw-bold" style={{ backgroundColor: '#0194f3' }}>
                                    Đăng nhập
                                </Button>
                            </Form>
                            <AuthSwitchFooter text="Chưa có tài khoản?" linkText="Đăng ký ngay" onClick={() => { onHide(); if (showRegister) showRegister(); }} />
                        </div>
                    )}
                </div>
            </Modal>
        </>
    );
}

export default LoginModal;