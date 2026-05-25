import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';
import HeroBanner from '../../components/HeroBanner';

const Home = () => {
    return (
        <div className="home-page">
            <HeroBanner 
                title="Trải nghiệm lưu trú đẳng cấp"
                subtitle="Đặt phòng sang trọng với mức giá ưu đãi nhất tại Hotel Booking System"
            >
                <Button as={Link} to="/room-types" size="lg" style={{ backgroundColor: '#ff5e1f', border: 'none', padding: '12px 30px' }}>
                    <i className="bi bi-calendar-check me-2"></i>Đặt phòng ngay
                </Button>
            </HeroBanner>

            <Container className="my-5">
                <h2 className="fw-bold mb-4 text-center">Các loại phòng nổi bật</h2>
                <Row className="g-4">
                    {[1, 2, 3].map((item) => (
                        <Col lg={4} key={item}>
                            <Card className="h-100 border-0 shadow-sm rounded-4 overflow-hidden">
                                <Card.Img variant="top" src="https://images.unsplash.com/photo-1611892440504-42a792e24d32?auto=format&fit=crop&w=600" />
                                <Card.Body>
                                    <Card.Title className="fw-bold">Phòng Deluxe View Biển</Card.Title>
                                    <Card.Text className="text-muted">Trải nghiệm không gian thoáng đãng với ban công nhìn ra toàn cảnh đại dương.</Card.Text>
                                    <div className="d-flex justify-content-between align-items-center">
                                        <span className="text-primary fw-bold fs-5">1.200.000đ / đêm</span>
                                        <Button as={Link} to="/booking" variant="outline-primary">Chọn phòng</Button>
                                    </div>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
            </Container>
        </div>
    );
}

export default Home;