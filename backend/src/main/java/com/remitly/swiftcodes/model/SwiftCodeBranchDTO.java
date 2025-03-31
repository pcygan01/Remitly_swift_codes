package com.remitly.swiftcodes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SwiftCodeBranchDTO {
    private String swiftCode;
    private String bankName;
    private String countryISO2;
    private Boolean isHeadquarter;
    private String address;
} 