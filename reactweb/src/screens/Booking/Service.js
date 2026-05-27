import React, { useEffect, useRef, useState } from 'react';
import { useSearchParams, useNavigate, useParams, Outlet } from 'react-router-dom';
import { Container, Row, Col, Card, Form, Button, Pagination, InputGroup, Badge } from 'react-bootstrap';
import 'bootstrap-icons/font/bootstrap-icons.css';
import Apis, { endpoints } from '../../configs/Apis';
import LoginModal from '../User/LoginModal';
import cookies from 'react-cookies';

let isFirstLoad = true;

const Service = () => {
    const user = cookies.load('user') || null;
    const { id: roomTypeId } = useParams();
    const [searchParams, setSearchParams] = useSearchParams();
    const navigate = useNavigate();

    const queryCheckIn = searchParams.get("checkIn") || "";
    const queryCheckOut = searchParams.get("checkOut") || "";
    const roomTypeName = searchParams.get("roomTypeName") || `Loại phòng ID ${roomTypeId}`;
    const roomParams = searchParams.get("roomsParams") || ""; // "id_roomNumber_price,id_roomNumber_price"

    const [services, setServices] = useState([]);
    const [totalPages, setTotalPages] = useState(1);

    const currentServicePage = parseInt(searchParams.get("servicePage") || "1");
    const queryKw = searchParams.get("kw") || "";
    const queryFromPrice = searchParams.get("fromPrice") || "";
    const queryToPrice = searchParams.get("toPrice") || "";

    const [selectedServices, setSelectedServices] = useState([]);
    
    const [showLoginModal, setShowLoginModal] = useState(false);

    const loadServices = async () => {
        try {
            let url = `${endpoints['services']}?page=${currentServicePage}`;
            if (queryKw) url += `&kw=${queryKw}`;
            if (queryFromPrice) url += `&fromPrice=${queryFromPrice}`;
            if (queryToPrice) url += `&toPrice=${queryToPrice}`;

            const response = await Apis.get(url);
            setServices(response.data.data);
            setTotalPages(parseInt(response.data.totalPages, 10) || 1);
        } catch (error) {
            console.error("Lỗi khi tải danh sách dịch vụ:", error);
        }
    };

    useEffect(() => {
        if (isFirstLoad) {
            isFirstLoad = false;
            const navEntries = window.performance.getEntriesByType("navigation");
            if (navEntries.length > 0 && navEntries[0].type === "reload") {
                navigate(`/room-types/${roomTypeId}/services`, { replace: true }); 
            }
        }
    }, []);

    useEffect(() => {
        loadServices();
    }, [currentServicePage, queryKw, queryFromPrice, queryToPrice]);

    const handlePageChange = (newPage) => {
        if (newPage >= 1 && newPage <= totalPages) {
            const currentParams = Object.fromEntries([...searchParams]);
            setSearchParams({ ...currentParams, servicePage: newPage });
        }
    };

    const handleQuantityChange = (service, change) => {
        setSelectedServices(prev => {
            const existing = prev.find(s => s.id === service.id);

            if (existing) {
                const newQuantity = existing.quantity + change;
                if (newQuantity <= 0) {
                    return prev.filter(s => s.id !== service.id);
                }
                return prev.map(s => s.id === service.id ? { ...s, quantity: newQuantity } : s);
            } else if (change > 0) {
                return [...prev, { id: service.id, name: service.name, price: service.price, quantity: 1 }];
            }
            return prev;
        });
    };

const handleProceedToPayment = () => {
    const selectedServiceStrings = selectedServices.map(service => {
        return `${service.id}_${service.name}_${service.price}_${service.quantity}`;
    });
    const servicesParam = selectedServiceStrings.join(',');
    const bookingUrl = `/booking?checkIn=${queryCheckIn}&checkOut=${queryCheckOut}&roomTypeName=${encodeURIComponent(roomTypeName)}&rooms=${encodeURIComponent(roomParams)}&services=${encodeURIComponent(servicesParam)}&roomTypeId=${roomTypeId}`;

    if (!user || Object.keys(user).length === 0) {
        setSearchParams(prev => ({ ...prev, next: bookingUrl }));
        setShowLoginModal(true);
        return;
    }

    navigate(bookingUrl);
};

    const renderPageItems = () => {
        let items = [];
        for (let number = 1; number <= totalPages; number++) {
            items.push(
                <Pagination.Item 
                    key={number} 
                    active={number === currentServicePage}
                    onClick={() => handlePageChange(number)}
                >
                    {number}
                </Pagination.Item>
            );
        }
        return items;
    };

    const totalItemsCount = selectedServices.reduce((sum, s) => sum + s.quantity, 0);

    return (
        <div className="service-page" style={{ paddingBottom: '30px' }}>
            <Container className="my-5">
                <h2 className="fw-bold mb-4 text-dark text-center text-md-start">Danh sách dịch vụ khả dụng</h2>
                {services && services.length > 0 ? (
                    <>
                        <Row className="g-4">
                            {services.map((s, index) => {
                                const selectedService = selectedServices.find(item => item.id === s.id);
                                const quantity = selectedService ? selectedService.quantity : 0;

                                return (
                                    <Col lg={4} md={6} key={s.id}>
                                        <Card 
                                            className="h-100 border-0 shadow-sm rounded-4 overflow-hidden"
                                            style={{
                                                backgroundColor: quantity > 0 ? '#f0f8ff' : '#ffffff',
                                                border: quantity > 0 ? '1px solid #0194f3' : '1px solid transparent',
                                                transition: 'all 0.2s ease'
                                            }}
                                        >
                                            <div style={{ position: 'relative', height: '220px' }}>
                                                <Card.Img variant="top" src={s.image || `https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=600&sig=${index}`} style={{ height: '100%', objectFit: 'cover' }} alt={s.name} />
                                            </div>
                                            <Card.Body className="d-flex flex-column p-4">
                                                <Card.Title className="fw-bold fs-5 text-dark mb-1">{s.name}</Card.Title>
                                                <div className="text-muted small mb-2 d-flex align-items-center">
                                                    <i className="bi bi-tag-fill me-1 text-primary"></i>
                                                    <span>Loại: {s.category || "Dịch vụ khách sạn"}</span>
                                                </div>
                                                <Card.Text className="text-muted flex-grow-1 small">
                                                    {s.description || "Dịch vụ chất lượng cao mang tới sự thoải mái và trải nghiệm tiện nghi tối đa cho khách lưu trú."}
                                                </Card.Text>
                                                
                                                <div className="border-top pt-3 mt-3 d-flex justify-content-between align-items-center">
                                                    <span className="text-primary fw-bold fs-5">
                                                        {s.price ? `${Number(s.price).toLocaleString('vi-VN')}đ` : "Miễn phí"}
                                                        <span className="text-muted fw-normal" style={{ fontSize: '13px' }}> / lượt</span>
                                                    </span>

                                                    {quantity === 0 ? (
                                                        <Button
                                                            onClick={() => handleQuantityChange(s, 1)}
                                                            size="sm" 
                                                            className="fw-bold px-3 rounded-pill" 
                                                            variant="outline-primary"
                                                        >
                                                            Thêm dịch vụ
                                                        </Button>
                                                    ) : (
                                                        <InputGroup style={{ width: '110px' }}>
                                                            <Button 
                                                                variant="outline-primary" 
                                                                size="sm"
                                                                onClick={() => handleQuantityChange(s, -1)}
                                                                className="rounded-circle p-0 d-flex align-items-center justify-content-center"
                                                                style={{ width: '28px', height: '28px' }}
                                                            >
                                                                <i className="bi bi-dash"></i>
                                                            </Button>
                                                            <Form.Control 
                                                                className="text-center border-0 bg-transparent fw-bold text-dark p-0"
                                                                value={quantity}
                                                                readOnly 
                                                                style={{ fontSize: '14px' }}
                                                            />
                                                            <Button 
                                                                variant="primary" 
                                                                size="sm"
                                                                onClick={() => handleQuantityChange(s, 1)}
                                                                className="rounded-circle p-0 d-flex align-items-center justify-content-center"
                                                                style={{ width: '28px', height: '28px', backgroundColor: '#0194f3', border: 'none' }}
                                                            >
                                                                <i className="bi bi-plus text-white"></i>
                                                            </Button>
                                                        </InputGroup>
                                                    )}
                                                </div>
                                            </Card.Body>
                                        </Card>
                                    </Col>
                                );
                            })}
                        </Row>

                        {totalPages > 1 && (
                            <div className="d-flex justify-content-center mt-5">
                                <Pagination>
                                    <Pagination.Prev disabled={currentServicePage === 1} onClick={() => handlePageChange(currentServicePage - 1)} />
                                    {renderPageItems()}
                                    <Pagination.Next disabled={currentServicePage === totalPages} onClick={() => handlePageChange(currentServicePage + 1)} />
                                </Pagination>
                            </div>
                        )}
                    </>
                ) : (
                    <div className="text-center text-muted my-5 py-5 border rounded-4 bg-white shadow-sm">
                        <i className="bi bi-search fs-1 mb-3 text-secondary d-block"></i>
                        <h5 className="fw-bold">Không tìm thấy dịch vụ nào phù hợp</h5>
                    </div>
                )}
            </Container>

            <Card 
                className="position-fixed shadow-lg border-0 rounded-4 p-3 d-flex align-items-center justify-content-center border"
                style={{ 
                    bottom: '24px', 
                    right: '24px', 
                    zIndex: 1050, 
                    backgroundColor: 'rgba(255, 255, 255, 0.95)',
                    backdropFilter: 'blur(6px)',
                    width: 'auto'
                }}
            >
                <div className="d-flex align-items-center gap-3">
                    {totalItemsCount > 0 && (
                        <div className="small fw-bold text-dark text-nowrap">
                            Dịch vụ: <Badge bg="warning" className="ms-1 px-2 py-1 fs-6 text-dark">{totalItemsCount}</Badge>
                        </div>
                    )}
                    <Button 
                        onClick={handleProceedToPayment} 
                        size="sm"
                        className="fw-bold px-4 py-2 rounded-3 text-nowrap text-white" 
                        style={{ backgroundColor: '#ff5e1f', border: 'none' }}
                    >
                        {totalItemsCount > 0 ? "Tiếp tục thanh toán" : "Bỏ qua & Tiếp tục"} <i className="bi bi-arrow-right-short ms-1"></i>
                    </Button>
                </div>
            </Card>

            <LoginModal 
                show={showLoginModal} 
                handleClose={() => setShowLoginModal(false)} 
            />

            <div className="service-detail-container mt-4">
                <Outlet />
            </div>
        </div>
    );
};

export default Service;