package com.remitly.swiftcodes.service.impl;

import com.remitly.swiftcodes.exception.ResourceNotFoundException;
import com.remitly.swiftcodes.model.*;
import com.remitly.swiftcodes.repository.SwiftCodeRepository;
import com.remitly.swiftcodes.service.SwiftCodeService;
import com.remitly.swiftcodes.util.ExcelReader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SwiftCodeServiceImpl implements SwiftCodeService {

    private final SwiftCodeRepository swiftCodeRepository;
    private final ExcelReader excelReader;

    @PostConstruct
    public void init() {
        initializeDataFromExcel();
    }

    @Override
    @Transactional
    public void initializeDataFromExcel() {
        if (swiftCodeRepository.count() == 0) {
            log.info("Initializing SWIFT codes from Excel");
            List<SwiftCode> swiftCodes = excelReader.readSwiftCodesFromExcel();
            swiftCodeRepository.saveAll(swiftCodes);
            log.info("Initialized {} SWIFT codes", swiftCodes.size());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SwiftCodeResponse getSwiftCode(String swiftCode) {
        SwiftCode code = swiftCodeRepository.findById(swiftCode)
                .orElseThrow(() -> new ResourceNotFoundException("SWIFT code not found: " + swiftCode));
        
        SwiftCodeResponse response = mapToSwiftCodeResponse(code);
        
        if (Boolean.TRUE.equals(code.getIsHeadquarter())) {
            List<SwiftCode> branches = swiftCodeRepository.findBranchesByHeadquarterSwiftCode(swiftCode)
                    .stream()
                    .filter(branch -> !branch.getSwiftCode().equals(swiftCode)) 
                    .toList();
            
            List<SwiftCodeBranchDTO> branchDTOs = branches.stream()
                    .map(this::mapToSwiftCodeBranchDTO)
                    .collect(Collectors.toList());
            
            response.setBranches(branchDTOs);
        }
        
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public CountrySwiftCodesResponse getSwiftCodesByCountry(String countryISO2) {
        List<SwiftCode> swiftCodes = swiftCodeRepository.findByCountryISO2(countryISO2.toUpperCase());
        
        if (swiftCodes.isEmpty()) {
            throw new ResourceNotFoundException("No SWIFT codes found for country: " + countryISO2);
        }
        
        List<SwiftCodeBranchDTO> swiftCodeDTOs = swiftCodes.stream()
                .map(this::mapToSwiftCodeBranchDTO)
                .collect(Collectors.toList());
        
        return CountrySwiftCodesResponse.builder()
                .countryISO2(countryISO2.toUpperCase())
                .countryName(swiftCodes.get(0).getCountryName())
                .swiftCodes(swiftCodeDTOs)
                .build();
    }

    @Override
    @Transactional
    public String createSwiftCode(CreateSwiftCodeRequest request) {
        String countryISO2 = request.getCountryISO2().toUpperCase();
        String countryName = request.getCountryName().toUpperCase();
        
        SwiftCode swiftCode = SwiftCode.builder()
                .swiftCode(request.getSwiftCode())
                .bankName(request.getBankName())
                .countryISO2(countryISO2)
                .countryName(countryName)
                .address(request.getAddress())
                .isHeadquarter(request.getIsHeadquarter())
                .build();
        
        swiftCodeRepository.save(swiftCode);
        return "SWIFT code created successfully";
    }

    @Override
    @Transactional
    public String deleteSwiftCode(String swiftCode) {
        if (!swiftCodeRepository.existsById(swiftCode)) {
            throw new ResourceNotFoundException("SWIFT code not found: " + swiftCode);
        }
        
        swiftCodeRepository.deleteById(swiftCode);
        return "SWIFT code deleted successfully";
    }
    
    private SwiftCodeResponse mapToSwiftCodeResponse(SwiftCode swiftCode) {
        return SwiftCodeResponse.builder()
                .swiftCode(swiftCode.getSwiftCode())
                .bankName(swiftCode.getBankName())
                .countryISO2(swiftCode.getCountryISO2())
                .countryName(swiftCode.getCountryName())
                .isHeadquarter(swiftCode.getIsHeadquarter())
                .address(swiftCode.getAddress())
                .build();
    }
    
    private SwiftCodeBranchDTO mapToSwiftCodeBranchDTO(SwiftCode swiftCode) {
        return SwiftCodeBranchDTO.builder()
                .swiftCode(swiftCode.getSwiftCode())
                .bankName(swiftCode.getBankName())
                .countryISO2(swiftCode.getCountryISO2())
                .isHeadquarter(swiftCode.getIsHeadquarter())
                .address(swiftCode.getAddress())
                .build();
    }
} 