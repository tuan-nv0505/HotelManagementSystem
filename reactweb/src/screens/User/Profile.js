import React, { useState, useContext, useEffect, useRef } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert } from 'react-bootstrap';
import { MyUserContext } from "../../configs/Contexts";
import { useNavigate, useLocation } from 'react-router-dom';
import { authApis, endpoints } from '../../configs/Apis';

const Profile = () => {
    const [user, dispatch] = useContext(MyUserContext);
    const [isEditing, setIsEditing] = useState(false);
    const [showAlert, setShowAlert] = useState(false);
    const [loading, setLoading] = useState(false);
    const [errors, setErrors] = useState({});

    const [avatar, setAvatar] = useState(null);
    const [preview, setPreview] = useState(null);
    const avatarRef = useRef();

    const [formData, setFormData] = useState({ 
        email: '', 
        phone: '', 
        password: '', 
        confirmPassword: '' 
    });

const fields = [
    { name: 'email', label: 'Email', type: 'email' },
    { name: 'phone', label: 'Số điện thoại', type: 'text' },
    { name: 'password', label: 'Mật khẩu mới (để trống nếu không đổi)', type: 'password' },
    { name: 'confirmPassword', label: 'Xác nhận mật khẩu', type: 'password' }
];

    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        if (location.state?.showUpdateAlert) {
            setIsEditing(true);
            setShowAlert(true);
        }
    }, [location.state]);

    useEffect(() => {
        if (user === null) {
            navigate('/?login=true');
        } else if (user) {
            setFormData({
                email: user.email || '',
                phone: user.phone === '0000000000' ? '' : user.phone,
                password: '',
                confirmPassword: ''
            });
            setPreview(user.avatar);
        }
    }, [user, navigate]);

    const validate = () => {
        let newErrors = {};
        const phoneRegex = /^0\d{9}$/;
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailRegex.test(formData.email)) newErrors.email = "Email không đúng định dạng!";
        if (!phoneRegex.test(formData.phone)) newErrors.phone = "SĐT phải bắt đầu bằng 0 và đủ 10 số!";
        
        if (formData.password && (formData.password.length < 3 || formData.password.length > 50)) {
            newErrors.password = "Mật khẩu từ 3 đến 50 ký tự!";
        }
        if (formData.password !== formData.confirmPassword) {
            newErrors.confirmPassword = "Mật khẩu xác nhận không khớp!";
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleUpdate = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setLoading(true);
    let form = new FormData();
    form.append("username", user.username);
    form.append("email", formData.email);
    form.append("phone", formData.phone);
    if (formData.password) form.append("password", formData.password);
    if (avatar) form.append("file", avatar);

    try {
        await authApis().post(endpoints['update-profile'], form);

        let profileRes = await authApis().get(endpoints['profile']);

        dispatch({ "type": "LOGIN", "payload": profileRes.data });
        setIsEditing(false); 
        alert("Cập nhật thành công!");
    } catch (ex) {
        console.error(ex);
        alert("Lỗi cập nhật!");
    } finally {
        setLoading(false);
    }
};

    if (!user) return null;

    return (
        <Container className="py-5 mt-4">
            <Row className="justify-content-center pt-2">
                <Col md={6}>
                    {showAlert && <Alert variant="warning" onClose={() => setShowAlert(false)} dismissible>Vui lòng cập nhật thông tin!</Alert>}
                    <Card className="shadow-lg border-0 rounded-4 p-4">
                        <div className="text-center mb-4">
                            <img src={preview} className="rounded-circle shadow-sm border" width="120" height="120" alt="Avatar" style={{ objectFit: 'cover' }} />
                            {isEditing && (
                                <div className="mt-2">
                                    <Button variant="outline-primary" size="sm" onClick={() => avatarRef.current.click()}>Đổi ảnh</Button>
                                    <input type="file" ref={avatarRef} hidden onChange={(e) => {setAvatar(e.target.files[0]); setPreview(URL.createObjectURL(e.target.files[0]))}} accept="image/*" />
                                </div>
                            )}
                            <h3 className="mt-3 text-dark fw-bold">{user.username}</h3>
                        </div>

                        {!isEditing ? (
                            <div className="px-4 text-center">
                                <div className="mb-3 border-bottom pb-2">
                                    <small className="text-muted d-block">Email</small>
                                    <strong>{user.email}</strong>
                                </div>
                                <div className="mb-4">
                                    <small className="text-muted d-block">Số điện thoại</small>
                                    <strong>{user.phone === '0000000000' ? 'Chưa cập nhật' : user.phone}</strong>
                                </div>
                                <Button className="w-100 rounded-pill py-2" onClick={() => setIsEditing(true)}>Chỉnh sửa thông tin</Button>
                            </div>
                        ) : (
                            <Form onSubmit={handleUpdate} className="px-3">
                                {fields.map((field) => (
                                    <Form.Group className={field.name === 'confirmPassword' ? "mb-4" : "mb-3"} key={field.name}>
                                        <Form.Label className="fw-bold small">{field.label}</Form.Label>
                                        <Form.Control 
                                            type={field.type} 
                                            value={formData[field.name]} 
                                            onChange={(e) => setFormData({...formData, [field.name]: e.target.value})} 
                                            isInvalid={!!errors[field.name]} 
                                        />
                                        <Form.Control.Feedback type="invalid">
                                            {errors[field.name]}
                                        </Form.Control.Feedback>
                                    </Form.Group>
                                ))}
                                <div className="d-flex gap-2">
                                    <Button type="submit" className="w-100 rounded-pill py-2" disabled={loading}>{loading ? "Đang xử lý..." : "Lưu thay đổi"}</Button>
                                    <Button variant="outline-secondary" className="w-100 rounded-pill" onClick={() => setIsEditing(false)}>Hủy</Button>
                                </div>
                            </Form>
                        )}
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default Profile;