import React, { useCallback, useEffect, useState } from 'react';
import { useSearchParams, Link, Outlet, useNavigate } from 'react-router-dom';
import { Container, Row, Col, Card, Form, Button, Pagination } from 'react-bootstrap';
import 'bootstrap-icons/font/bootstrap-icons.css';
import Apis, { endpoints } from '../../configs/Apis';
import HeroBanner from '../../components/HeroBanner';
import cookies from 'react-cookies';

let isFirstLoad = true;

const RoomType = () => {
    console.log(cookies.load('user'))
    const [searchParams, setSearchParams] = useSearchParams();
    const navigate = useNavigate();

    const [roomTypes, setRoomTypes] = useState([]);
    const [totalPages, setTotalPages] = useState(1);

    const currentRoomTypePage = parseInt(searchParams.get("roomTypePage") || "1");
    const queryKw = searchParams.get("kw") || "";
    const queryFromPrice = searchParams.get("fromPrice") || "";
    const queryToPrice = searchParams.get("toPrice") || "";

    const [kw, setKw] = useState(queryKw);
    const [fromPrice, setFromPrice] = useState(queryFromPrice);
    const [toPrice, setToPrice] = useState(queryToPrice);

    const loadRoomTypes = useCallback(async () => {
        try {
            let url = `${endpoints['roomTypes']}?page=${currentRoomTypePage}`;
            if (queryKw) url += `&kw=${queryKw}`;
            if (queryFromPrice) url += `&fromPrice=${queryFromPrice}`;
            if (queryToPrice) url += `&toPrice=${queryToPrice}`;

            const response = await Apis.get(url);
            setRoomTypes(response.data.data);
            setTotalPages(parseInt(response.data.totalPages, 10) || 1);
        } catch (error) {
            console.error("Lỗi khi tải danh sách loại phòng:", error);
        }
    }, [currentRoomTypePage, queryKw, queryFromPrice, queryToPrice]);

    useEffect(() => {
        if (isFirstLoad) {
            isFirstLoad = false;
            
            const navEntries = window.performance.getEntriesByType("navigation");
            if (navEntries.length > 0 && navEntries[0].type === "reload") {
                navigate('/room-types', { replace: true }); 
            }
        }
    }, [navigate]);

    useEffect(() => {
        loadRoomTypes();
    }, [loadRoomTypes]);

    const handleSearch = (e) => {
        e.preventDefault();
        const currentParams = Object.fromEntries([...searchParams]);
        setSearchParams({ ...currentParams, kw, fromPrice, toPrice, roomTypePage: 1 });
    };

    const handlePageChange = (newPage) => {
        if (newPage >= 1 && newPage <= totalPages) {
            const currentParams = Object.fromEntries([...searchParams]);
            setSearchParams({ ...currentParams, roomTypePage: newPage });
        }
    };

    const renderPageItems = () => {
        let items = [];
        for (let number = 1; number <= totalPages; number++) {
            items.push(
                <Pagination.Item 
                    key={number} 
                    active={number === currentRoomTypePage}
                    onClick={() => handlePageChange(number)}
                >
                    {number}
                </Pagination.Item>
            );
        }
        return items;
    };

    const getFilteredQueryString = () => {
        const tempParams = new URLSearchParams(searchParams);
        tempParams.delete("roomTypeName");
        tempParams.delete("checkIn");
        tempParams.delete("checkOut");
        return tempParams.toString();
    };

    return (
        <div className="room-type-page">
            <HeroBanner 
                title="Tìm kiếm loại phòng"
                subtitle="Lựa chọn không gian nghỉ dưỡng hoàn hảo phù hợp với nhu cầu của bạn"
                height="450px"
                isCenter={true}
            />

            <Container style={{ marginTop: '-50px', position: 'relative', zIndex: 10 }}>
                <Card className="shadow-lg border-0 rounded-4 p-4">
                    <Form onSubmit={handleSearch}>
                        <Row className="g-3 align-items-end">
                            <Col md={4}>
                                <Form.Group>
                                    <Form.Label className="fw-bold text-muted small"><i className="bi bi-search me-2"></i>Tên / Loại phòng</Form.Label>
                                    <Form.Control type="text" placeholder="Nhập từ khóa..." value={kw} onChange={(e) => setKw(e.target.value)} />
                                </Form.Group>
                            </Col>
                            <Col md={3}>
                                <Form.Group>
                                    <Form.Label className="fw-bold text-muted small"><i className="bi bi-cash me-2"></i>Giá từ</Form.Label>
                                    <Form.Control type="number" placeholder="Từ..." value={fromPrice} onChange={(e) => setFromPrice(e.target.value)} />
                                </Form.Group>
                            </Col>
                            <Col md={3}>
                                <Form.Group>
                                    <Form.Label className="fw-bold text-muted small"><i className="bi bi-cash-stack me-2"></i>Đến giá</Form.Label>
                                    <Form.Control type="number" placeholder="Đến..." value={toPrice} onChange={(e) => setToPrice(e.target.value)} />
                                </Form.Group>
                            </Col>
                            <Col md={2}>
                                <Button type="submit" className="w-100 fw-bold py-2" style={{ backgroundColor: '#ff5e1f', border: 'none' }}>
                                    LỌC PHÒNG
                                </Button>
                            </Col>
                        </Row>
                    </Form>
                </Card>
            </Container>

            <Container className="my-5">
                <h2 className="fw-bold mb-4 text-dark text-center text-md-start">Kết quả tìm kiếm phù hợp</h2>
                {roomTypes && roomTypes.length > 0 ? (
                    <>
                        <Row className="g-4">
                            {roomTypes.map((r, index) => (
                                <Col lg={4} md={6} key={r.id}>
                                    <Card className="h-100 border-0 shadow-sm rounded-4 overflow-hidden">
                                        <div style={{ position: 'relative', height: '220px' }}>
                                            <Card.Img variant="top" src={r.image || `https://images.unsplash.com/photo-1611892440504-42a792e24d32?auto=format&fit=crop&w=600&sig=${index}`} style={{ height: '100%', objectFit: 'cover' }} alt={r.name} />
                                        </div>
                                        <Card.Body className="d-flex flex-column p-4">
                                            <Card.Title className="fw-bold fs-5 text-dark mb-1">{r.name}</Card.Title>
                                            <div className="text-muted small mb-2 d-flex align-items-center">
                                                <i className="bi bi-people-fill me-1 text-primary"></i>
                                                <span>Sức chứa tối đa: {r.capacity || 2} người lớn</span>
                                            </div>
                                            <Card.Text className="text-muted flex-grow-1 small">
                                                {r.description || "Trải nghiệm không gian lưu trú tuyệt vời với đầy đủ tiện ích sang trọng."}
                                            </Card.Text>
                                            <div className="border-top pt-3 mt-3 d-flex justify-content-between align-items-center">
                                                <span className="text-primary fw-bold fs-5">
                                                    {r.basePrice ? `${Number(r.basePrice).toLocaleString('vi-VN')}đ` : "Liên hệ"}
                                                    <span className="text-muted fw-normal" style={{ fontSize: '13px' }}> / đêm</span>
                                                </span>
                                                <Button as={Link} to={`/room-types/${r.id}/rooms?${getFilteredQueryString()}&roomTypeName=${encodeURIComponent(r.name)}&roomPrice=${r.basePrice}`} variant="outline-primary" className="fw-bold px-3">
                                                    Chọn loại phòng
                                                </Button>
                                            </div>
                                        </Card.Body>
                                    </Card>
                                </Col>
                            ))}
                        </Row>

                        {totalPages > 1 && (
                            <div className="d-flex justify-content-center mt-5">
                                <Pagination>
                                    <Pagination.Prev 
                                        disabled={currentRoomTypePage === 1} 
                                        onClick={() => handlePageChange(currentRoomTypePage - 1)} 
                                    />
                                    {renderPageItems()}
                                    <Pagination.Next 
                                        disabled={currentRoomTypePage === totalPages} 
                                        onClick={() => handlePageChange(currentRoomTypePage + 1)} 
                                    />
                                </Pagination>
                            </div>
                        )}
                    </>
                ) : (
                    <div className="text-center text-muted my-5 py-5 border rounded-4 bg-white shadow-sm">
                        <i className="bi bi-search fs-1 mb-3 text-secondary d-block"></i>
                        <h5 className="fw-bold">Không tìm thấy loại phòng nào phù hợp</h5>
                        {currentRoomTypePage > 1 && (
                            <Button variant="link" onClick={() => handlePageChange(1)}>Quay lại trang 1</Button>
                        )}
                    </div>
                )}
            </Container>
            <div className="room-container mt-4">
                <Outlet />
            </div>
        </div>
    );
};

export default RoomType;