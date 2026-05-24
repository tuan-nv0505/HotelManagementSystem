import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';
import bgImage from '../images/hotel-background.jpg';

const Home = () => {
    return (
        <div className="home-page">
            <div className="hero-section position-relative d-flex align-items-center"
                style={{
                    backgroundImage: `url(${bgImage})`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    height: '500px'
                }}
            >
                <div className="position-absolute top-0 start-0 w-100 h-100"
                    style={{
                        background: 'linear-gradient(rgba(0, 0, 0, 0.3), rgba(0, 0, 0, 0.6))'
                    }}
                ></div>

                <Container className="position-relative text-white">
                    <h1 className="display-4 fw-bold" style={{ textShadow: '2px 2px 4px rgba(0,0,0,0.5)' }}>
                        Trải nghiệm lưu trú đẳng cấp
                    </h1>
                    <p className="fs-4 mb-4" style={{ textShadow: '1px 1px 2px rgba(0,0,0,0.5)' }}>
                        Đặt phòng sang trọng với mức giá ưu đãi nhất tại Hotel Booking System
                    </p>
                    <Button as={Link} to="/booking" size="lg" style={{ backgroundColor: '#ff5e1f', border: 'none', padding: '12px 30px' }}>
                        <i className="bi bi-calendar-check me-2"></i>Đặt phòng ngay
                    </Button>
                </Container>
            </div>


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