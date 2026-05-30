import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Form, Button } from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { authApis, endpoints } from '../../configs/Apis';

const Payment = () => {
    const navigate = useNavigate();
    const location = useLocation();
    
    const bookingId = location.state?.bookingId;
    // const totalPrice = location.state?.totalPrice;

    const [paymentMethod, setPaymentMethod] = useState('VNPAY');
    const [isSubmitting, setIsSubmitting] = useState(false);

    useEffect(() => {
        if (!bookingId) {
            alert("Phiên thanh toán không hợp lệ hoặc đã hết hạn!");
            navigate('/', { replace: true });
        }
    }, [bookingId, navigate]);

    const handleGoBack = () => {
        navigate(`/booking${location.search}`, { replace: true });
    };

    const handleProcessPayment = async () => {
        setIsSubmitting(true);
        try {
            if (paymentMethod === 'VNPAY') {
                const payResponse = await authApis().get(`${endpoints["paymentCreate"]}?bookingId=${bookingId}`);
                
                if (payResponse.data && payResponse.data.url) {
                    const width = 600;
                    const height = 750;
                    const left = (window.screen.width / 2) - (width / 2);
                    const top = (window.screen.height / 2) - (height / 2);
                    
                    const paymentWindow = window.open(
                        payResponse.data.url, 
                        'VnPayPaymentWindow', 
                        `width=${width},height=${height},top=${top},left=${left},toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes`
                    );

                    const checkPopupClosed = setInterval(() => {
                        if (paymentWindow.closed) {
                            clearInterval(checkPopupClosed);
                            navigate('/', { replace: true });
                        }
                    }, 500);

                } else {
                    alert("Tính năng thanh toán online đang bị lỗi. Không tìm thấy URL.");
                    setIsSubmitting(false);
                }
            } else {
                alert("Cổng thanh toán MoMo đang được bảo trì.");
                setIsSubmitting(false);
            }
        } catch (error) {
            console.error("Lỗi thanh toán:", error);
            alert("Lỗi kết nối đến máy chủ thanh toán.");
            setIsSubmitting(false);
        }
    };

    if (!bookingId) return null;

    return (
        <Row className="mt-5 justify-content-center animation-fade-in">
            <Col lg={8} md={10}>
                <Card className="border-0 shadow-lg rounded-4 p-4 p-md-5" style={{ backgroundColor: '#f8fbff' }}>
                    <h4 className="fw-bold mb-3 text-dark text-center">
                        <i className="bi bi-wallet2 me-2 text-primary"></i>
                        Phương thức thanh toán
                    </h4>
                    <p className="text-center text-muted small mb-4">
                        Vui lòng chọn hình thức thanh toán để hoàn tất hóa đơn <b>#{bookingId}</b>
                    </p>

                    <Row className="g-4 justify-content-center mb-4">
                        <Col xs={6} md={5}>
                            <label className={`w-100 h-100 border rounded-4 p-4 d-flex flex-column align-items-center justify-content-center text-center ${paymentMethod === 'VNPAY' ? 'border-primary bg-white shadow-sm' : 'bg-transparent'}`} style={{ cursor: 'pointer', transition: 'all 0.2s' }}>
                                <Form.Check type="radio" name="payMethod" checked={paymentMethod === 'VNPAY'} onChange={() => setPaymentMethod('VNPAY')} className="mb-3" />
                                <img src="https://vinadesign.vn/uploads/images/2023/05/vnpay-logo-vinadesign-25-12-57-55.jpg" width="70" className="rounded mb-2" alt="VNPay" />
                                <div className="fw-bold text-dark">Qua VNPAY</div>
                            </label>
                        </Col>
                        <Col xs={6} md={5}>
                            <label className={`w-100 h-100 border rounded-4 p-4 d-flex flex-column align-items-center justify-content-center text-center ${paymentMethod === 'MOMO' ? 'border-primary bg-white shadow-sm' : 'bg-transparent'}`} style={{ cursor: 'pointer', transition: 'all 0.2s' }}>
                                <Form.Check type="radio" name="payMethod" checked={paymentMethod === 'MOMO'} onChange={() => setPaymentMethod('MOMO')} className="mb-3" />
                                <img src="https://cdn.haitrieu.com/wp-content/uploads/2022/10/Logo-MoMo-Square.png" width="70" className="rounded mb-2" alt="MoMo" />
                                <div className="fw-bold text-dark">Ví MoMo</div>
                            </label>
                        </Col>
                    </Row>

                    <div className="d-flex flex-column flex-md-row justify-content-between align-items-center mt-4 pt-3 border-top">
                        <Button
                            variant="outline-secondary"
                            onClick={handleGoBack}
                            disabled={isSubmitting}
                            className="fw-bold px-4 py-3 rounded-pill mb-3 mb-md-0 w-100 w-md-auto"
                        >
                            <i className="bi bi-arrow-left me-2"></i> Quay lại sửa thông tin
                        </Button>

                        <Button
                            onClick={handleProcessPayment}
                            disabled={isSubmitting}
                            className="fw-bold px-5 py-3 rounded-pill shadow-sm w-100 w-md-auto"
                            style={{ backgroundColor: '#ff5e1f', border: 'none' }}
                        >
                            {isSubmitting ? "ĐANG XỬ LÝ..." : "TIẾN HÀNH THANH TOÁN"}
                        </Button>
                    </div>
                </Card>
            </Col>
        </Row>
    );
};

export default Payment;