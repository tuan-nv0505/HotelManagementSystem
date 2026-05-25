import React, { useState } from 'react';

export const SharedModalStyle = () => (
    <style>
        {`
            .traveloka-modal { border-radius: 24px !important; border: none !important; overflow: hidden; }
            .modal.show .modal-dialog { transform: none !important; animation: zoomIn 0.3s ease-out forwards; }
            @keyframes zoomIn { from { opacity: 0; transform: scale(0.8); } to { opacity: 1; transform: scale(1); } }
            .btn-close:focus { box-shadow: none !important; }
        `}
    </style>
);

export const ModalCloseButton = ({ onClick }) => (
    <div className="position-absolute top-0 end-0 p-3" style={{ zIndex: 1070 }}>
        <button type="button" className="btn-close" aria-label="Close" onClick={onClick}></button>
    </div>
);

export const AuthSwitchFooter = ({ text, linkText, onClick }) => (
    <div className="text-center mt-4 pt-2 mb-2">
        <span className="text-muted small">{text} </span>
        <a href="#!" className="fw-bold text-primary text-decoration-none small" onClick={(e) => {
            e.preventDefault();
            onClick();
        }}>
            {linkText}
        </a>
    </div>
);

export const SocialButton = ({ onClick, iconSrc, label }) => {
    const [isHovered, setIsHovered] = useState(false);

    const style = {
        width: '100%',
        height: '40px',
        borderRadius: '20px',
        backgroundColor: isHovered ? '#f8f9fa' : '#ffffff',
        color: '#3c4043',
        border: '1px solid #dadce0',
        fontWeight: '500',
        fontSize: '14px',
        fontFamily: '"Google Sans", Roboto, Arial, sans-serif',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        gap: '10px',
        cursor: 'pointer',
        boxShadow: '0 1px 2px 0 rgba(60,64,67,0.3)',
        transition: 'background-color 0.2s',
    };

    return (
        <button type="button" onClick={onClick} style={style} onMouseEnter={() => setIsHovered(true)} onMouseLeave={() => setIsHovered(false)}>
            <img src={iconSrc} alt={label} style={{ width: '20px', height: '20px' }} />
            <span>{label}</span>
        </button>
    );
};