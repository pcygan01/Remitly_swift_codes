import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import NavigationBar from './components/NavigationBar';
import HomePage from './components/HomePage';
import SwiftCodeDetails from './components/SwiftCodeDetails';
import CountrySwiftCodes from './components/CountrySwiftCodes';
import CreateSwiftCode from './components/CreateSwiftCode';
import NotFound from './components/NotFound';

function App() {
  return (
    <>
      <NavigationBar />
      <Container>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/swift-code/:swiftCode" element={<SwiftCodeDetails />} />
          <Route path="/country/:countryCode" element={<CountrySwiftCodes />} />
          <Route path="/create" element={<CreateSwiftCode />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </Container>
    </>
  );
}

export default App; 