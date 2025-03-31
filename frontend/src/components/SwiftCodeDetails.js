import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { Card, Button, Alert, Badge, Row, Col, ListGroup } from 'react-bootstrap';
import api from '../services/api';

const SwiftCodeDetails = () => {
  const { swiftCode } = useParams();
  const navigate = useNavigate();
  const [swiftCodeData, setSwiftCodeData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchSwiftCode = async () => {
      try {
        setLoading(true);
        const data = await api.getSwiftCode(swiftCode);
        setSwiftCodeData(data);
        setError('');
      } catch (err) {
        console.error('Error fetching SWIFT code:', err);
        setError('Failed to load SWIFT code details. The code may not exist.');
      } finally {
        setLoading(false);
      }
    };

    fetchSwiftCode();
  }, [swiftCode]);

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this SWIFT code?')) {
      try {
        await api.deleteSwiftCode(swiftCode);
        navigate('/');
      } catch (err) {
        console.error('Error deleting SWIFT code:', err);
        setError('Failed to delete SWIFT code');
      }
    }
  };

  if (loading) {
    return <div className="text-center mt-5">Loading...</div>;
  }

  if (error) {
    return (
      <Alert variant="danger">
        {error}
        <div className="mt-3">
          <Button variant="primary" onClick={() => navigate('/')}>Back to Home</Button>
        </div>
      </Alert>
    );
  }

  return (
    <div>
      <div className="heading-with-button">
        <h2>SWIFT Code Details</h2>
        <div>
          <Button variant="danger" onClick={handleDelete} className="me-2">
            Delete
          </Button>
          <Button variant="secondary" onClick={() => navigate('/')}>
            Back
          </Button>
        </div>
      </div>

      <Card className="mb-4">
        <Card.Header>
          <h3>
            {swiftCodeData.swiftCode}
            {swiftCodeData.isHeadquarter && 
              <Badge bg="success" className="ms-2">Headquarter</Badge>
            }
          </h3>
        </Card.Header>
        <Card.Body>
          <Row>
            <Col md={6}>
              <ListGroup variant="flush">
                <ListGroup.Item>
                  <strong>Bank Name:</strong> {swiftCodeData.bankName}
                </ListGroup.Item>
                <ListGroup.Item>
                  <strong>Country:</strong> {swiftCodeData.countryName} ({swiftCodeData.countryISO2})
                </ListGroup.Item>
                <ListGroup.Item>
                  <strong>Address:</strong> {swiftCodeData.address || 'N/A'}
                </ListGroup.Item>
              </ListGroup>
            </Col>
            <Col md={6}>
              <ListGroup variant="flush">
                <ListGroup.Item>
                  <strong>Type:</strong> {swiftCodeData.isHeadquarter ? 'Headquarter' : 'Branch'}
                </ListGroup.Item>
                <ListGroup.Item>
                  <Link to={`/country/${swiftCodeData.countryISO2}`}>
                    View all SWIFT codes in {swiftCodeData.countryISO2}
                  </Link>
                </ListGroup.Item>
              </ListGroup>
            </Col>
          </Row>
        </Card.Body>
      </Card>

      {swiftCodeData.isHeadquarter && swiftCodeData.branches && swiftCodeData.branches.length > 0 && (
        <div className="branch-list">
          <h3>Branches ({swiftCodeData.branches.length})</h3>
          <Row>
            {swiftCodeData.branches.map((branch) => (
              <Col md={6} key={branch.swiftCode}>
                <Card className="mb-3 branch-card">
                  <Card.Body>
                    <Card.Title>
                      <Link to={`/swift-code/${branch.swiftCode}`}>
                        {branch.swiftCode}
                      </Link>
                    </Card.Title>
                    <Card.Text>
                      <strong>Bank:</strong> {branch.bankName}
                    </Card.Text>
                    <Card.Text>
                      <strong>Address:</strong> {branch.address || 'N/A'}
                    </Card.Text>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>
        </div>
      )}
    </div>
  );
};

export default SwiftCodeDetails; 