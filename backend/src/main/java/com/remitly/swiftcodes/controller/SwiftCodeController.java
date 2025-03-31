package com.remitly.swiftcodes.controller;

import com.remitly.swiftcodes.model.ApiResponse;
import com.remitly.swiftcodes.model.CountrySwiftCodesResponse;
import com.remitly.swiftcodes.model.CreateSwiftCodeRequest;
import com.remitly.swiftcodes.model.SwiftCodeResponse;
import com.remitly.swiftcodes.service.SwiftCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/swift-codes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SwiftCodeController {

    private final SwiftCodeService swiftCodeService;

    @GetMapping("/{swiftCode}")
    public ResponseEntity<SwiftCodeResponse> getSwiftCode(@PathVariable String swiftCode) {
        return ResponseEntity.ok(swiftCodeService.getSwiftCode(swiftCode));
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<CountrySwiftCodesResponse> getSwiftCodesByCountry(
            @PathVariable String countryISO2code) {
        return ResponseEntity.ok(swiftCodeService.getSwiftCodesByCountry(countryISO2code));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createSwiftCode(@Valid @RequestBody CreateSwiftCodeRequest request) {
        String message = swiftCodeService.createSwiftCode(request);
        return new ResponseEntity<>(new ApiResponse(message), HttpStatus.CREATED);
    }

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<ApiResponse> deleteSwiftCode(@PathVariable String swiftCode) {
        String message = swiftCodeService.deleteSwiftCode(swiftCode);
        return ResponseEntity.ok(new ApiResponse(message));
    }

    @GetMapping("/country-info/{countryISO2}")
    public ResponseEntity<Map<String, String>> getCountryInfo(@PathVariable String countryISO2) {
        String countryName = swiftCodeService.getCountryNameByISO2(countryISO2.toUpperCase());
        if (countryName == null) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("countryISO2", countryISO2.toUpperCase());
        response.put("countryName", countryName);
        
        return ResponseEntity.ok(response);
    }
} 