package com.remitly.swiftcodes.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SwiftCodeMongoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValidThenNoViolations() {
        SwiftCodeMongo swiftCode = SwiftCodeMongo.builder()
                .swiftCode("ABCDEF12")
                .bankName("Test Bank")
                .countryISO2("US")
                .countryName("UNITED STATES")
                .address("123 Test St")
                .isHeadquarter(true)
                .build();

        Set<ConstraintViolation<SwiftCodeMongo>> violations = validator.validate(swiftCode);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenSwiftCodeIsNullThenViolation() {
        SwiftCodeMongo swiftCode = SwiftCodeMongo.builder()
                .swiftCode(null)
                .bankName("Test Bank")
                .countryISO2("US")
                .countryName("UNITED STATES")
                .address("123 Test St")
                .isHeadquarter(true)
                .build();

        Set<ConstraintViolation<SwiftCodeMongo>> violations = validator.validate(swiftCode);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("SWIFT code is required", violations.iterator().next().getMessage());
    }

    @Test
    void whenCountryISO2InvalidFormatThenViolation() {
        SwiftCodeMongo swiftCode = SwiftCodeMongo.builder()
                .swiftCode("ABCDEF12")
                .bankName("Test Bank")
                .countryISO2("USA") // Invalid: more than 2 characters
                .countryName("UNITED STATES")
                .address("123 Test St")
                .isHeadquarter(true)
                .build();

        Set<ConstraintViolation<SwiftCodeMongo>> violations = validator.validate(swiftCode);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Country ISO2 must be a valid 2-letter code", violations.iterator().next().getMessage());
    }

    @Test
    void whenIsHeadquarterNullThenViolation() {
        SwiftCodeMongo swiftCode = SwiftCodeMongo.builder()
                .swiftCode("ABCDEF12")
                .bankName("Test Bank")
                .countryISO2("US")
                .countryName("UNITED STATES")
                .address("123 Test St")
                .isHeadquarter(null)
                .build();

        Set<ConstraintViolation<SwiftCodeMongo>> violations = validator.validate(swiftCode);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Is headquarter field is required", violations.iterator().next().getMessage());
    }
} 