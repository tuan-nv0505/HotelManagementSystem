import React, { useEffect, useState, useRef } from 'react';
import { Outlet, useParams, useSearchParams, Link, useLocation } from 'react-router-dom';
import { Container, Row, Col, Card, Form, Button, Badge, Pagination } from 'react-bootstrap';
import 'bootstrap-icons/font/bootstrap-icons.css';
import Apis, { endpoints } from '../../configs/Apis';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const Room = () => {
    const { id } = useParams(); 
    const [searchParams, setSearchParams] = useSearchParams();
    const location = useLocation(); 
    const contentSectionRef = useRef(null); 

    const roomTypeName = searchParams.get("roomTypeName") || `ID ${id}`;

    const currentRoomPage = parseInt(searchParams.get("roomPage") || "1", 10);
    const queryCheckIn = searchParams.get("checkIn") || "";
    const queryCheckOut = searchParams.get("checkOut") || "";
    const roomPrice = searchParams.get("roomPrice") || "0";
    
    const [rooms, setRooms] = useState([]);
    const [totalRoomPages, setTotalRoomPages] = useState(1); 
    const [selectedRooms, setSelectedRooms] = useState([]);

    const [expectedCheckIn, setExpectedCheckIn] = useState(queryCheckIn);
    const [expectedCheckOut, setExpectedCheckOut] = useState(queryCheckOut);
    const [searched, setSearched] = useState(!!(queryCheckIn && queryCheckOut));

    const isServicePage = location.pathname.includes('/services');

    useEffect(() => {
        setExpectedCheckIn(queryCheckIn);
        setExpectedCheckOut(queryCheckOut);
    }, [queryCheckIn, queryCheckOut]);

    useEffect(() => {
        setRooms([]); 
        setTotalRoomPages(1);
        setSelectedRooms([]); 
        
        if (!queryCheckIn || !queryCheckOut) {
            setSearched(false);
        }

        if (contentSectionRef.current) {
            contentSectionRef.current.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    }, [id, queryCheckIn, queryCheckOut]);

    const handleFilterRooms = async (isRealtime = false) => {
        if (!queryCheckIn || !queryCheckOut) return;
        try {
            let url = `${endpoints['availableRooms']}?roomTypeId=${id}&expectedCheckIn=${queryCheckIn}&expectedCheckOut=${queryCheckOut}&page=${currentRoomPage}`;
            const response = await Apis.get(url);
            const fetchedRooms = response.data.data;
            
            if (isRealtime) {
                setRooms(prevRooms => {
                    if (prevRooms.length > 0) {
                        const missingRoomIds = prevRooms.filter(oldRoom => !fetchedRooms.some(newRoom => newRoom.id === oldRoom.id)).map(r => r.id);

                        if (missingRoomIds.length > 0) {
                            setSelectedRooms(prevSelected => {
                                const stillAvailable = prevSelected.filter(sr => !missingRoomIds.includes(sr.id));
                                if (stillAvailable.length < prevSelected.length) {
                                    alert("Cảnh báo: (Các) phòng bạn đang chọn vừa được khách khác đặt thành công! Hệ thống đã gỡ phòng đó khỏi danh sách lựa chọn của bạn.");
                                }
                                return stillAvailable;
                            });
                        }
                    }
                    return fetchedRooms;
                });
            } else {
                setRooms(fetchedRooms);
            }

            setTotalRoomPages(parseInt(response.data.totalPages, 10) || 1);
            setSearched(true);
        } catch (error) {
            console.error("Lỗi khi tải danh sách phòng trống:", error);
        }
    };

    useEffect(() => {
        if (!id) return;

        const stompClient = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/springserver/websocket'),
            debug: (str) => console.log('STOMP Debug:', str), 
            onConnect: () => {
                stompClient.subscribe(`/topic/room-type/${id}`, (message) => {
                    if (message.body === "ROOM_UPDATED") {
                        if (searchParams.get("checkIn") && searchParams.get("checkOut")) {
                            handleFilterRooms(true);
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
    }, [id, searchParams]);

    useEffect(() => {
        if (id && queryCheckIn && queryCheckOut) {
            handleFilterRooms(false);
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

    const handleToggleRoom = (roomId, roomNumber, price) => {
        setSelectedRooms(prev => {
            const isExist = prev.find(r => r.id === roomId);
            if (isExist) {
                return prev.filter(r => r.id !== roomId); 
            } else {
                return [...prev, { id: roomId, roomNumber, price }]; 
            }
        });
    };

    const getURL = () => {
        const ids = selectedRooms.map(r => r.id).join(',');
        const nums = selectedRooms.map(r => r.roomNumber).join(',');
        const roomsParams = selectedRooms.map(r => `${r.id}_${r.roomNumber}_${roomPrice}`).join(',');

        return `/room-types/${id}/rooms/services?checkIn=${expectedCheckIn}&checkOut=${expectedCheckOut}&roomTypeName=${encodeURIComponent(roomTypeName)}&roomsParams=${encodeURIComponent(roomsParams)}`;
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
        <div className="room-detail-roomPage" style={{ position: 'relative' }}>
            <Container ref={contentSectionRef} style={{ marginTop: '-40px', position: 'relative', zIndex: 10 }} className="mb-5">
                
                {/* BỐ CỤC: BỘ CHỌN THỜI GIAN THEO HÀNG NGANG */}
                <Card className="border-0 shadow-lg rounded-4 p-4 bg-white mb-5">
                    <Form onSubmit={handleSubmit}>
                        <Row className="g-3 align-items-end">
                            <Col md={5}>
                                <Form.Group>
                                    <Form.Label className="fw-bold text-muted small">
                                        <i className="bi bi-calendar-check-fill me-2 text-primary"></i>Ngày nhận phòng
                                    </Form.Label>
                                    <Form.Control 
                                        type="date" 
                                        min={new Date().toISOString().split('T')[0]} 
                                        value={expectedCheckIn} 
                                        onChange={(e) => setExpectedCheckIn(e.target.value)} 
                                        required 
                                    />
                                </Form.Group>
                            </Col>
                            <Col md={5}>
                                <Form.Group>
                                    <Form.Label className="fw-bold text-muted small">
                                        <i className="bi bi-calendar-x-fill me-2 text-primary"></i>Ngày trả phòng
                                    </Form.Label>
                                    <Form.Control 
                                        type="date" 
                                        min={expectedCheckIn || new Date().toISOString().split('T')[0]} 
                                        value={expectedCheckOut} 
                                        onChange={(e) => setExpectedCheckOut(e.target.value)} 
                                        required 
                                    />
                                </Form.Group>
                            </Col>
                            <Col md={2}>
                                <Button type="submit" className="w-100 fw-bold py-2" style={{ backgroundColor: '#ff5e1f', border: 'none' }}>
                                    LỌC PHÒNG TRỐNG
                                </Button>
                            </Col>
                        </Row>
                    </Form>
                </Card>

                {/* BỐ CỤC DANH SÁCH PHÒNG FULL-WIDTH PHÍA DƯỚI */}
                <div className="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
                    <h4 className="fw-bold text-dark mb-0">Các phòng hiện đang khả dụng</h4>
                    <Badge bg="secondary" className="px-3 py-2 fs-6 rounded-pill">Loại phòng: {roomTypeName}</Badge>
                </div>

                {!searched ? (
                    <div className="text-center p-5 border border-dashed rounded-4 bg-white text-muted shadow-sm">
                        <i className="bi bi-arrow-left-right fs-1 mb-2 text-secondary d-block"></i>
                        <h5>Nhập thời gian lưu trú để xem số phòng trống</h5>
                        <p className="small mb-0">Hệ thống thời gian thực sẽ lọc ra các phòng chưa bị trùng lịch đặt.</p>
                    </div>
                ) : rooms && rooms.length > 0 ? (
                    <>
                        <Row className="g-4">
                            {rooms.map((room) => {
                                const isSelected = selectedRooms.some(r => r.id === room.id);
                                return (
                                    <Col lg={4} md={6} key={room.id}>
                                        <Card 
                                            className="border-0 shadow-sm rounded-4 p-4 h-100"
                                            style={{
                                                backgroundColor: isSelected ? '#f0f8ff' : '#ffffff',
                                                border: isSelected ? '1px solid #0194f3' : '1px solid transparent',
                                                transition: 'all 0.2s ease'
                                            }}
                                        >
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
                                                <Button
                                                    onClick={() => handleToggleRoom(room.id, room.roomNumber)}
                                                    size="sm" 
                                                    className="fw-bold px-4" 
                                                    variant={isSelected ? "primary" : "outline-primary"}
                                                >
                                                    {isSelected ? <><i className="bi bi-check2-circle me-1"></i> Đã chọn</> : "Chọn phòng"}
                                                </Button>
                                            </div>
                                        </Card>
                                    </Col>
                                );
                            })}
                        </Row>

                        {totalRoomPages > 1 && (
                            <div className="d-flex justify-content-center mt-5">
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
            </Container>

            {/* TỐI ƯU: WIDGET NỔI NHỎ GỌN PHÍA GÓC PHẢI - TỰ ĐỘNG ẨN KHI VÀO SERVICE */}
            {selectedRooms.length > 0 && !isServicePage && (
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
                        <div className="small fw-bold text-dark text-nowrap">
                            Đã chọn: <Badge bg="primary" className="ms-1 px-2 py-1 fs-6">{selectedRooms.length}</Badge> phòng
                        </div>
                        <Button 
                            as={Link}
                            to={getURL()} 
                            size="sm"
                            className="fw-bold px-3 py-2 rounded-3 text-nowrap" 
                            style={{ backgroundColor: '#0194f3', border: 'none' }}
                        >
                            Tiếp tục đặt phòng <i className="bi bi-arrow-right-short ms-1"></i>
                        </Button>
                    </div>
                </Card>
            )}

            <div className="service-container">
                <Outlet />
            </div>
        </div>
    );
};

export default Room;