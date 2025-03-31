import React, { useState } from 'react';
import { Form, Button, Card, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

// Mapowanie kodów krajów na ich nazwy
const COUNTRY_CODES = {
  "PL": "Polska",
  "MT": "Malta",
  "US": "Stany Zjednoczone",
  "GB": "Wielka Brytania",
  "DE": "Niemcy",
  "FR": "Francja",
  "IT": "Włochy",
  "ES": "Hiszpania",
  "PT": "Portugalia",
  "NL": "Holandia",
  "BE": "Belgia",
  "LU": "Luksemburg",
  "IE": "Irlandia",
  "DK": "Dania",
  "SE": "Szwecja",
  "FI": "Finlandia",
  "AT": "Austria",
  "CY": "Cypr",
  "CZ": "Czechy",
  "EE": "Estonia",
  "HU": "Węgry",
  "LV": "Łotwa",
  "LT": "Litwa",
  "RO": "Rumunia",
  "SK": "Słowacja",
  "SI": "Słowenia",
  "BG": "Bułgaria",
  "HR": "Chorwacja",
  "CH": "Szwajcaria",
  "NO": "Norwegia",
  "IS": "Islandia",
  "RU": "Rosja",
  "UA": "Ukraina",
  "BY": "Białoruś",
  "MD": "Mołdawia",
  "RS": "Serbia",
  "ME": "Czarnogóra",
  "AL": "Albania",
  "MK": "Macedonia Północna",
  "GR": "Grecja",
  "TR": "Turcja",
  "AZ": "Azerbejdżan",
  "GE": "Gruzja",
  "AM": "Armenia",
  "IR": "Iran",
  "IQ": "Irak",
  "SA": "Arabia Saudyjska",
  "KW": "Kuwejt",
  "AE": "Zjednoczone Emiraty Arabskie",
  "QA": "Katar",
  "BH": "Bahrajn",
  "OM": "Oman",
  "YE": "Jemen",
  "SY": "Syria",
  "JO": "Jordania",
  "LB": "Liban",
  "IL": "Izrael",
  "PS": "Palestyna",
  "EG": "Egipt",
  "SD": "Sudan",
  "LY": "Libia",
  "TN": "Tunezja",
  "DZ": "Algieria",
  "MA": "Maroko",
  "CA": "Kanada",
  "MX": "Meksyk",
  "BR": "Brazylia",
  "AR": "Argentyna",
  "CO": "Kolumbia",
  "PE": "Peru",
  "VE": "Wenezuela",
  "CL": "Chile",
  "EC": "Ekwador",
  "BO": "Boliwia",
  "PY": "Paragwaj",
  "UY": "Urugwaj",
  "CN": "Chiny",
  "JP": "Japonia",
  "KR": "Korea Południowa",
  "IN": "Indie",
  "AU": "Australia",
  "NZ": "Nowa Zelandia",
  "ZA": "Republika Południowej Afryki",
  // Dodatkowe kraje z fix-script-countries.min.js
  "AD": "Andora",
  "AF": "Afganistan",
  "AG": "Antigua i Barbuda",
  "AO": "Angola",
  "BA": "Bośnia i Hercegowina",
  "BB": "Barbados",
  "BD": "Bangladesz",
  "BF": "Burkina Faso",
  "BI": "Burundi",
  "BJ": "Benin",
  "BM": "Bermudy",
  "BN": "Brunei",
  "BS": "Bahamy",
  "BT": "Bhutan",
  "BW": "Botswana",
  "BZ": "Belize",
  "CD": "Demokratyczna Republika Konga",
  "CF": "Republika Środkowoafrykańska",
  "CG": "Kongo",
  "CI": "Wybrzeże Kości Słoniowej",
  "CM": "Kamerun",
  "CR": "Kostaryka",
  "CU": "Kuba",
  "CV": "Republika Zielonego Przylądka",
  "DJ": "Dżibuti",
  "DM": "Dominika",
  "DO": "Dominikana",
  "ER": "Erytrea",
  "ET": "Etiopia",
  "FJ": "Fidżi",
  "FM": "Mikronezja",
  "GA": "Gabon",
  "GD": "Grenada",
  "GH": "Ghana",
  "GM": "Gambia",
  "GN": "Gwinea",
  "GQ": "Gwinea Równikowa",
  "GT": "Gwatemala",
  "GW": "Gwinea Bissau",
  "GY": "Gujana",
  "HK": "Hongkong",
  "HN": "Honduras",
  "HT": "Haiti",
  "ID": "Indonezja",
  "JM": "Jamajka",
  "KE": "Kenia",
  "KG": "Kirgistan",
  "KH": "Kambodża",
  "KI": "Kiribati",
  "KM": "Komory",
  "KN": "Saint Kitts i Nevis",
  "KP": "Korea Północna",
  "KZ": "Kazachstan",
  "LA": "Laos",
  "LC": "Saint Lucia",
  "LI": "Liechtenstein",
  "LK": "Sri Lanka",
  "LR": "Liberia",
  "LS": "Lesotho",
  "MC": "Monako",
  "MG": "Madagaskar",
  "MH": "Wyspy Marshalla",
  "ML": "Mali",
  "MM": "Mjanma",
  "MN": "Mongolia",
  "MR": "Mauretania",
  "MU": "Mauritius",
  "MV": "Malediwy",
  "MW": "Malawi",
  "MY": "Malezja",
  "MZ": "Mozambik",
  "NA": "Namibia",
  "NE": "Niger",
  "NG": "Nigeria",
  "NI": "Nikaragua",
  "NP": "Nepal",
  "NR": "Nauru",
  "PA": "Panama",
  "PG": "Papua-Nowa Gwinea",
  "PH": "Filipiny",
  "PK": "Pakistan",
  "PR": "Portoryko",
  "RW": "Rwanda",
  "SB": "Wyspy Salomona",
  "SC": "Seszele",
  "SG": "Singapur",
  "SL": "Sierra Leone",
  "SM": "San Marino",
  "SN": "Senegal",
  "SO": "Somalia",
  "SR": "Surinam",
  "SS": "Sudan Południowy",
  "ST": "Wyspy Świętego Tomasza i Książęca",
  "SV": "Salwador",
  "SZ": "Eswatini",
  "TD": "Czad",
  "TG": "Togo",
  "TH": "Tajlandia",
  "TJ": "Tadżykistan",
  "TL": "Timor Wschodni",
  "TM": "Turkmenistan",
  "TO": "Tonga",
  "TT": "Trynidad i Tobago",
  "TV": "Tuvalu",
  "TW": "Tajwan",
  "TZ": "Tanzania",
  "UG": "Uganda",
  "UZ": "Uzbekistan",
  "VA": "Watykan",
  "VC": "Saint Vincent i Grenadyny",
  "VN": "Wietnam",
  "VU": "Vanuatu",
  "WS": "Samoa",
  "ZM": "Zambia",
  "ZW": "Zimbabwe"
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

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    
    if (name === 'swiftCode' && value.length >= 6) {
      // Gdy kod SWIFT ma co najmniej 6 znaków, możemy wyciągnąć kod kraju (5-6 pozycja)
      const countryCode = value.substring(4, 6).toUpperCase();
      
      // Aktualizujemy formData z nowym kodem SWIFT oraz kodami kraju
      setFormData({
        ...formData,
        [name]: value,
        countryISO2: countryCode,
        countryName: COUNTRY_CODES[countryCode] || ''
      });
    } else {
      // Dla innych pól lub gdy SWIFT jest za krótki, aktualizujemy tylko to pole
      setFormData({
        ...formData,
        [name]: type === 'checkbox' ? checked : value
      });
    }
    
    // Clear field-specific error when user edits
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: ''
      });
    }
  };

  const validateForm = () => {
    const newErrors = {};
    
    // Validate SWIFT code (4 letter bank code + 2 letter country code + 2 alphanumeric + optional 3 branch code)
    if (!formData.swiftCode) {
      newErrors.swiftCode = 'SWIFT code is required';
    } else if (!/^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$/.test(formData.swiftCode)) {
      newErrors.swiftCode = 'Invalid SWIFT code format';
    }
    
    // Validate bank name
    if (!formData.bankName.trim()) {
      newErrors.bankName = 'Bank name is required';
    }
    
    // Validate country ISO2
    if (!formData.countryISO2.trim()) {
      newErrors.countryISO2 = 'Country ISO2 code is required';
    } else if (!/^[A-Z]{2}$/.test(formData.countryISO2)) {
      newErrors.countryISO2 = 'Country ISO2 must be a valid 2-letter code';
    }
    
    // Validate country name
    if (!formData.countryName.trim()) {
      newErrors.countryName = 'Country name is required';
    }
    
    // Validate that country code in SWIFT matches the country ISO2 field
    if (formData.swiftCode.length >= 6 && 
        formData.countryISO2 && 
        formData.swiftCode.substring(4, 6) !== formData.countryISO2) {
      newErrors.countryISO2 = 'Country code must match the country code in SWIFT (positions 5-6)';
    }
    
    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Validate form
    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }
    
    // Convert to uppercase as per requirements
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
      
      // Clear the form
      setFormData({
        swiftCode: '',
        bankName: '',
        countryISO2: '',
        countryName: '',
        address: '',
        isHeadquarter: false
      });
      
      // Redirect to the newly created SWIFT code after a delay
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