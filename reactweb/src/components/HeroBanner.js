import React from 'react';
import { Container } from 'react-bootstrap';
import bgImage from '../images/hotel-background.jpg';

const HeroBanner = ({ title, subtitle, children }) => {
    return (
        <div className="hero-section position-relative d-flex align-items-center"
            style={{
                backgroundImage: `url(${bgImage})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                height: '450px',
                paddingTop: '80px'
            }}
        >
            <div className="position-absolute top-0 start-0 w-100 h-100"
                style={{
                    background: 'linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.7))'
                }}
            ></div>

            <Container className="position-relative text-white text-center">
                <h1 className="display-4 fw-bold" style={{ textShadow: '2px 2px 4px rgba(0,0,0,0.5)' }}>
                    {title}
                </h1>
                {subtitle && (
                    <p className="fs-4 mb-3" style={{ textShadow: '1px 1px 2px rgba(0,0,0,0.5)' }}>
                        {subtitle}
                    </p>
                )}
                {children}
            </Container>
        </div>
    );
};

export default HeroBanner;