package com.remitly.swiftcodes.service;

import com.remitly.swiftcodes.exception.ResourceNotFoundException;
import com.remitly.swiftcodes.model.*;
import com.remitly.swiftcodes.repository.SwiftCodeMongoRepository;
import com.remitly.swiftcodes.service.impl.SwiftCodeMongoServiceImpl;
import com.remitly.swiftcodes.util.ExcelReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SwiftCodeMongoServiceImplTest {

    @Mock
    private SwiftCodeMongoRepository swiftCodeRepository;

    @Mock
    private ExcelReader excelReader;

    @InjectMocks
    private SwiftCodeMongoServiceImpl swiftCodeService;

    private SwiftCodeMongo testHeadquarter;
    private SwiftCodeMongo testBranch;
    private List<SwiftCodeMongo> testBranches;

    @BeforeEach
    void setUp() {
        testHeadquarter = SwiftCodeMongo.builder()
                .id("1")
                .swiftCode("ABCDEF12XXX")
                .bankName("Test Headquarter Bank")
                .countryISO2("US")
                .countryName("UNITED STATES")
                .address("123 HQ St")
                .isHeadquarter(true)
                .build();

        testBranch = SwiftCodeMongo.builder()
                .id("2")
                .swiftCode("ABCDEF12BRN")
                .bankName("Test Branch Bank")
                .countryISO2("US")
                .countryName("UNITED STATES")
                .address("456 Branch Ave")
                .isHeadquarter(false)
                .build();

        testBranches = Arrays.asList(testHeadquarter, testBranch);
    }

    @Test
    void getSwiftCode_WhenCodeExists_ReturnsCorrectResponse() {
        when(swiftCodeRepository.findBySwiftCode("ABCDEF12XXX")).thenReturn(Optional.of(testHeadquarter));
        when(swiftCodeRepository.findBranchesByHeadquarterSwiftCodePrefix("ABCDEF12")).thenReturn(testBranches);

        SwiftCodeResponse response = swiftCodeService.getSwiftCode("ABCDEF12XXX");

        assertNotNull(response);
        assertEquals("ABCDEF12XXX", response.getSwiftCode());
        assertEquals("Test Headquarter Bank", response.getBankName());
        assertEquals("US", response.getCountryISO2());
        assertEquals("UNITED STATES", response.getCountryName());
        assertEquals("123 HQ St", response.getAddress());
        assertTrue(response.getIsHeadquarter());
        
        assertEquals(1, response.getBranches().size());
        SwiftCodeBranchDTO branchDTO = response.getBranches().get(0);
        assertEquals("ABCDEF12BRN", branchDTO.getSwiftCode());
    }

    @Test
    void getSwiftCode_WhenCodeDoesNotExist_ThrowsResourceNotFoundException() {
        when(swiftCodeRepository.findBySwiftCode("NONEXISTENT")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            swiftCodeService.getSwiftCode("NONEXISTENT");
        });
    }

    @Test
    void getSwiftCodesByCountry_WhenCountryExists_ReturnsAllCodes() {
        when(swiftCodeRepository.findByCountryISO2("US")).thenReturn(testBranches);

        CountrySwiftCodesResponse response = swiftCodeService.getSwiftCodesByCountry("US");

        assertNotNull(response);
        assertEquals("US", response.getCountryISO2());
        assertEquals("UNITED STATES", response.getCountryName());
        assertEquals(2, response.getSwiftCodes().size());
    }

    @Test
    void getSwiftCodesByCountry_WhenCountryHasNoCodes_ThrowsResourceNotFoundException() {
        when(swiftCodeRepository.findByCountryISO2("ZZ")).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> {
            swiftCodeService.getSwiftCodesByCountry("ZZ");
        });
    }

    @Test
    void createSwiftCode_WhenValidRequest_ReturnsSuccessMessage() {
        CreateSwiftCodeRequest request = CreateSwiftCodeRequest.builder()
            .swiftCode("NEWCODE12")
            .bankName("New Bank")
            .countryISO2("CA")
            .countryName("Canada")
            .address("New Address")
            .isHeadquarter(true)
            .build();

        when(swiftCodeRepository.existsBySwiftCode("NEWCODE12")).thenReturn(false);
        when(swiftCodeRepository.save(any(SwiftCodeMongo.class))).thenReturn(null);

        String result = swiftCodeService.createSwiftCode(request);

        assertEquals("SWIFT code created successfully", result);
        verify(swiftCodeRepository).save(any(SwiftCodeMongo.class));
    }

    @Test
    void createSwiftCode_WhenCodeAlreadyExists_ThrowsIllegalArgumentException() {
        CreateSwiftCodeRequest request = CreateSwiftCodeRequest.builder()
            .swiftCode("EXISTING")
            .bankName("Existing Bank")
            .countryISO2("CA")
            .countryName("Canada")
            .address("Existing Address")
            .isHeadquarter(true)
            .build();

        when(swiftCodeRepository.existsBySwiftCode("EXISTING")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            swiftCodeService.createSwiftCode(request);
        });
        
        verify(swiftCodeRepository, never()).save(any());
    }

    @Test
    void deleteSwiftCode_WhenCodeExists_ReturnsSuccessMessage() {
        when(swiftCodeRepository.existsBySwiftCode("DELETEME")).thenReturn(true);
        doNothing().when(swiftCodeRepository).deleteBySwiftCode("DELETEME");

        String result = swiftCodeService.deleteSwiftCode("DELETEME");

        assertEquals("SWIFT code deleted successfully", result);
        verify(swiftCodeRepository).deleteBySwiftCode("DELETEME");
    }

    @Test
    void deleteSwiftCode_WhenCodeDoesNotExist_ThrowsResourceNotFoundException() {
        when(swiftCodeRepository.existsBySwiftCode("NONEXISTENT")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            swiftCodeService.deleteSwiftCode("NONEXISTENT");
        });
        
        verify(swiftCodeRepository, never()).deleteBySwiftCode(anyString());
    }
} 