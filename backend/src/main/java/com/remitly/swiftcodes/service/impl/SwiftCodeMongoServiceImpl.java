package com.remitly.swiftcodes.service.impl;

import com.remitly.swiftcodes.exception.ResourceNotFoundException;
import com.remitly.swiftcodes.model.*;
import com.remitly.swiftcodes.repository.SwiftCodeMongoRepository;
import com.remitly.swiftcodes.service.SwiftCodeService;
import com.remitly.swiftcodes.util.ExcelReader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
@Qualifier("mongoService")
public class SwiftCodeMongoServiceImpl implements SwiftCodeService {

    private final SwiftCodeMongoRepository swiftCodeRepository;
    private final ExcelReader excelReader;

    @PostConstruct
    public void init() {
        initializeDataFromExcel();
    }

    @Override
    public void initializeDataFromExcel() {
        if (swiftCodeRepository.count() == 0) {
            log.info("Initializing SWIFT codes from Excel to MongoDB");
            List<SwiftCode> swiftCodes = excelReader.readSwiftCodesFromExcel();
            
            List<SwiftCodeMongo> mongoSwiftCodes = swiftCodes.stream()
                    .map(this::convertToMongoDocument)
                    .collect(Collectors.toList());
            
            swiftCodeRepository.saveAll(mongoSwiftCodes);
            log.info("Initialized {} SWIFT codes in MongoDB", mongoSwiftCodes.size());
        }
    }

    @Override
    public SwiftCodeResponse getSwiftCode(String swiftCode) {
        SwiftCodeMongo code = swiftCodeRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new ResourceNotFoundException("SWIFT code not found: " + swiftCode));
        
        SwiftCodeResponse response = mapToSwiftCodeResponse(code);
        
        if (Boolean.TRUE.equals(code.getIsHeadquarter())) {
            String swiftCodePrefix = swiftCode.substring(0, 8);
            
            List<SwiftCodeMongo> branches = swiftCodeRepository.findBranchesByHeadquarterSwiftCodePrefix(swiftCodePrefix)
                    .stream()
                    .filter(branch -> !branch.getSwiftCode().equals(swiftCode)) // Exclude the headquarter itself
                    .toList();
            
            List<SwiftCodeBranchDTO> branchDTOs = branches.stream()
                    .map(this::mapToSwiftCodeBranchDTO)
                    .collect(Collectors.toList());
            
            response.setBranches(branchDTOs);
        }
        
        return response;
    }

    @Override
    public CountrySwiftCodesResponse getSwiftCodesByCountry(String countryISO2) {
        List<SwiftCodeMongo> swiftCodes = swiftCodeRepository.findByCountryISO2(countryISO2.toUpperCase());
        
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
    public String createSwiftCode(CreateSwiftCodeRequest request) {
        String countryISO2 = request.getCountryISO2().toUpperCase();
        String countryName = request.getCountryName().toUpperCase();
        
        if (swiftCodeRepository.existsBySwiftCode(request.getSwiftCode())) {
            throw new IllegalArgumentException("SWIFT code already exists: " + request.getSwiftCode());
        }
        
        SwiftCodeMongo swiftCode = SwiftCodeMongo.builder()
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
    public String deleteSwiftCode(String swiftCode) {
        if (!swiftCodeRepository.existsBySwiftCode(swiftCode)) {
            throw new ResourceNotFoundException("SWIFT code not found: " + swiftCode);
        }
        
        swiftCodeRepository.deleteBySwiftCode(swiftCode);
        return "SWIFT code deleted successfully";
    }
    
    private SwiftCodeResponse mapToSwiftCodeResponse(SwiftCodeMongo swiftCode) {
        return SwiftCodeResponse.builder()
                .swiftCode(swiftCode.getSwiftCode())
                .bankName(swiftCode.getBankName())
                .countryISO2(swiftCode.getCountryISO2())
                .countryName(swiftCode.getCountryName())
                .isHeadquarter(swiftCode.getIsHeadquarter())
                .address(swiftCode.getAddress())
                .build();
    }
    
    private SwiftCodeBranchDTO mapToSwiftCodeBranchDTO(SwiftCodeMongo swiftCode) {
        return SwiftCodeBranchDTO.builder()
                .swiftCode(swiftCode.getSwiftCode())
                .bankName(swiftCode.getBankName())
                .countryISO2(swiftCode.getCountryISO2())
                .isHeadquarter(swiftCode.getIsHeadquarter())
                .address(swiftCode.getAddress())
                .build();
    }
    
    private SwiftCodeMongo convertToMongoDocument(SwiftCode jpaEntity) {
        return SwiftCodeMongo.builder()
                .swiftCode(jpaEntity.getSwiftCode())
                .bankName(jpaEntity.getBankName())
                .countryISO2(jpaEntity.getCountryISO2())
                .countryName(jpaEntity.getCountryName())
                .address(jpaEntity.getAddress())
                .isHeadquarter(jpaEntity.getIsHeadquarter())
                .build();
    }
} 