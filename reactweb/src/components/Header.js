import React, { useState, useContext, useEffect } from 'react';
import { Navbar, Nav, Container, Button } from 'react-bootstrap';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';
import LoginModal from '../screens/User/LoginModal';
import RegisterModal from '../screens/User/RegisterModal';
import { MyUserContext } from "../configs/Contexts";
import cookies from 'react-cookies';
import { useSearchParams } from "react-router-dom";

const Header = () => {
    const [user, dispatch] = useContext(MyUserContext);
    const [showLoginModal, setShowLoginModal] = useState(false);
    const [showRegisterModal, setShowRegisterModal] = useState(false);
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const navigate = useNavigate();
    const [q, setQ] = useSearchParams();

    const logout = (e) => {
        e.preventDefault();
        cookies.remove('token', { path: '/' });
        cookies.remove('user', { path: '/' });
        dispatch({ type: "LOGOUT" });
        setIsDropdownOpen(false);
        navigate('/')
    };

    const toggleMenu = () => {
        setIsDropdownOpen(!isDropdownOpen);
    };

    const location = useLocation();
    const isHomePage = location.pathname === '/';

    useEffect(() => {
        if (q.get('login') === 'true') {
            setShowLoginModal(true);
            setQ({});
        }
    }, [q, setQ]);

    return (
        <>
            <LoginModal
                show={showLoginModal}
                handleClose={() => setShowLoginModal(false)}
                showRegister={() => setShowRegisterModal(true)}
            />

            <RegisterModal
                show={showRegisterModal}
                handleClose={() => setShowRegisterModal(false)}
                showLogin={() => setShowLoginModal(true)}
            />

            <Navbar
                expand="lg"
                className="py-3"
                style={{
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    right: 0,
                    zIndex: 100,
                    backgroundColor: isHomePage ? 'transparent' : '#2c3e50',
                    transition: 'background-color 0.4s ease',
                    boxShadow: isHomePage ? 'none' : '0 2px 10px rgba(0,0,0,0.1)'
                }}
            >
                <Container>
                    <Navbar.Brand as={Link} to="/" className="fw-bold fs-3 d-flex align-items-center text-white">
                        <i className="bi bi-buildings-fill me-2"></i>
                        HOTEL BOOKING SYSTEM
                    </Navbar.Brand>

                    <Navbar.Toggle aria-controls="basic-navbar-nav" className="border-0 shadow-none text-white" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="ms-auto align-items-center">

                            <div className="vr mx-3 d-none d-lg-block bg-white opacity-50" style={{ height: '24px', alignSelf: 'center' }}></div>

                            {user === null ? (
                                <>
                                    <Button
                                        onClick={() => setShowLoginModal(true)}
                                        className="me-2 fw-bold rounded-pill px-4 py-2 d-flex align-items-center border-0"
                                        style={{ backgroundColor: '#ffffff', color: '#0194f3' }}
                                    >
                                        Đăng nhập
                                        <i className="bi bi-box-arrow-in-right ms-2"></i>
                                    </Button>

                                    <Button
                                        onClick={() => setShowRegisterModal(true)}
                                        className="fw-bold border-0 rounded-pill px-4 py-2"
                                        style={{ backgroundColor: '#0194f3', color: '#ffffff' }}
                                    >
                                        Đăng ký
                                    </Button>
                                </>
                            ) : (
                                <div style={{ position: 'relative' }}>
                                    <div
                                        className="d-flex align-items-center text-white"
                                        onClick={toggleMenu}
                                        style={{ cursor: 'pointer' }}
                                    >
                                        <img
                                            src={user?.avatar || "https://cdn-icons-png.flaticon.com/512/149/149071.png"}
                                            width="38"
                                            height="38"
                                            className="rounded-circle me-2 border border-white shadow-sm"
                                            alt="avatar"
                                            style={{ objectFit: 'cover' }}
                                            onError={(e) => {
                                                e.target.src = "https://cdn-icons-png.flaticon.com/512/149/149071.png";
                                            }}
                                        />
                                        <span className="fw-bold me-2">{user?.name || user?.username}</span>
                                        <i className={`bi bi-chevron-${isDropdownOpen ? 'up' : 'down'} small`}></i>
                                    </div>

                                    {isDropdownOpen && (
                                        <div
                                            className="dropdown-menu show shadow border-0 mt-3"
                                            style={{ right: 0, left: 'auto', minWidth: '230px', borderRadius: '12px', padding: '10px 0' }}
                                        >
                                            <Link
                                                to="/profile"
                                                className="dropdown-item py-2 d-flex align-items-center text-dark"
                                                style={{ fontWeight: '500' }}
                                                onClick={() => setIsDropdownOpen(false)}
                                            >
                                                <i className="bi bi-person-lines-fill me-3 fs-5 text-secondary"></i>
                                                Thông tin cá nhân
                                            </Link>

                                            <div className="dropdown-divider my-2"></div>

                                            <button
                                                className="dropdown-item py-2 d-flex align-items-center text-danger fw-bold"
                                                onClick={logout}
                                            >
                                                <i className="bi bi-power me-3 fs-5"></i>
                                                Đăng xuất
                                            </button>
                                        </div>
                                    )}
                                </div>
                            )}
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
        </>
    );
}

export default Header;