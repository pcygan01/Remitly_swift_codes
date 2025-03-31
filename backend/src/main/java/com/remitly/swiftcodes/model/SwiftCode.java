package com.remitly.swiftcodes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "swift_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SwiftCode {

    @Id
    @NotBlank(message = "SWIFT code is required")
    @Column(name = "swift_code", nullable = false, unique = true)
    private String swiftCode;
    
    @NotBlank(message = "Bank name is required")
    @Column(name = "bank_name", nullable = false)
    private String bankName;
    
    @NotBlank(message = "Country ISO2 code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country ISO2 must be a valid 2-letter code")
    @Column(name = "country_iso2", nullable = false)
    private String countryISO2;
    
    @NotBlank(message = "Country name is required")
    @Column(name = "country_name", nullable = false)
    private String countryName;
    
    @Column(name = "address")
    private String address;
    
    @NotNull(message = "Is headquarter field is required")
    @Column(name = "is_headquarter", nullable = false)
    private Boolean isHeadquarter;
    
    // This field is not persisted but used in responses for headquarter swift codes
    @jakarta.persistence.Transient
    private transient java.util.List<SwiftCodeBranchDTO> branches;
} 