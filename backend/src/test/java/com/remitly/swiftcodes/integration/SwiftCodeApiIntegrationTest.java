package com.remitly.swiftcodes.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remitly.swiftcodes.model.CreateSwiftCodeRequest;
import com.remitly.swiftcodes.model.SwiftCodeMongo;
import com.remitly.swiftcodes.repository.SwiftCodeMongoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SwiftCodeApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SwiftCodeMongoRepository swiftCodeRepository;

    private SwiftCodeMongo headquarterSwiftCode;
    private SwiftCodeMongo branchSwiftCode;

    @BeforeEach
    void setUp() {
        swiftCodeRepository.deleteAll();

        headquarterSwiftCode = SwiftCodeMongo.builder()
                .swiftCode("TESTAB12XXX")
                .bankName("Test HQ Bank")
                .countryISO2("US")
                .countryName("UNITED STATES")
                .address("123 Test HQ St")
                .isHeadquarter(true)
                .build();

        branchSwiftCode = SwiftCodeMongo.builder()
                .swiftCode("TESTAB12BRN")
                .bankName("Test Branch Bank")
                .countryISO2("US")
                .countryName("UNITED STATES")
                .address("456 Test Branch St")
                .isHeadquarter(false)
                .build();

        List<SwiftCodeMongo> swiftCodes = Arrays.asList(headquarterSwiftCode, branchSwiftCode);
        swiftCodeRepository.saveAll(swiftCodes);
    }

    @AfterEach
    void tearDown() {
        swiftCodeRepository.deleteAll();
    }

    @Test
    void getSwiftCode_WhenHeadquarterExists_ReturnsDetailsWithBranches() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/TESTAB12XXX"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.swiftCode", is("TESTAB12XXX")))
                .andExpect(jsonPath("$.bankName", is("Test HQ Bank")))
                .andExpect(jsonPath("$.countryISO2", is("US")))
                .andExpect(jsonPath("$.countryName", is("UNITED STATES")))
                .andExpect(jsonPath("$.isHeadquarter", is(true)))
                .andExpect(jsonPath("$.branches", hasSize(1)))
                .andExpect(jsonPath("$.branches[0].swiftCode", is("TESTAB12BRN")));
    }

    @Test
    void getSwiftCode_WhenBranchExists_ReturnsDetailsWithoutBranches() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/TESTAB12BRN"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.swiftCode", is("TESTAB12BRN")))
                .andExpect(jsonPath("$.bankName", is("Test Branch Bank")))
                .andExpect(jsonPath("$.countryISO2", is("US")))
                .andExpect(jsonPath("$.countryName", is("UNITED STATES")))
                .andExpect(jsonPath("$.isHeadquarter", is(false)))
                .andExpect(jsonPath("$.branches").doesNotExist());
    }

    @Test
    void getSwiftCode_WhenCodeDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/NONEXISTENT"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }

    @Test
    void getSwiftCodesByCountry_WhenCountryExists_ReturnsAllCodes() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/country/US"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.countryISO2", is("US")))
                .andExpect(jsonPath("$.countryName", is("UNITED STATES")))
                .andExpect(jsonPath("$.swiftCodes", hasSize(2)));
    }

    @Test
    void getSwiftCodesByCountry_WhenCountryDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/country/ZZ"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("No SWIFT codes found")));
    }

    @Test
    void createSwiftCode_WhenValidRequest_CreatesCodeAndReturnsSuccess() throws Exception {
        CreateSwiftCodeRequest request = CreateSwiftCodeRequest.builder()
                .swiftCode("NEWABC12XXX")
                .bankName("New Test Bank")
                .countryISO2("CA")
                .countryName("CANADA")
                .address("123 New Test St")
                .isHeadquarter(true)
                .build();

        mockMvc.perform(post("/v1/swift-codes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("SWIFT code created successfully")));

        // Verify the code was created in the database
        mockMvc.perform(get("/v1/swift-codes/NEWABC12XXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode", is("NEWABC12XXX")))
                .andExpect(jsonPath("$.bankName", is("New Test Bank")));
    }

    @Test
    void deleteSwiftCode_WhenCodeExists_DeletesCodeAndReturnsSuccess() throws Exception {
        mockMvc.perform(delete("/v1/swift-codes/TESTAB12BRN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("SWIFT code deleted successfully")));

        mockMvc.perform(get("/v1/swift-codes/TESTAB12BRN"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSwiftCode_WhenCodeDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/v1/swift-codes/NONEXISTENT"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }
} 