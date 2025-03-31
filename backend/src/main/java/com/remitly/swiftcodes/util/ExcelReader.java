package com.remitly.swiftcodes.util;

import com.remitly.swiftcodes.model.SwiftCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ExcelReader {

    private static final String EXCEL_FILE_PATH = "Interns_2025_SWIFT_CODES.xlsx";
    private static final String HEADQUARTER_CODE_SUFFIX = "XXX";
    private static final int COUNTRY_ISO2_COL_INDEX = 0;
    private static final int SWIFT_CODE_COL_INDEX = 1;
    private static final int CODE_TYPE_COL_INDEX = 2;
    private static final int BANK_NAME_COL_INDEX = 3;
    private static final int ADDRESS_COL_INDEX = 4;
    private static final int TOWN_NAME_COL_INDEX = 5;

    private static final Map<String, String> COUNTRY_CODES = new HashMap<>();
    static {
        COUNTRY_CODES.put("PL", "POLAND");
        COUNTRY_CODES.put("MT", "MALTA");
        COUNTRY_CODES.put("US", "UNITED STATES");
        COUNTRY_CODES.put("GB", "GREAT BRITAIN");
        COUNTRY_CODES.put("DE", "GERMANY");
        COUNTRY_CODES.put("FR", "FRANCE");
        COUNTRY_CODES.put("IT", "ITALY");
        COUNTRY_CODES.put("ES", "SPAIN");
        COUNTRY_CODES.put("PT", "PORTUGAL");
        COUNTRY_CODES.put("NL", "NETHERLANDS");
        COUNTRY_CODES.put("BE", "BELGIUM");
        COUNTRY_CODES.put("LU", "LUXEMBOURG");
        COUNTRY_CODES.put("IE", "IRELAND");
        COUNTRY_CODES.put("DK", "DENMARK");
        COUNTRY_CODES.put("SE", "SWEDEN");
        COUNTRY_CODES.put("FI", "FINLAND");
        COUNTRY_CODES.put("AT", "AUSTRIA");
        COUNTRY_CODES.put("CY", "CYPRUS");
        COUNTRY_CODES.put("CZ", "CZECH REPUBLIC");
        COUNTRY_CODES.put("EE", "ESTONIA");
        COUNTRY_CODES.put("HU", "HUNGARY");
        COUNTRY_CODES.put("LV", "LATVIA");
        COUNTRY_CODES.put("LT", "LITHUANIA");
        COUNTRY_CODES.put("RO", "ROMANIA");
        COUNTRY_CODES.put("SK", "SLOVAKIA");
        COUNTRY_CODES.put("SI", "SLOVENIA");
        COUNTRY_CODES.put("BG", "BULGARIA");
        COUNTRY_CODES.put("HR", "CROATIA");
        COUNTRY_CODES.put("AD", "ANDORRA");
        COUNTRY_CODES.put("AE", "UNITED ARAB EMIRATES");
        COUNTRY_CODES.put("AF", "AFGHANISTAN");
        COUNTRY_CODES.put("AG", "ANTIGUA AND BARBUDA");
        COUNTRY_CODES.put("AL", "ALBANIA");
        COUNTRY_CODES.put("AM", "ARMENIA");
        COUNTRY_CODES.put("AO", "ANGOLA");
        COUNTRY_CODES.put("AR", "ARGENTINA");
        COUNTRY_CODES.put("AU", "AUSTRALIA");
        COUNTRY_CODES.put("AZ", "AZERBAIJAN");
        COUNTRY_CODES.put("BA", "BOSNIA AND HERZEGOVINA");
        COUNTRY_CODES.put("BB", "BARBADOS");
        COUNTRY_CODES.put("BD", "BANGLADESH");
        COUNTRY_CODES.put("BF", "BURKINA FASO");
        COUNTRY_CODES.put("BH", "BAHRAIN");
        COUNTRY_CODES.put("BI", "BURUNDI");
        COUNTRY_CODES.put("BJ", "BENIN");
        COUNTRY_CODES.put("BM", "BERMUDA");
        COUNTRY_CODES.put("BN", "BRUNEI");
        COUNTRY_CODES.put("BO", "BOLIVIA");
        COUNTRY_CODES.put("BR", "BRAZIL");
        COUNTRY_CODES.put("BS", "BAHAMAS");
        COUNTRY_CODES.put("BT", "BHUTAN");
        COUNTRY_CODES.put("BW", "BOTSWANA");
        COUNTRY_CODES.put("BY", "BELARUS");
        COUNTRY_CODES.put("BZ", "BELIZE");
        COUNTRY_CODES.put("CA", "CANADA");
        COUNTRY_CODES.put("CD", "DEMOCRATIC REPUBLIC OF THE CONGO");
        COUNTRY_CODES.put("CF", "CENTRAL AFRICAN REPUBLIC");
        COUNTRY_CODES.put("CG", "CONGO");
        COUNTRY_CODES.put("CH", "SWITZERLAND");
        COUNTRY_CODES.put("CI", "IVORY COAST");
        COUNTRY_CODES.put("CL", "CHILE");
        COUNTRY_CODES.put("CM", "CAMEROON");
        COUNTRY_CODES.put("CN", "CHINA");
        COUNTRY_CODES.put("CO", "COLOMBIA");
        COUNTRY_CODES.put("CR", "COSTA RICA");
        COUNTRY_CODES.put("CU", "CUBA");
        COUNTRY_CODES.put("CV", "CAPE VERDE");
        COUNTRY_CODES.put("DJ", "DJIBOUTI");
        COUNTRY_CODES.put("DM", "DOMINICA");
        COUNTRY_CODES.put("DO", "DOMINICAN REPUBLIC");
        COUNTRY_CODES.put("DZ", "ALGERIA");
        COUNTRY_CODES.put("EC", "ECUADOR");
        COUNTRY_CODES.put("EG", "EGYPT");
        COUNTRY_CODES.put("ER", "ERITREA");
        COUNTRY_CODES.put("ET", "ETHIOPIA");
        COUNTRY_CODES.put("FJ", "FIJI");
        COUNTRY_CODES.put("FM", "MICRONESIA");
        COUNTRY_CODES.put("GA", "GABON");
        COUNTRY_CODES.put("GD", "GRENADA");
        COUNTRY_CODES.put("GE", "GEORGIA");
        COUNTRY_CODES.put("GH", "GHANA");
        COUNTRY_CODES.put("GM", "GAMBIA");
        COUNTRY_CODES.put("GN", "GUINEA");
        COUNTRY_CODES.put("GQ", "EQUATORIAL GUINEA");
        COUNTRY_CODES.put("GR", "GREECE");
        COUNTRY_CODES.put("GT", "GUATEMALA");
        COUNTRY_CODES.put("GW", "GUINEA-BISSAU");
        COUNTRY_CODES.put("GY", "GUYANA");
        COUNTRY_CODES.put("HK", "HONG KONG");
        COUNTRY_CODES.put("HN", "HONDURAS");
        COUNTRY_CODES.put("HT", "HAITI");
        COUNTRY_CODES.put("ID", "INDONESIA");
        COUNTRY_CODES.put("IL", "ISRAEL");
        COUNTRY_CODES.put("IN", "INDIA");
        COUNTRY_CODES.put("IQ", "IRAQ");
        COUNTRY_CODES.put("IR", "IRAN");
        COUNTRY_CODES.put("IS", "ICELAND");
        COUNTRY_CODES.put("JM", "JAMAICA");
        COUNTRY_CODES.put("JO", "JORDAN");
        COUNTRY_CODES.put("JP", "JAPAN");
        COUNTRY_CODES.put("KE", "KENYA");
        COUNTRY_CODES.put("KG", "KYRGYZSTAN");
        COUNTRY_CODES.put("KH", "CAMBODIA");
        COUNTRY_CODES.put("KI", "KIRIBATI");
        COUNTRY_CODES.put("KM", "COMOROS");
        COUNTRY_CODES.put("KN", "SAINT KITTS AND NEVIS");
        COUNTRY_CODES.put("KP", "NORTH KOREA");
        COUNTRY_CODES.put("KR", "SOUTH KOREA");
        COUNTRY_CODES.put("KW", "KUWAIT");
        COUNTRY_CODES.put("KZ", "KAZAKHSTAN");
        COUNTRY_CODES.put("LA", "LAOS");
        COUNTRY_CODES.put("LB", "LEBANON");
        COUNTRY_CODES.put("LC", "SAINT LUCIA");
        COUNTRY_CODES.put("LI", "LIECHTENSTEIN");
        COUNTRY_CODES.put("LK", "SRI LANKA");
        COUNTRY_CODES.put("LR", "LIBERIA");
        COUNTRY_CODES.put("LS", "LESOTHO");
        COUNTRY_CODES.put("LY", "LIBYA");
        COUNTRY_CODES.put("MA", "MOROCCO");
        COUNTRY_CODES.put("MC", "MONACO");
        COUNTRY_CODES.put("MD", "MOLDOVA");
        COUNTRY_CODES.put("ME", "MONTENEGRO");
        COUNTRY_CODES.put("MG", "MADAGASCAR");
        COUNTRY_CODES.put("MH", "MARSHALL ISLANDS");
        COUNTRY_CODES.put("MK", "NORTH MACEDONIA");
        COUNTRY_CODES.put("ML", "MALI");
        COUNTRY_CODES.put("MM", "MYANMAR");
        COUNTRY_CODES.put("MN", "MONGOLIA");
        COUNTRY_CODES.put("MR", "MAURITANIA");
        COUNTRY_CODES.put("MU", "MAURITIUS");
        COUNTRY_CODES.put("MV", "MALDIVES");
        COUNTRY_CODES.put("MW", "MALAWI");
        COUNTRY_CODES.put("MX", "MEXICO");
        COUNTRY_CODES.put("MY", "MALAYSIA");
        COUNTRY_CODES.put("MZ", "MOZAMBIQUE");
        COUNTRY_CODES.put("NA", "NAMIBIA");
        COUNTRY_CODES.put("NE", "NIGER");
        COUNTRY_CODES.put("NG", "NIGERIA");
        COUNTRY_CODES.put("NI", "NICARAGUA");
        COUNTRY_CODES.put("NO", "NORWAY");
        COUNTRY_CODES.put("NP", "NEPAL");
        COUNTRY_CODES.put("NR", "NAURU");
        COUNTRY_CODES.put("NZ", "NEW ZEALAND");
        COUNTRY_CODES.put("OM", "OMAN");
        COUNTRY_CODES.put("PA", "PANAMA");
        COUNTRY_CODES.put("PE", "PERU");
        COUNTRY_CODES.put("PG", "PAPUA NEW GUINEA");
        COUNTRY_CODES.put("PH", "PHILIPPINES");
        COUNTRY_CODES.put("PK", "PAKISTAN");
        COUNTRY_CODES.put("PR", "PUERTO RICO");
        COUNTRY_CODES.put("PS", "PALESTINE");
        COUNTRY_CODES.put("PY", "PARAGUAY");
        COUNTRY_CODES.put("QA", "QATAR");
        COUNTRY_CODES.put("RS", "SERBIA");
        COUNTRY_CODES.put("RU", "RUSSIA");
        COUNTRY_CODES.put("RW", "RWANDA");
        COUNTRY_CODES.put("SA", "SAUDI ARABIA");
        COUNTRY_CODES.put("SB", "SOLOMON ISLANDS");
        COUNTRY_CODES.put("SC", "SEYCHELLES");
        COUNTRY_CODES.put("SD", "SUDAN");
        COUNTRY_CODES.put("SG", "SINGAPORE");
        COUNTRY_CODES.put("SL", "SIERRA LEONE");
        COUNTRY_CODES.put("SM", "SAN MARINO");
        COUNTRY_CODES.put("SN", "SENEGAL");
        COUNTRY_CODES.put("SO", "SOMALIA");
        COUNTRY_CODES.put("SR", "SURINAME");
        COUNTRY_CODES.put("SS", "SOUTH SUDAN");
        COUNTRY_CODES.put("ST", "SAO TOME AND PRINCIPE");
        COUNTRY_CODES.put("SV", "EL SALVADOR");
        COUNTRY_CODES.put("SY", "SYRIA");
        COUNTRY_CODES.put("SZ", "ESWATINI");
        COUNTRY_CODES.put("TD", "CHAD");
        COUNTRY_CODES.put("TG", "TOGO");
        COUNTRY_CODES.put("TH", "THAILAND");
        COUNTRY_CODES.put("TJ", "TAJIKISTAN");
        COUNTRY_CODES.put("TL", "EAST TIMOR");
        COUNTRY_CODES.put("TM", "TURKMENISTAN");
        COUNTRY_CODES.put("TN", "TUNISIA");
        COUNTRY_CODES.put("TO", "TONGA");
        COUNTRY_CODES.put("TR", "TURKEY");
        COUNTRY_CODES.put("TT", "TRINIDAD AND TOBAGO");
        COUNTRY_CODES.put("TV", "TUVALU");
        COUNTRY_CODES.put("TW", "TAIWAN");
        COUNTRY_CODES.put("TZ", "TANZANIA");
        COUNTRY_CODES.put("UA", "UKRAINE");
        COUNTRY_CODES.put("UG", "UGANDA");
        COUNTRY_CODES.put("UY", "URUGUAY");
        COUNTRY_CODES.put("UZ", "UZBEKISTAN");
        COUNTRY_CODES.put("VA", "VATICAN CITY");
        COUNTRY_CODES.put("VC", "SAINT VINCENT AND THE GRENADINES");
        COUNTRY_CODES.put("VE", "VENEZUELA");
        COUNTRY_CODES.put("VN", "VIETNAM");
        COUNTRY_CODES.put("VU", "VANUATU");
        COUNTRY_CODES.put("WS", "SAMOA");
        COUNTRY_CODES.put("YE", "YEMEN");
        COUNTRY_CODES.put("ZA", "SOUTH AFRICA");
        COUNTRY_CODES.put("ZM", "ZAMBIA");
        COUNTRY_CODES.put("ZW", "ZIMBABWE");
    }

    /**
     * Get country name by ISO2 code
     * @param countryISO2 Two-letter country code (ISO2)
     * @return Country name or null if not found
     */
    public String getCountryNameByISO2(String countryISO2) {
        return COUNTRY_CODES.get(countryISO2);
    }

    public List<SwiftCode> readSwiftCodesFromExcel() {
        List<SwiftCode> swiftCodes = new ArrayList<>();
        
        try (InputStream is = new ClassPathResource(EXCEL_FILE_PATH).getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                SwiftCode swiftCode = parseRowToSwiftCode(row);
                if (swiftCode != null) {
                    swiftCodes.add(swiftCode);
                }
            }
            
        } catch (IOException e) {
            log.error("Error reading Excel file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to read SWIFT codes from Excel file", e);
        }
        
        return swiftCodes;
    }
    
    private SwiftCode parseRowToSwiftCode(Row row) {
        try {
            String countryISO2 = getCellStringValue(row, COUNTRY_ISO2_COL_INDEX).toUpperCase();
            String swiftCode = getCellStringValue(row, SWIFT_CODE_COL_INDEX);
            String bankName = getCellStringValue(row, BANK_NAME_COL_INDEX);
            String address = getCellStringValue(row, ADDRESS_COL_INDEX);
            
            boolean isHeadquarter = swiftCode.endsWith(HEADQUARTER_CODE_SUFFIX);
            
            String countryName = COUNTRY_CODES.getOrDefault(countryISO2, countryISO2);
            
            return SwiftCode.builder()
                    .swiftCode(swiftCode)
                    .bankName(bankName)
                    .countryISO2(countryISO2)
                    .countryName(countryName)
                    .address(address)
                    .isHeadquarter(isHeadquarter)
                    .build();
            
        } catch (Exception e) {
            log.warn("Error parsing row {}: {}", row.getRowNum(), e.getMessage());
            return null;
        }
    }
    
    private String getCellStringValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
} 