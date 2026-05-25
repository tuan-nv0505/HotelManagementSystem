import React, { useState } from 'react';
import { Navbar, Nav, Container, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';
import LoginModal from '../screens/User/LoginModal';
import RegisterModal from '../screens/User/RegisterModal';
import { useContext } from "react";
import { MyUserContext } from "../configs/Contexts";

const Header = () => {
    const [user, dispatch] = useContext(MyUserContext);
    const [showLoginModal, setShowLoginModal] = useState(false);
    const [showRegisterModal, setShowRegisterModal] = useState(false);

    const logout = () => {
        dispatch({ "type": "LOGOUT" });
    };

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
                    backgroundColor: 'transparent'
                }}
            >
                <Container>
                    <Navbar.Brand as={Link} to="/" className="fw-bold fs-3 d-flex align-items-center text-white">
                        <i className="bi bi-buildings me-2"></i>
                        HOTEL BOOKING SYSTEM
                    </Navbar.Brand>

                    <Navbar.Toggle aria-controls="basic-navbar-nav" className="border-0 shadow-none text-white" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="ms-auto align-items-center">
                            <Nav.Link as={Link} to="/promos" className="text-white fw-semibold mx-lg-2">Khuyến mãi</Nav.Link>
                            <Nav.Link as={Link} to="/support" className="text-white fw-semibold mx-lg-2">Hỗ trợ</Nav.Link>
                            <Nav.Link as={Link} to="/partner" className="text-white fw-semibold mx-lg-2">Hợp tác với chúng tôi</Nav.Link>

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
                                <div className="d-flex align-items-center text-white">
                                    <img
                                        src={user.avatar}
                                        width="35"
                                        height="35"
                                        className="rounded-circle me-2 border border-white"
                                        alt="avatar"
                                        style={{ objectFit: 'cover' }}
                                    />
                                    <span className="fw-bold me-3">{user.username}</span>
                                    <Button
                                        variant="outline-light"
                                        onClick={logout}
                                        className="rounded-pill px-3 py-1 small"
                                    >
                                        Đăng xuất
                                    </Button>
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