import React, { useState } from 'react';
import { Form, Button, InputGroup, Card, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const HomePage = () => {
  const [searchType, setSearchType] = useState('swiftCode');
  const [searchValue, setSearchValue] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSearch = (e) => {
    e.preventDefault();
    
    if (!searchValue.trim()) {
      setError('Please enter a search value');
      return;
    }

    setError('');
    
    if (searchType === 'swiftCode') {
      navigate(`/swift-code/${searchValue.trim()}`);
    } else {
      if (searchValue.trim().length !== 2) {
        setError('Country code must be 2 characters (ISO-2 format)');
        return;
      }
      navigate(`/country/${searchValue.trim().toUpperCase()}`);
    }
  };

  return (
    <div>
      <Card className="mb-4 p-4">
        <Card.Body>
          <Card.Title as="h2" className="mb-4">SWIFT Code Lookup</Card.Title>
          
          {error && <Alert variant="danger">{error}</Alert>}
          
          <Form onSubmit={handleSearch}>
            <Form.Group className="mb-3">
              <Form.Label>Search Type</Form.Label>
              <Form.Select 
                value={searchType}
                onChange={(e) => setSearchType(e.target.value)}
              >
                <option value="swiftCode">By SWIFT Code</option>
                <option value="country">By Country Code (ISO-2)</option>
              </Form.Select>
            </Form.Group>
            
            <Form.Group className="mb-3">
              <Form.Label>
                {searchType === 'swiftCode' ? 'Enter SWIFT Code' : 'Enter Country Code (ISO-2)'}
              </Form.Label>
              <InputGroup>
                <Form.Control
                  type="text"
                  placeholder={searchType === 'swiftCode' ? 'e.g., AAISITRAXXX' : 'e.g., US'}
                  value={searchValue}
                  onChange={(e) => setSearchValue(e.target.value)}
                />
                <Button variant="primary" type="submit">
                  Search
                </Button>
              </InputGroup>
            </Form.Group>
          </Form>
        </Card.Body>
      </Card>
      
      <Card>
        <Card.Body>
          <Card.Title as="h3">About SWIFT Codes</Card.Title>
          <Card.Text>
            A SWIFT code (also known as BIC - Bank Identifier Code) is a unique identification code for banks and financial institutions globally. 
            It is used for international wire transfers and helps ensure that money is sent to the correct bank.
          </Card.Text>
          <Card.Text>
            Our database contains comprehensive SWIFT code information for banks worldwide.
            Simply search by the SWIFT code or country to find the details you need.
          </Card.Text>
        </Card.Body>
      </Card>
    </div>
  );
};

export default HomePage; 