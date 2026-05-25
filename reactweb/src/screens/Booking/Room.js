import React, { useEffect, useState, useRef } from 'react';
import { useParams, useSearchParams } from 'react-router-dom';
import { Container, Row, Col, Card, Form, Button, Badge, Pagination, Spinner } from 'react-bootstrap'; // Thêm Spinner
import 'bootstrap-icons/font/bootstrap-icons.css';
import Apis, { endpoints } from '../../configs/Apis';

const Room = () => {
    const { id } = useParams(); 
    const [searchParams, setSearchParams] = useSearchParams();
    const contentSectionRef = useRef(null);

    const currentRoomPage = parseInt(searchParams.get("roomPage") || "1", 10);
    const queryCheckIn = searchParams.get("checkIn") || "";
    const queryCheckOut = searchParams.get("checkOut") || "";
    
    const [rooms, setRooms] = useState([]);
    const [totalRoomPages, setTotalRoomPages] = useState(1); 

    const [expectedCheckIn, setExpectedCheckIn] = useState(queryCheckIn);
    const [expectedCheckOut, setExpectedCheckOut] = useState(queryCheckOut);
    const [searched, setSearched] = useState(!!(queryCheckIn && queryCheckOut));

    useEffect(() => {
        setExpectedCheckIn(queryCheckIn);
        setExpectedCheckOut(queryCheckOut);
    }, [queryCheckIn, queryCheckOut]);

    useEffect(() => {
        setRooms([]); 
        setTotalRoomPages(1);
        
        if (!queryCheckIn || !queryCheckOut) {
            setSearched(false);
        }

        if (contentSectionRef.current) {
            contentSectionRef.current.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    }, [id, queryCheckIn, queryCheckOut]);

    const handleFilterRooms = async () => {
        if (!queryCheckIn || !queryCheckOut) 
            return;
        try {
            let url = `${endpoints['availableRooms']}?roomTypeId=${id}&expectedCheckIn=${queryCheckIn}&expectedCheckOut=${queryCheckOut}&page=${currentRoomPage}`;
            const response = await Apis.get(url);
            setRooms(response.data.data); 
            setTotalRoomPages(parseInt(response.data.totalPages, 10) || 1);
            setSearched(true);
        } catch (error) {
            console.error("Lỗi khi tải danh sách phòng trống:", error);
        }
    };

    useEffect(() => {
        if (id && queryCheckIn && queryCheckOut) {
            handleFilterRooms();
        }
    }, [id, queryCheckIn, queryCheckOut, currentRoomPage]);

    const handleSubmit = (e) => {
        e.preventDefault();
        const currentParams = Object.fromEntries([...searchParams]);
        setSearchParams({ ...currentParams, checkIn: expectedCheckIn, checkOut: expectedCheckOut, roomPage: 1 });
    };

    const handleRoomPageChange = (newRoomPage) => {
        if (newRoomPage >= 1 && newRoomPage <= totalRoomPages) {
            const currentParams = Object.fromEntries([...searchParams]);
            setSearchParams({ ...currentParams, roomPage: newRoomPage });
        }
    };

    const renderRoomPageItems = () => {
        let items = [];
        for (let number = 1; number <= totalRoomPages; number++) {
            items.push(
                <Pagination.Item key={number} active={number === currentRoomPage} onClick={() => handleRoomPageChange(number)}>
                    {number}
                </Pagination.Item>
            );
        }
        return items;
    };

    return (
        <div className="room-detail-roomPage">
            <Container ref={contentSectionRef} style={{ marginTop: '-40px', position: 'relative', zIndex: 10 }} className="mb-5">
                <Row className="g-4">
                    <Col lg={4}>
                        <Card className="border-0 shadow-lg rounded-4 p-4 bg-white">
                            <h5 className="fw-bold text-dark mb-4 d-flex align-items-center"><i className="bi bi-calendar-check-fill me-2 text-primary"></i>Chọn lịch lưu trú</h5>
                            <Form onSubmit={handleSubmit}>
                                <Form.Group className="mb-3">
                                    <Form.Label className="small fw-bold text-muted">Ngày nhận phòng</Form.Label>
                                    <Form.Control type="date" min={new Date().toISOString().split('T')[0]} value={expectedCheckIn} onChange={(e) => setExpectedCheckIn(e.target.value)} required />
                                </Form.Group>
                                <Form.Group className="mb-4">
                                    <Form.Label className="small fw-bold text-muted">Ngày trả phòng</Form.Label>
                                    <Form.Control type="date" min={expectedCheckIn || new Date().toISOString().split('T')[0]} value={expectedCheckOut} onChange={(e) => setExpectedCheckOut(e.target.value)} required />
                                </Form.Group>
                                <Button type="submit" className="w-100 fw-bold py-2" style={{ backgroundColor: '#ff5e1f', border: 'none' }}>
                                    LỌC PHÒNG TRỐNG
                                </Button>
                            </Form>
                        </Card>
                    </Col>

                    <Col lg={8}>
                        <div className="d-flex justify-content-between align-items-center mb-4">
                            <h4 className="fw-bold text-dark mb-0">Các phòng hiện đang khả dụng</h4>
                            <Badge bg="secondary" className="px-3 py-2 fs-6 rounded-pill">Loại phòng: {id}</Badge>
                        </div>

                        {!searched ? (
                            <div className="text-center p-5 border border-dashed rounded-4 bg-white text-muted shadow-sm">
                                <i className="bi bi-arrow-left-right fs-1 mb-2 text-secondary d-block"></i>
                                <h5>Nhập thời gian lưu trú để xem số phòng trống</h5>
                                <p className="small mb-0">Hệ thống thời gian thực sẽ lọc ra các phòng chưa bị trùng lịch đặt.</p>
                            </div>
                        ) : rooms && rooms.length > 0 ? (
                            <>
                                <Row className="g-3">
                                    {rooms.map((room) => (
                                        <Col md={6} key={room.id}>
                                            <Card className="border-0 shadow-sm rounded-4 p-4 bg-white h-100">
                                                <div className="d-flex justify-content-between align-items-start mb-2">
                                                    <div>
                                                        <h5 className="fw-bold mb-0 text-dark">Phòng {room.roomNumber || room.name || room.id}</h5>
                                                        <span className="text-muted small">Vị trí: Tầng {room.floor || "Mặc định"}</span>
                                                    </div>
                                                </div>
                                                <Card.Text className="text-muted small flex-grow-1 my-3">
                                                    {room.description || "Phòng tiện nghi tiêu chuẩn cao, đầy đủ nội thất, dịch vụ dọn phòng hàng ngày."}
                                                </Card.Text>
                                                <div className="border-top pt-3 d-flex justify-content-between align-items-center">
                                                    <Button size="sm" className="fw-bold px-3" style={{ backgroundColor: '#0194f3', border: 'none' }}>
                                                        Đặt ngay
                                                    </Button>
                                                </div>
                                            </Card>
                                        </Col>
                                    ))}
                                </Row>

                                {totalRoomPages > 1 && (
                                    <div className="d-flex justify-content-center mt-4">
                                        <Pagination>
                                            <Pagination.Prev disabled={currentRoomPage === 1} onClick={() => handleRoomPageChange(currentRoomPage - 1)} />
                                            {renderRoomPageItems()}
                                            <Pagination.Next disabled={currentRoomPage === totalRoomPages} onClick={() => handleRoomPageChange(currentRoomPage + 1)} />
                                        </Pagination>
                                    </div>
                                )}
                            </>
                        ) : (
                            <div className="text-center p-5 border border-dashed rounded-4 bg-white text-muted shadow-sm">
                                <i className="bi bi-dash-circle fs-1 text-danger mb-2 d-block"></i>
                                <h5 className="fw-bold text-dark">Rất tiếc, loại phòng này đã hết!</h5>
                                <p className="small mb-0">Không tìm thấy phòng nào trống trong khoảng thời gian bạn chọn. Xin vui lòng đổi ngày hoặc chọn loại phòng khác.</p>
                            </div>
                        )}
                    </Col>
                </Row>
            </Container>
        </div>
    );
};

export default Room;