import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/v1/swift-codes';

const api = {
  // Get a single SWIFT code
  getSwiftCode: async (swiftCode) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/${swiftCode}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching SWIFT code:', error);
      throw error;
    }
  },
  
  // Get all SWIFT codes for a country
  getSwiftCodesByCountry: async (countryISO2) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/country/${countryISO2}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching country SWIFT codes:', error);
      throw error;
    }
  },
  
  // Create a new SWIFT code
  createSwiftCode: async (swiftCodeData) => {
    try {
      const response = await axios.post(API_BASE_URL, swiftCodeData);
      return response.data;
    } catch (error) {
      console.error('Error creating SWIFT code:', error);
      throw error;
    }
  },
  
  // Delete a SWIFT code
  deleteSwiftCode: async (swiftCode) => {
    try {
      const response = await axios.delete(`${API_BASE_URL}/${swiftCode}`);
      return response.data;
    } catch (error) {
      console.error('Error deleting SWIFT code:', error);
      throw error;
    }
  }
};

export default api; 