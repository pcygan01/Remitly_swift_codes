package com.remitly.swiftcodes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remitly.swiftcodes.exception.ResourceNotFoundException;
import com.remitly.swiftcodes.model.*;
import com.remitly.swiftcodes.service.SwiftCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SwiftCodeController.class)
class SwiftCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SwiftCodeService swiftCodeService;

    private SwiftCodeResponse headquarterResponse;
    private SwiftCodeResponse branchResponse;
    private CountrySwiftCodesResponse countryResponse;
    private CreateSwiftCodeRequest createRequest;

    @BeforeEach
    void setUp() {
        SwiftCodeBranchDTO branchDTO = new SwiftCodeBranchDTO(
                "ABCDEF12BRN",
                "Branch Bank",
                "US",
                false,
                "456 Branch St"
        );

        List<SwiftCodeBranchDTO> branches = Collections.singletonList(branchDTO);

        headquarterResponse = SwiftCodeResponse.builder()
                .swiftCode("ABCDEF12XXX")
                .bankName("HQ Bank")
                .countryISO2("US")
                .countryName("UNITED STATES")
                .isHeadquarter(true)
                .address("123 HQ St")
                .branches(branches)
                .build();

        branchResponse = SwiftCodeResponse.builder()
                .swiftCode("ABCDEF12BRN")
                .bankName("Branch Bank")
                .countryISO2("US")
                .countryName("UNITED STATES")
                .isHeadquarter(false)
                .build();

        countryResponse = CountrySwiftCodesResponse.builder()
                .countryISO2("US")
                .countryName("UNITED STATES")
                .swiftCodes(Arrays.asList(
                        new SwiftCodeBranchDTO("ABCDEF12XXX", "HQ Bank", "US", true, "123 HQ St"),
                        new SwiftCodeBranchDTO("ABCDEF12BRN", "Branch Bank", "US", false, "456 Branch St")
                ))
                .build();

        createRequest = CreateSwiftCodeRequest.builder()
                .swiftCode("NEWCOD12XXX")
                .bankName("New Bank")
                .countryISO2("GB")
                .countryName("United Kingdom")
                .address("789 New St")
                .isHeadquarter(true)
                .build();
    }

    @Test
    void getSwiftCode_WhenCodeExists_ReturnsCorrectResponse() throws Exception {
        when(swiftCodeService.getSwiftCode("ABCDEF12XXX")).thenReturn(headquarterResponse);

        mockMvc.perform(get("/v1/swift-codes/ABCDEF12XXX"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.swiftCode", is("ABCDEF12XXX")))
                .andExpect(jsonPath("$.bankName", is("HQ Bank")))
                .andExpect(jsonPath("$.countryISO2", is("US")))
                .andExpect(jsonPath("$.isHeadquarter", is(true)))
                .andExpect(jsonPath("$.branches", hasSize(1)))
                .andExpect(jsonPath("$.branches[0].swiftCode", is("ABCDEF12BRN")));
    }

    @Test
    void getSwiftCode_WhenCodeDoesNotExist_ReturnsNotFound() throws Exception {
        when(swiftCodeService.getSwiftCode("NONEXISTENT"))
                .thenThrow(new ResourceNotFoundException("SWIFT code not found: NONEXISTENT"));

        mockMvc.perform(get("/v1/swift-codes/NONEXISTENT"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }

    @Test
    void getSwiftCodesByCountry_WhenCountryExists_ReturnsAllCodes() throws Exception {
        when(swiftCodeService.getSwiftCodesByCountry("US")).thenReturn(countryResponse);

        mockMvc.perform(get("/v1/swift-codes/country/US"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.countryISO2", is("US")))
                .andExpect(jsonPath("$.countryName", is("UNITED STATES")))
                .andExpect(jsonPath("$.swiftCodes", hasSize(2)))
                .andExpect(jsonPath("$.swiftCodes[0].swiftCode", is("ABCDEF12XXX")))
                .andExpect(jsonPath("$.swiftCodes[1].swiftCode", is("ABCDEF12BRN")));
    }

    @Test
    void getSwiftCodesByCountry_WhenCountryDoesNotExist_ReturnsNotFound() throws Exception {
        when(swiftCodeService.getSwiftCodesByCountry("ZZ"))
                .thenThrow(new ResourceNotFoundException("No SWIFT codes found for country: ZZ"));

        mockMvc.perform(get("/v1/swift-codes/country/ZZ"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("No SWIFT codes found")));
    }

    @Test
    void createSwiftCode_WhenValidRequest_ReturnsCreated() throws Exception {
        when(swiftCodeService.createSwiftCode(any(CreateSwiftCodeRequest.class)))
                .thenReturn("SWIFT code created successfully");

        mockMvc.perform(post("/v1/swift-codes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("SWIFT code created successfully")));
    }

    @Test
    void createSwiftCode_WhenInvalidRequest_ReturnsBadRequest() throws Exception {
        CreateSwiftCodeRequest invalidRequest = new CreateSwiftCodeRequest();
        
        mockMvc.perform(post("/v1/swift-codes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSwiftCode_WhenCodeExists_ReturnsOk() throws Exception {
        when(swiftCodeService.deleteSwiftCode("DELETEME"))
                .thenReturn("SWIFT code deleted successfully");

        mockMvc.perform(delete("/v1/swift-codes/DELETEME"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("SWIFT code deleted successfully")));
    }

    @Test
    void deleteSwiftCode_WhenCodeDoesNotExist_ReturnsNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("SWIFT code not found: NONEXISTENT"))
                .when(swiftCodeService).deleteSwiftCode("NONEXISTENT");

        mockMvc.perform(delete("/v1/swift-codes/NONEXISTENT"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }
} 