import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import 'bootstrap-icons/font/bootstrap-icons.css';
import Apis, { endpoints } from '../../configs/Apis';

const PaymentReturn = () => {
    const [searchParams] = useSearchParams();
    const [countdown, setCountdown] = useState(3);
    const [isUpdating, setIsUpdating] = useState(true);

    const responseCode = searchParams.get('vnp_ResponseCode');
    const isSuccess = responseCode === '00';

    useEffect(() => {
        const verifyPayment = async () => {
            if (isSuccess) {
                try {
                    const queryString = searchParams.toString();
                    await Apis.get(`${endpoints["paymentVerify"]}?${queryString}`);
                    console.log("Database đã được cập nhật!");
                } catch (error) {
                    console.error("Lỗi cập nhật DB:", error);
                }
            }
            setIsUpdating(false);
        };

        verifyPayment();

        const timer = setInterval(() => {
            setCountdown((prev) => {
                if (prev <= 1) {
                    clearInterval(timer);
                    window.close();
                    return 0;
                }
                return prev - 1;
            });
        }, 1000);

        return () => clearInterval(timer);
    }, [isSuccess, searchParams]);

    return (
        <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            height: '100vh',
            backgroundColor: '#f8fbff',
            fontFamily: 'sans-serif'
        }}>
            <div style={{
                textAlign: 'center',
                padding: '40px',
                borderRadius: '16px',
                backgroundColor: '#fff',
                boxShadow: '0 10px 30px rgba(0,0,0,0.1)'
            }}>
                {isUpdating ? (
                    <>
                        <i className="bi bi-hourglass-split" style={{ fontSize: '4rem', color: '#ff5e1f' }}></i>
                        <h2 style={{ color: '#ff5e1f', marginTop: '15px', fontWeight: 'bold' }}>Đang xác thực thanh toán...</h2>
                        <p className="text-muted mt-2">Vui lòng chờ trong giây lát, hệ thống đang cập nhật hóa đơn.</p>
                    </>
                ) : isSuccess ? (
                    <>
                        <i className="bi bi-check-circle-fill" style={{ fontSize: '4rem', color: '#28a745' }}></i>
                        <h2 style={{ color: '#28a745', marginTop: '15px', fontWeight: 'bold' }}>Thanh toán thành công!</h2>
                        <p className="text-muted mt-2">Cảm ơn bạn đã sử dụng dịch vụ.</p>
                    </>
                ) : (
                    <>
                        <i className="bi bi-x-circle-fill" style={{ fontSize: '4rem', color: '#dc3545' }}></i>
                        <h2 style={{ color: '#dc3545', marginTop: '15px', fontWeight: 'bold' }}>Thanh toán thất bại!</h2>
                        <p className="text-muted mt-2">Giao dịch bị hủy hoặc xảy ra lỗi.</p>
                    </>
                )}

                <hr style={{ margin: '20px 0', borderColor: '#eee' }} />

                <p style={{ color: '#6c757d', fontSize: '0.9rem', marginBottom: 0 }}>
                    Cửa sổ này sẽ tự động đóng sau <b style={{ color: '#ff5e1f', fontSize: '1.1rem' }}>{countdown}</b> giây...
                </p>
                <button
                    onClick={() => window.close()}
                    style={{
                        marginTop: '15px',
                        padding: '8px 24px',
                        border: 'none',
                        borderRadius: '20px',
                        backgroundColor: '#e9ecef',
                        color: '#495057',
                        cursor: 'pointer'
                    }}
                >
                    Đóng ngay
                </button>
            </div>
        </div>
    );
};

export default PaymentReturn;