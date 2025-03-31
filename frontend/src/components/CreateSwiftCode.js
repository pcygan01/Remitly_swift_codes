import React, { useState } from 'react';
import { Form, Button, Card, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

const COUNTRY_CODES = {
  "PL": "POLAND",
  "MT": "MALTA",
  "US": "UNITED STATES",
  "GB": "GREAT BRITAIN",
  "DE": "GERMANY",
  "FR": "FRANCE",
  "IT": "ITALY",
  "ES": "SPAIN",
  "PT": "PORTUGAL",
  "NL": "NETHERLANDS",
  "BE": "BELGIUM",
  "LU": "LUXEMBOURG",
  "IE": "IRELAND",
  "DK": "DENMARK",
  "SE": "SWEDEN",
  "FI": "FINLAND",
  "AT": "AUSTRIA",
  "CY": "CYPRUS",
  "CZ": "CZECH REPUBLIC",
  "EE": "ESTONIA",
  "HU": "HUNGARY",
  "LV": "LATVIA",
  "LT": "LITHUANIA",
  "RO": "ROMANIA",
  "SK": "SLOVAKIA",
  "SI": "SLOVENIA",
  "BG": "BULGARIA",
  "HR": "CROATIA",
  "CH": "SWITZERLAND",
  "NO": "NORWAY",
  "IS": "ICELAND",
  "RU": "RUSSIA",
  "UA": "UKRAINE",
  "BY": "BELARUS",
  "MD": "MOLDOVA",
  "RS": "SERBIA",
  "ME": "MONTENEGRO",
  "AL": "ALBANIA",
  "MK": "NORTH MACEDONIA",
  "GR": "GREECE",
  "TR": "TURKEY",
  "AZ": "AZERBAIJAN",
  "GE": "GEORGIA",
  "AM": "ARMENIA",
  "IR": "IRAN",
  "IQ": "IRAQ",
  "SA": "SAUDI ARABIA",
  "KW": "KUWAIT",
  "AE": "UNITED ARAB EMIRATES",
  "QA": "QATAR",
  "BH": "BAHRAIN",
  "OM": "OMAN",
  "YE": "YEMEN",
  "SY": "SYRIA",
  "JO": "JORDAN",
  "LB": "LEBANON",
  "IL": "ISRAEL",
  "PS": "PALESTINE",
  "EG": "EGYPT",
  "SD": "SUDAN",
  "LY": "LIBYA",
  "TN": "TUNISIA",
  "DZ": "ALGERIA",
  "MA": "MOROCCO",
  "CA": "CANADA",
  "MX": "MEXICO",
  "BR": "BRAZIL",
  "AR": "ARGENTINA",
  "CO": "COLOMBIA",
  "PE": "PERU",
  "VE": "VENEZUELA",
  "CL": "CHILE",
  "EC": "ECUADOR",
  "BO": "BOLIVIA",
  "PY": "PARAGUAY",
  "UY": "URUGUAY",
  "CN": "CHINA",
  "JP": "JAPAN",
  "KR": "SOUTH KOREA",
  "IN": "INDIA",
  "AU": "AUSTRALIA",
  "NZ": "NEW ZEALAND",
  "ZA": "SOUTH AFRICA",
  "AD": "ANDORRA",
  "AF": "AFGHANISTAN",
  "AG": "ANTIGUA AND BARBUDA",
  "AO": "ANGOLA",
  "BA": "BOSNIA AND HERZEGOVINA",
  "BB": "BARBADOS",
  "BD": "BANGLADESH",
  "BF": "BURKINA FASO",
  "BI": "BURUNDI",
  "BJ": "BENIN",
  "BM": "BERMUDA",
  "BN": "BRUNEI",
  "BS": "BAHAMAS",
  "BT": "BHUTAN",
  "BW": "BOTSWANA",
  "BZ": "BELIZE",
  "CD": "DEMOCRATIC REPUBLIC OF THE CONGO",
  "CF": "CENTRAL AFRICAN REPUBLIC",
  "CG": "CONGO",
  "CI": "IVORY COAST",
  "CM": "CAMEROON",
  "CR": "COSTA RICA",
  "CU": "CUBA",
  "CV": "CAPE VERDE",
  "DJ": "DJIBOUTI",
  "DM": "DOMINICA",
  "DO": "DOMINICAN REPUBLIC",
  "ER": "ERITREA",
  "ET": "ETHIOPIA",
  "FJ": "FIJI",
  "FM": "MICRONESIA",
  "GA": "GABON",
  "GD": "GRENADA",
  "GH": "GHANA",
  "GM": "GAMBIA",
  "GN": "GUINEA",
  "GQ": "EQUATORIAL GUINEA",
  "GT": "GUATEMALA",
  "GW": "GUINEA-BISSAU",
  "GY": "GUYANA",
  "HK": "HONG KONG",
  "HN": "HONDURAS",
  "HT": "HAITI",
  "ID": "INDONESIA",
  "JM": "JAMAICA",
  "KE": "KENYA",
  "KG": "KYRGYZSTAN",
  "KH": "CAMBODIA",
  "KI": "KIRIBATI",
  "KM": "COMOROS",
  "KN": "SAINT KITTS AND NEVIS",
  "KP": "NORTH KOREA",
  "KZ": "KAZAKHSTAN",
  "LA": "LAOS",
  "LC": "SAINT LUCIA",
  "LI": "LIECHTENSTEIN",
  "LK": "SRI LANKA",
  "LR": "LIBERIA",
  "LS": "LESOTHO",
  "MC": "MONACO",
  "MG": "MADAGASCAR",
  "MH": "MARSHALL ISLANDS",
  "ML": "MALI",
  "MM": "MYANMAR",
  "MN": "MONGOLIA",
  "MR": "MAURITANIA",
  "MU": "MAURITIUS",
  "MV": "MALDIVES",
  "MW": "MALAWI",
  "MY": "MALAYSIA",
  "MZ": "MOZAMBIQUE",
  "NA": "NAMIBIA",
  "NE": "NIGER",
  "NG": "NIGERIA",
  "NI": "NICARAGUA",
  "NP": "NEPAL",
  "NR": "NAURU",
  "PA": "PANAMA",
  "PG": "PAPUA NEW GUINEA",
  "PH": "PHILIPPINES",
  "PK": "PAKISTAN",
  "PR": "PUERTO RICO",
  "RW": "RWANDA",
  "SB": "SOLOMON ISLANDS",
  "SC": "SEYCHELLES",
  "SG": "SINGAPORE",
  "SL": "SIERRA LEONE",
  "SM": "SAN MARINO",
  "SN": "SENEGAL",
  "SO": "SOMALIA",
  "SR": "SURINAME",
  "SS": "SOUTH SUDAN",
  "ST": "SAO TOME AND PRINCIPE",
  "SV": "EL SALVADOR",
  "SZ": "ESWATINI",
  "TD": "CHAD",
  "TG": "TOGO",
  "TH": "THAILAND",
  "TJ": "TAJIKISTAN",
  "TL": "EAST TIMOR",
  "TM": "TURKMENISTAN",
  "TO": "TONGA",
  "TT": "TRINIDAD AND TOBAGO",
  "TV": "TUVALU",
  "TW": "TAIWAN",
  "TZ": "TANZANIA",
  "UG": "UGANDA",
  "UZ": "UZBEKISTAN",
  "VA": "VATICAN CITY",
  "VC": "SAINT VINCENT AND THE GRENADINES",
  "VN": "VIETNAM",
  "VU": "VANUATU",
  "WS": "SAMOA",
  "ZM": "ZAMBIA",
  "ZW": "ZIMBABWE"
};

const CreateSwiftCode = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    swiftCode: '',
    bankName: '',
    countryISO2: '',
    countryName: '',
    address: '',
    isHeadquarter: false
  });
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState('');
  const [submitSuccess, setSubmitSuccess] = useState('');

  const handleChange = async (e) => {
    const { name, value, type, checked } = e.target;
    
    if (name === 'swiftCode' && value.length >= 6) {
      const countryCode = value.substring(4, 6).toUpperCase();
      
      try {
        setFormData(prev => ({
          ...prev,
          [name]: value,
          countryISO2: countryCode
        }));
        
        const countryInfo = await api.getCountryInfo(countryCode);
        
        setFormData(prev => ({
          ...prev,
          countryName: countryInfo.countryName
        }));
      } catch (error) {
        console.error('Error fetching country info:', error);
        setFormData(prev => ({
          ...prev,
          countryName: COUNTRY_CODES[countryCode] || ''
        }));
      }
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: type === 'checkbox' ? checked : value
      }));
    }
    
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.swiftCode) {
      newErrors.swiftCode = 'SWIFT code is required';
    } else if (!/^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$/.test(formData.swiftCode)) {
      newErrors.swiftCode = 'Invalid SWIFT code format';
    }
    
    if (!formData.bankName.trim()) {
      newErrors.bankName = 'Bank name is required';
    }
    
    if (!formData.countryISO2.trim()) {
      newErrors.countryISO2 = 'Country ISO2 code is required';
    } else if (!/^[A-Z]{2}$/.test(formData.countryISO2)) {
      newErrors.countryISO2 = 'Country ISO2 must be a valid 2-letter code';
    }
    
    if (!formData.countryName.trim()) {
      newErrors.countryName = 'Country name is required';
    }
    
    if (formData.swiftCode.length >= 6 && 
        formData.countryISO2 && 
        formData.swiftCode.substring(4, 6) !== formData.countryISO2) {
      newErrors.countryISO2 = 'Country code must match the country code in SWIFT (positions 5-6)';
    }
    
    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }
    
    const submissionData = {
      ...formData,
      countryISO2: formData.countryISO2.toUpperCase(),
      countryName: formData.countryName.toUpperCase()
    };
    
    setIsSubmitting(true);
    setSubmitError('');
    setSubmitSuccess('');
    
    try {
      const response = await api.createSwiftCode(submissionData);
      setSubmitSuccess(response.message || 'SWIFT code created successfully');
      
      setFormData({
        swiftCode: '',
        bankName: '',
        countryISO2: '',
        countryName: '',
        address: '',
        isHeadquarter: false
      });
      
      setTimeout(() => {
        navigate(`/swift-code/${submissionData.swiftCode}`);
      }, 2000);
      
    } catch (error) {
      console.error('Error creating SWIFT code:', error);
      setSubmitError(
        error.response?.data?.message || 
        'Failed to create SWIFT code. Please try again.'
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="form-container">
      <Card>
        <Card.Body>
          <div className="heading-with-button">
            <Card.Title as="h2">Create New SWIFT Code</Card.Title>
            <Button variant="secondary" onClick={() => navigate('/')}>
              Back
            </Button>
          </div>
          
          {submitError && (
            <Alert variant="danger" dismissible onClose={() => setSubmitError('')}>
              {submitError}
            </Alert>
          )}
          
          {submitSuccess && (
            <Alert variant="success" dismissible onClose={() => setSubmitSuccess('')}>
              {submitSuccess}
            </Alert>
          )}
          
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>SWIFT Code *</Form.Label>
              <Form.Control
                type="text"
                name="swiftCode"
                value={formData.swiftCode}
                onChange={handleChange}
                isInvalid={!!errors.swiftCode}
                placeholder="e.g., AAISITRAXXX"
              />
              <Form.Control.Feedback type="invalid">
                {errors.swiftCode}
              </Form.Control.Feedback>
              <Form.Text className="text-muted">
                Format: 4 letters (bank code) + 2 letters (country code) + 2 alphanumeric + optional 3 alphanumeric (branch code)
              </Form.Text>
            </Form.Group>
            
            <Form.Group className="mb-3">
              <Form.Label>Bank Name *</Form.Label>
              <Form.Control
                type="text"
                name="bankName"
                value={formData.bankName}
                onChange={handleChange}
                isInvalid={!!errors.bankName}
                placeholder="e.g., BANK OF EXAMPLE"
              />
              <Form.Control.Feedback type="invalid">
                {errors.bankName}
              </Form.Control.Feedback>
            </Form.Group>
            
            <Form.Group className="mb-3">
              <Form.Label>Country ISO2 Code *</Form.Label>
              <Form.Control
                type="text"
                name="countryISO2"
                value={formData.countryISO2}
                onChange={handleChange}
                isInvalid={!!errors.countryISO2}
                placeholder="e.g., US"
                readOnly={formData.swiftCode.length >= 6}
              />
              <Form.Control.Feedback type="invalid">
                {errors.countryISO2}
              </Form.Control.Feedback>
              <Form.Text className="text-muted">
                2-letter country code (automatically filled from SWIFT code)
              </Form.Text>
            </Form.Group>
            
            <Form.Group className="mb-3">
              <Form.Label>Country Name *</Form.Label>
              <Form.Control
                type="text"
                name="countryName"
                value={formData.countryName}
                onChange={handleChange}
                isInvalid={!!errors.countryName}
                placeholder="e.g., UNITED STATES"
                readOnly={formData.swiftCode.length >= 6 && COUNTRY_CODES[formData.countryISO2]}
              />
              <Form.Control.Feedback type="invalid">
                {errors.countryName}
              </Form.Control.Feedback>
              <Form.Text className="text-muted">
                Will be automatically filled based on country code
              </Form.Text>
            </Form.Group>
            
            <Form.Group className="mb-3">
              <Form.Label>Address</Form.Label>
              <Form.Control
                type="text"
                name="address"
                value={formData.address}
                onChange={handleChange}
                placeholder="Bank address (optional)"
              />
            </Form.Group>
            
            <Form.Group className="mb-4">
              <Form.Check
                type="checkbox"
                name="isHeadquarter"
                label="This is a bank headquarter"
                checked={formData.isHeadquarter}
                onChange={handleChange}
              />
              <Form.Text className="text-muted">
                If checked, this SWIFT code will be treated as a headquarter and can have branches associated with it
              </Form.Text>
            </Form.Group>
            
            <div className="d-grid gap-2">
              <Button 
                variant="primary" 
                type="submit" 
                disabled={isSubmitting}
              >
                {isSubmitting ? 'Creating...' : 'Create SWIFT Code'}
              </Button>
            </div>
          </Form>
        </Card.Body>
      </Card>
    </div>
  );
};

export default CreateSwiftCode; 