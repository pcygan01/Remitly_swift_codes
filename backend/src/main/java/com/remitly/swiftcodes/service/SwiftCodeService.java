package com.remitly.swiftcodes.service;

import com.remitly.swiftcodes.model.CountrySwiftCodesResponse;
import com.remitly.swiftcodes.model.CreateSwiftCodeRequest;
import com.remitly.swiftcodes.model.SwiftCodeResponse;

public interface SwiftCodeService {
    
    SwiftCodeResponse getSwiftCode(String swiftCode);
    
    CountrySwiftCodesResponse getSwiftCodesByCountry(String countryISO2);
    
    String createSwiftCode(CreateSwiftCodeRequest request);

    String deleteSwiftCode(String swiftCode);

    void initializeDataFromExcel();

    String getCountryNameByISO2(String countryISO2);
} 