import React from 'react';
import { Alert, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const NotFound = () => {
  return (
    <Alert variant="warning">
      <Alert.Heading>Page Not Found</Alert.Heading>
      <p>
        Sorry, the page you are looking for does not exist.
      </p>
      <hr />
      <div className="d-flex justify-content-end">
        <Button as={Link} to="/" variant="outline-warning">
          Go to Home Page
        </Button>
      </div>
    </Alert>
  );
};

export default NotFound; 