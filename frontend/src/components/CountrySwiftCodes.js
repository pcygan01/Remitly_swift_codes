import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { Card, Button, Alert, Badge, Row, Col } from 'react-bootstrap';
import api from '../services/api';

const CountrySwiftCodes = () => {
  const { countryCode } = useParams();
  const navigate = useNavigate();
  const [countryData, setCountryData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchCountrySwiftCodes = async () => {
      try {
        setLoading(true);
        const data = await api.getSwiftCodesByCountry(countryCode);
        setCountryData(data);
        setError('');
      } catch (err) {
        console.error('Error fetching country SWIFT codes:', err);
        setError('Failed to load SWIFT codes for this country.');
      } finally {
        setLoading(false);
      }
    };

    fetchCountrySwiftCodes();
  }, [countryCode]);

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
        <h2>SWIFT Codes for {countryData.countryName} ({countryData.countryISO2})</h2>
        <Button variant="secondary" onClick={() => navigate('/')}>
          Back
        </Button>
      </div>

      <p>Total SWIFT codes: {countryData.swiftCodes.length}</p>

      <Row>
        {countryData.swiftCodes.map((swiftCode) => (
          <Col lg={6} key={swiftCode.swiftCode}>
            <Card className="mb-3 swift-code-card">
              <Card.Body>
                <Card.Title>
                  <Link to={`/swift-code/${swiftCode.swiftCode}`}>
                    {swiftCode.swiftCode}
                  </Link>
                  {swiftCode.isHeadquarter && 
                    <Badge bg="success" className="ms-2">Headquarter</Badge>
                  }
                </Card.Title>
                <Card.Text>
                  <strong>Bank:</strong> {swiftCode.bankName}
                </Card.Text>
                <Card.Text>
                  <strong>Address:</strong> {swiftCode.address || 'N/A'}
                </Card.Text>
                <Button 
                  as={Link} 
                  to={`/swift-code/${swiftCode.swiftCode}`} 
                  variant="outline-primary"
                  size="sm"
                >
                  View Details
                </Button>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default CountrySwiftCodes; 