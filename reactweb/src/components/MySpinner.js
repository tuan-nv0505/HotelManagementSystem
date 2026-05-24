import { Spinner } from "react-bootstrap";

const MySpinner = () => {
    return (
        <Spinner
            as="span"
            animation="border"
            size="sm"
            role="status"
            aria-hidden="true"
            className="me-2"
        />
    );
}

export default MySpinner;