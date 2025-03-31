import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/v1/swift-codes';

const api = {
  // Get a single SWIFT code
  getSwiftCode: async (swiftCode) => {
    try {
      const response = await axios.get(`${API_URL}/${swiftCode}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching SWIFT code:', error);
      throw error;
    }
  },
  
  // Get all SWIFT codes for a country
  getSwiftCodesByCountry: async (countryIso2) => {
    try {
      const response = await axios.get(`${API_URL}/country/${countryIso2}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching country SWIFT codes:', error);
      throw error;
    }
  },
  
  // Create a new SWIFT code
  createSwiftCode: async (swiftCodeData) => {
    try {
      const response = await axios.post(`${API_URL}`, swiftCodeData);
      return response.data;
    } catch (error) {
      console.error('Error creating SWIFT code:', error);
      throw error;
    }
  },
  
  // Delete a SWIFT code
  deleteSwiftCode: async (swiftCode) => {
    try {
      const response = await axios.delete(`${API_URL}/${swiftCode}`);
      return response.data;
    } catch (error) {
      console.error('Error deleting SWIFT code:', error);
      throw error;
    }
  },
  
  getCountryInfo: async (countryIso2) => {
    const response = await axios.get(`${API_URL}/country-info/${countryIso2}`);
    return response.data;
  }
};

export default api; 