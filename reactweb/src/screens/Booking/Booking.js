import React, { useState, useEffect, useCallback, useMemo } from 'react';
import { Container, Row, Col, Card, Form, Button, ListGroup, Badge } from 'react-bootstrap';
import { useSearchParams, useNavigate } from 'react-router-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';
import HeroBanner from '../../components/HeroBanner';
import Apis, { authApis, endpoints } from '../../configs/Apis';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import cookies from 'react-cookies';

const Booking = () => {
    const isSubmittingRef = React.useRef(false); 
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const checkIn = searchParams.get("checkIn") || "";
    const checkOut = searchParams.get("checkOut") || "";
    const roomTypeName = searchParams.get("roomTypeName") || "Chưa xác định";
    const services = searchParams.get("services") || "";
    const rooms = searchParams.get("rooms") || "";
    const roomTypeId = searchParams.get("roomTypeId") || "";

    const [actualRooms, setActualRooms] = useState([]);
    const [selectRooms, setSelectRooms] = useState(() => {
        return rooms ? rooms.split(',').map(r => ({
            'id': r.split('_')[0],
            'roomNumber': r.split('_')[1],
            'price': Number(r.split('_')[2]) || 0,
            'roomTypeId': roomTypeId,
            'roomTypeName': roomTypeName
        })) : [];
    });

    const [customer, setCustomer] = useState({
        fullName: '',
        email: '',
        phone: ''
    });
    const [errors, setErrors] = useState({});

    const parsedServices = useMemo(() => {
        return services ? services.split(',').map(s => ({
            'id': s.split('_')[0],
            'name': s.split('_')[1],
            'price': Number(s.split('_')[2]) || 0,
            'quantity': Number(s.split('_')[3]) || 0
        })) : [];
    }, [services]);

    const totalDays = useMemo(() => {
        return checkIn && checkOut ? Math.ceil((new Date(checkOut) - new Date(checkIn)) / (1000 * 60 * 60 * 24)) : 0;
    }, [checkIn, checkOut]);

    const totalRoomPrice = useMemo(() => {
        return selectRooms.reduce((sum, r) => sum + r.price, 0) * totalDays;
    }, [selectRooms, totalDays]);

    const servicePriceTotal = useMemo(() => {
        return parsedServices.reduce((sum, s) => sum + (s.price * s.quantity), 0);
    }, [parsedServices]);

    const totalPrice = totalRoomPrice + servicePriceTotal;

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setCustomer(prev => ({ ...prev, [name]: value }));
        
        if (!!errors[name]) {
            setErrors({ ...errors, [name]: null });
        }
    };

    const validateForm = () => {
        const newErrors = {};

        if (!customer.fullName.trim()) {
            newErrors.fullName = 'Vui lòng nhập họ và tên.';
        }

        const phoneRegex = /^(0[3|5|7|8|9])+([0-9]{8})\b$/;
        if (!customer.phone.trim()) {
            newErrors.phone = 'Vui lòng nhập số điện thoại.';
        } else if (!phoneRegex.test(customer.phone)) {
            newErrors.phone = 'Số điện thoại không hợp lệ.';
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!customer.email.trim()) {
            newErrors.email = 'Vui lòng nhập email.';
        } else if (!emailRegex.test(customer.email)) {
            newErrors.email = 'Email không hợp lệ.';
        }

        return newErrors;
    };

    const loadActualRooms = async () => {
        if (isSubmittingRef.current) 
            return;
        try {
            let url = `${endpoints['availableRooms']}?roomTypeId=${roomTypeId}&expectedCheckIn=${checkIn}&expectedCheckOut=${checkOut}`;
            const response = await Apis.get(url);
            const fetchedRooms = response.data.data;
            
            setActualRooms(fetchedRooms);
            
            if (selectRooms.length > 0) {
                const currentSelectedRoomIds = selectRooms.map(r => String(r.id));
                
                const missingRooms = currentSelectedRoomIds.filter(
                    id => !fetchedRooms.some(newRoom => String(newRoom.id) === id)
                );

                if (missingRooms.length > 0) {
                    alert("⚠️ Cảnh báo: Một hoặc vài phòng bạn đang chọn vừa được khách khác đặt thành công cách đây vài giây! Hệ thống tự động gỡ phòng đó.");
                    const updatedSelectRooms = selectRooms.filter(r => !missingRooms.includes(String(r.id)));
                    setSelectRooms(updatedSelectRooms);
                    if (updatedSelectRooms.length === 0)
                        navigate(-1, { replace: true });
                }
            }
        } catch (error) {
            console.error("Lỗi đồng bộ danh sách phòng trống:", error);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formErrors = validateForm();
        if (Object.keys(formErrors).length > 0) {
            setErrors(formErrors);
            return;
        }

        if (selectRooms.length === 0) {
            alert("Bạn không còn phòng nào trong danh sách lựa chọn để đặt!");
            return;
        }

        if (isSubmittingRef.current) 
            return; 
        
        isSubmittingRef.current = true; 
        setIsSubmitting(true); 

        const finalBookingData = {
            'expectedCheckIn': checkIn,
            'expectedCheckOut': checkOut,
            'rooms': selectRooms,
            'services': parsedServices,
            'customer': customer,
            'totalPrice': totalPrice
        };

        try {
            const response = await authApis().post(endpoints["bookings"], finalBookingData);
            console.log("Đặt phòng thành công:", response.data);
            alert("Đặt phòng thành công! Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.");
            navigate('/', { replace: true });
        } catch (error) {
            console.error("Lỗi khi đặt phòng:", error);
            alert("Đã xảy ra lỗi khi đặt phòng. Vui lòng thử lại.");
            isSubmittingRef.current = false;
            setIsSubmitting(false);
        }
    };

    useEffect(() => {
        if (!roomTypeId) return;

        const stompClient = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/springserver/websocket'),
            debug: (str) => console.log('STOMP Debug:', str), 
            onConnect: () => {
                stompClient.subscribe(`/topic/room-type/${roomTypeId}`, (message) => {
                    if (message.body === "ROOM_UPDATED") {
                        if (checkIn && checkOut) {
                            loadActualRooms();
                        }
                    }
                });
            },
            onStompError: (frame) => {
                console.error('Error Broker: ' + frame.headers['message']);
            }
        });
        stompClient.activate();

        return () => {
            if (stompClient.active) {
                stompClient.deactivate();
            }
        };
    }, [roomTypeId, checkIn, checkOut, loadActualRooms]);

    useEffect(() => {
        const user = cookies.load("user");
        if (user) {
            setCustomer({
                fullName: user.name || '',
                email: user.email || '',
                phone: user.phone || '',
                address: user.address || ''
            });
        }
    }, []);

    return (
        <div className="booking-page bg-light pb-5">
            <HeroBanner 
                title="Hoàn tất đặt phòng"
                subtitle="Vui lòng điền thông tin chi tiết để chúng tôi chuẩn bị tốt nhất cho kỳ nghỉ của bạn"
                height="450px"
            />

            <Container style={{ marginTop: '-40px', position: 'relative', zIndex: 10 }}>
                <Form onSubmit={handleSubmit} noValidate>
                    <Row className="g-4">
                        <Col lg={7}>
                            <Card className="border-0 shadow-lg rounded-4 p-4 h-100">
                                <h4 className="fw-bold mb-4 text-dark">
                                    <i className="bi bi-person-lines-fill me-2 text-primary"></i>
                                    Thông tin khách hàng
                                </h4>
                                
                                <Row className="g-3">
                                    <Col md={12}>
                                        <Form.Group>
                                            <Form.Label className="fw-semibold">Họ và tên <span className="text-danger">*</span></Form.Label>
                                            <Form.Control 
                                                type="text" 
                                                placeholder="Nhập đầy đủ họ tên" 
                                                name="fullName"
                                                value={customer.fullName}
                                                onChange={handleInputChange}
                                                isInvalid={!!errors.fullName}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.fullName}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group>
                                            <Form.Label className="fw-semibold">Số điện thoại <span className="text-danger">*</span></Form.Label>
                                            <Form.Control 
                                                type="tel" 
                                                placeholder="Nhập số điện thoại liên hệ" 
                                                name="phone"
                                                value={customer.phone}
                                                onChange={handleInputChange}
                                                isInvalid={!!errors.phone}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.phone}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group>
                                            <Form.Label className="fw-semibold">Email <span className="text-danger">*</span></Form.Label>
                                            <Form.Control 
                                                type="email" 
                                                placeholder="Nhập địa chỉ email" 
                                                name="email"
                                                value={customer.email}
                                                onChange={handleInputChange}
                                                isInvalid={!!errors.email}
                                            />
                                            <Form.Control.Feedback type="invalid">
                                                {errors.email}
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                </Row>
                            </Card>
                        </Col>

                        <Col lg={5}>
                            <Card className="border-0 shadow-lg rounded-4 p-4 h-100" style={{ backgroundColor: '#f8fbff' }}>
                                <h4 className="fw-bold mb-4 text-dark">
                                    <i className="bi bi-receipt me-2 text-primary"></i>
                                    Danh sách phòng và dịch vụ đã chọn
                                </h4>

                                <ListGroup variant="flush" className="bg-transparent mb-3">
                                    <ListGroup.Item className="bg-transparent px-0 py-3 border-bottom">
                                        <div className="d-flex justify-content-between mb-2">
                                            <span className="text-muted fw-semibold">Nhận phòng:</span>
                                            <span className="fw-bold text-dark">{checkIn || "---"}</span>
                                        </div>
                                        <div className="d-flex justify-content-between">
                                            <span className="text-muted fw-semibold">Trả phòng:</span>
                                            <span className="fw-bold text-dark">{checkOut || "---"}</span>
                                        </div>
                                    </ListGroup.Item>

                                    <ListGroup.Item className="bg-transparent px-0 py-3 border-bottom">
                                        <div className="fw-bold text-dark mb-1">{roomTypeName}</div>
                                        <div className="text-muted small mb-2">
                                            Phòng đã chọn: {selectRooms.map((room) => (
                                                <Badge className="bg-primary rounded-0 me-2" key={room.id}>
                                                    {room.roomNumber}
                                                </Badge>
                                            ))}
                                        </div>
                                        <div className="d-flex justify-content-between align-items-center">
                                            <span className="text-muted">Tiền phòng ({totalDays} đêm):</span>
                                            <span className="fw-bold">{totalRoomPrice.toLocaleString('vi-VN')}đ</span>
                                        </div>
                                    </ListGroup.Item>

                                    {parsedServices.length > 0 && (
                                        <ListGroup.Item className="bg-transparent px-0 py-3 border-bottom">
                                            <div className="fw-bold text-dark mb-2">Dịch vụ đi kèm</div>
                                            {parsedServices.map(service => (
                                                <div key={service.id} className="d-flex justify-content-between align-items-center mb-1 small text-muted">
                                                    <span>{service.name} (x{service.quantity})</span>
                                                    <span>{(service.quantity * service.price).toLocaleString('vi-VN')}đ</span>
                                                </div>
                                            ))}
                                            <div className="d-flex justify-content-between align-items-center mt-2">
                                                <span className="text-muted fw-semibold">Tổng tiền dịch vụ:</span>
                                                <span className="fw-bold">{servicePriceTotal.toLocaleString('vi-VN')}đ</span>
                                            </div>
                                        </ListGroup.Item>
                                    )}

                                    <ListGroup.Item className="bg-transparent px-0 py-3">
                                        <div className="d-flex justify-content-between align-items-center">
                                            <span className="fs-5 fw-bold text-dark">TỔNG CỘNG</span>
                                            <span className="fs-4 fw-bold" style={{ color: '#ff5e1f' }}>
                                                {totalPrice.toLocaleString('vi-VN')}đ
                                            </span>
                                        </div>
                                    </ListGroup.Item>
                                </ListGroup>
                                <Button 
                                    type="submit" 
                                    size="lg" 
                                    className="w-100 fw-bold rounded-3 py-3 shadow-sm mt-auto"
                                    style={{ backgroundColor: '#ff5e1f', border: 'none' }}
                                    disabled={isSubmitting}
                                >
                                    {isSubmitting ? "ĐANG XỬ LÝ..." : "HOÀN TẤT ĐẶT PHÒNG"} 
                                </Button>
                            </Card>
                        </Col>
                    </Row>
                </Form>
            </Container>
        </div>
    );
};

export default Booking;