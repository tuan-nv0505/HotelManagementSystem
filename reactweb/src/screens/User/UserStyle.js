import React from 'react';

export const SharedModalStyle = () => (
    <style>
        {`
            .traveloka-modal { border-radius: 24px !important; border: none !important; overflow: hidden; }
            .modal.show .modal-dialog { transform: none !important; animation: zoomIn 0.3s ease-out forwards; }
            @keyframes zoomIn { from { opacity: 0; transform: scale(0.8); } to { opacity: 1; transform: scale(1); } }
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