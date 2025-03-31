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
        COUNTRY_CODES.put("PL", "Polska");
        COUNTRY_CODES.put("MT", "Malta");
        COUNTRY_CODES.put("US", "Stany Zjednoczone");
        COUNTRY_CODES.put("GB", "Wielka Brytania");
        COUNTRY_CODES.put("DE", "Niemcy");
        COUNTRY_CODES.put("FR", "Francja");
        COUNTRY_CODES.put("IT", "Włochy");
        COUNTRY_CODES.put("ES", "Hiszpania");
        COUNTRY_CODES.put("PT", "Portugalia");
        COUNTRY_CODES.put("NL", "Holandia");
        COUNTRY_CODES.put("BE", "Belgia");
        COUNTRY_CODES.put("LU", "Luksemburg");
        COUNTRY_CODES.put("IE", "Irlandia");
        COUNTRY_CODES.put("DK", "Dania");
        COUNTRY_CODES.put("SE", "Szwecja");
        COUNTRY_CODES.put("FI", "Finlandia");
        COUNTRY_CODES.put("AT", "Austria");
        COUNTRY_CODES.put("CY", "Cypr");
        COUNTRY_CODES.put("CZ", "Czechy");
        COUNTRY_CODES.put("EE", "Estonia");
        COUNTRY_CODES.put("HU", "Węgry");
        COUNTRY_CODES.put("LV", "Łotwa");
        COUNTRY_CODES.put("LT", "Litwa");
        COUNTRY_CODES.put("RO", "Rumunia");
        COUNTRY_CODES.put("SK", "Słowacja");
        COUNTRY_CODES.put("SI", "Słowenia");
        COUNTRY_CODES.put("BG", "Bułgaria");
        COUNTRY_CODES.put("HR", "Chorwacja");
        COUNTRY_CODES.put("AD", "Andora");
        COUNTRY_CODES.put("AE", "Zjednoczone Emiraty Arabskie");
        COUNTRY_CODES.put("AF", "Afganistan");
        COUNTRY_CODES.put("AG", "Antigua i Barbuda");
        COUNTRY_CODES.put("AL", "Albania");
        COUNTRY_CODES.put("AM", "Armenia");
        COUNTRY_CODES.put("AO", "Angola");
        COUNTRY_CODES.put("AR", "Argentyna");
        COUNTRY_CODES.put("AU", "Australia");
        COUNTRY_CODES.put("AZ", "Azerbejdżan");
        COUNTRY_CODES.put("BA", "Bośnia i Hercegowina");
        COUNTRY_CODES.put("BB", "Barbados");
        COUNTRY_CODES.put("BD", "Bangladesz");
        COUNTRY_CODES.put("BF", "Burkina Faso");
        COUNTRY_CODES.put("BH", "Bahrajn");
        COUNTRY_CODES.put("BI", "Burundi");
        COUNTRY_CODES.put("BJ", "Benin");
        COUNTRY_CODES.put("BM", "Bermudy");
        COUNTRY_CODES.put("BN", "Brunei");
        COUNTRY_CODES.put("BO", "Boliwia");
        COUNTRY_CODES.put("BR", "Brazylia");
        COUNTRY_CODES.put("BS", "Bahamy");
        COUNTRY_CODES.put("BT", "Bhutan");
        COUNTRY_CODES.put("BW", "Botswana");
        COUNTRY_CODES.put("BY", "Białoruś");
        COUNTRY_CODES.put("BZ", "Belize");
        COUNTRY_CODES.put("CA", "Kanada");
        COUNTRY_CODES.put("CD", "Demokratyczna Republika Konga");
        COUNTRY_CODES.put("CF", "Republika Środkowoafrykańska");
        COUNTRY_CODES.put("CG", "Kongo");
        COUNTRY_CODES.put("CH", "Szwajcaria");
        COUNTRY_CODES.put("CI", "Wybrzeże Kości Słoniowej");
        COUNTRY_CODES.put("CL", "Chile");
        COUNTRY_CODES.put("CM", "Kamerun");
        COUNTRY_CODES.put("CN", "Chiny");
        COUNTRY_CODES.put("CO", "Kolumbia");
        COUNTRY_CODES.put("CR", "Kostaryka");
        COUNTRY_CODES.put("CU", "Kuba");
        COUNTRY_CODES.put("CV", "Republika Zielonego Przylądka");
        COUNTRY_CODES.put("DJ", "Dżibuti");
        COUNTRY_CODES.put("DM", "Dominika");
        COUNTRY_CODES.put("DO", "Dominikana");
        COUNTRY_CODES.put("DZ", "Algieria");
        COUNTRY_CODES.put("EC", "Ekwador");
        COUNTRY_CODES.put("EG", "Egipt");
        COUNTRY_CODES.put("ER", "Erytrea");
        COUNTRY_CODES.put("ET", "Etiopia");
        COUNTRY_CODES.put("FJ", "Fidżi");
        COUNTRY_CODES.put("FM", "Mikronezja");
        COUNTRY_CODES.put("GA", "Gabon");
        COUNTRY_CODES.put("GD", "Grenada");
        COUNTRY_CODES.put("GE", "Gruzja");
        COUNTRY_CODES.put("GH", "Ghana");
        COUNTRY_CODES.put("GM", "Gambia");
        COUNTRY_CODES.put("GN", "Gwinea");
        COUNTRY_CODES.put("GQ", "Gwinea Równikowa");
        COUNTRY_CODES.put("GR", "Grecja");
        COUNTRY_CODES.put("GT", "Gwatemala");
        COUNTRY_CODES.put("GW", "Gwinea Bissau");
        COUNTRY_CODES.put("GY", "Gujana");
        COUNTRY_CODES.put("HK", "Hongkong");
        COUNTRY_CODES.put("HN", "Honduras");
        COUNTRY_CODES.put("HT", "Haiti");
        COUNTRY_CODES.put("ID", "Indonezja");
        COUNTRY_CODES.put("IL", "Izrael");
        COUNTRY_CODES.put("IN", "Indie");
        COUNTRY_CODES.put("IQ", "Irak");
        COUNTRY_CODES.put("IR", "Iran");
        COUNTRY_CODES.put("IS", "Islandia");
        COUNTRY_CODES.put("JM", "Jamajka");
        COUNTRY_CODES.put("JO", "Jordania");
        COUNTRY_CODES.put("JP", "Japonia");
        COUNTRY_CODES.put("KE", "Kenia");
        COUNTRY_CODES.put("KG", "Kirgistan");
        COUNTRY_CODES.put("KH", "Kambodża");
        COUNTRY_CODES.put("KI", "Kiribati");
        COUNTRY_CODES.put("KM", "Komory");
        COUNTRY_CODES.put("KN", "Saint Kitts i Nevis");
        COUNTRY_CODES.put("KP", "Korea Północna");
        COUNTRY_CODES.put("KR", "Korea Południowa");
        COUNTRY_CODES.put("KW", "Kuwejt");
        COUNTRY_CODES.put("KZ", "Kazachstan");
        COUNTRY_CODES.put("LA", "Laos");
        COUNTRY_CODES.put("LB", "Liban");
        COUNTRY_CODES.put("LC", "Saint Lucia");
        COUNTRY_CODES.put("LI", "Liechtenstein");
        COUNTRY_CODES.put("LK", "Sri Lanka");
        COUNTRY_CODES.put("LR", "Liberia");
        COUNTRY_CODES.put("LS", "Lesotho");
        COUNTRY_CODES.put("LY", "Libia");
        COUNTRY_CODES.put("MA", "Maroko");
        COUNTRY_CODES.put("MC", "Monako");
        COUNTRY_CODES.put("MD", "Mołdawia");
        COUNTRY_CODES.put("ME", "Czarnogóra");
        COUNTRY_CODES.put("MG", "Madagaskar");
        COUNTRY_CODES.put("MH", "Wyspy Marshalla");
        COUNTRY_CODES.put("MK", "Macedonia Północna");
        COUNTRY_CODES.put("ML", "Mali");
        COUNTRY_CODES.put("MM", "Mjanma");
        COUNTRY_CODES.put("MN", "Mongolia");
        COUNTRY_CODES.put("MR", "Mauretania");
        COUNTRY_CODES.put("MU", "Mauritius");
        COUNTRY_CODES.put("MV", "Malediwy");
        COUNTRY_CODES.put("MW", "Malawi");
        COUNTRY_CODES.put("MX", "Meksyk");
        COUNTRY_CODES.put("MY", "Malezja");
        COUNTRY_CODES.put("MZ", "Mozambik");
        COUNTRY_CODES.put("NA", "Namibia");
        COUNTRY_CODES.put("NE", "Niger");
        COUNTRY_CODES.put("NG", "Nigeria");
        COUNTRY_CODES.put("NI", "Nikaragua");
        COUNTRY_CODES.put("NO", "Norwegia");
        COUNTRY_CODES.put("NP", "Nepal");
        COUNTRY_CODES.put("NR", "Nauru");
        COUNTRY_CODES.put("NZ", "Nowa Zelandia");
        COUNTRY_CODES.put("OM", "Oman");
        COUNTRY_CODES.put("PA", "Panama");
        COUNTRY_CODES.put("PE", "Peru");
        COUNTRY_CODES.put("PG", "Papua-Nowa Gwinea");
        COUNTRY_CODES.put("PH", "Filipiny");
        COUNTRY_CODES.put("PK", "Pakistan");
        COUNTRY_CODES.put("PR", "Portoryko");
        COUNTRY_CODES.put("PS", "Palestyna");
        COUNTRY_CODES.put("PY", "Paragwaj");
        COUNTRY_CODES.put("QA", "Katar");
        COUNTRY_CODES.put("RS", "Serbia");
        COUNTRY_CODES.put("RU", "Rosja");
        COUNTRY_CODES.put("RW", "Rwanda");
        COUNTRY_CODES.put("SA", "Arabia Saudyjska");
        COUNTRY_CODES.put("SB", "Wyspy Salomona");
        COUNTRY_CODES.put("SC", "Seszele");
        COUNTRY_CODES.put("SD", "Sudan");
        COUNTRY_CODES.put("SG", "Singapur");
        COUNTRY_CODES.put("SL", "Sierra Leone");
        COUNTRY_CODES.put("SM", "San Marino");
        COUNTRY_CODES.put("SN", "Senegal");
        COUNTRY_CODES.put("SO", "Somalia");
        COUNTRY_CODES.put("SR", "Surinam");
        COUNTRY_CODES.put("SS", "Sudan Południowy");
        COUNTRY_CODES.put("ST", "Wyspy Świętego Tomasza i Książęca");
        COUNTRY_CODES.put("SV", "Salwador");
        COUNTRY_CODES.put("SY", "Syria");
        COUNTRY_CODES.put("SZ", "Eswatini");
        COUNTRY_CODES.put("TD", "Czad");
        COUNTRY_CODES.put("TG", "Togo");
        COUNTRY_CODES.put("TH", "Tajlandia");
        COUNTRY_CODES.put("TJ", "Tadżykistan");
        COUNTRY_CODES.put("TL", "Timor Wschodni");
        COUNTRY_CODES.put("TM", "Turkmenistan");
        COUNTRY_CODES.put("TN", "Tunezja");
        COUNTRY_CODES.put("TO", "Tonga");
        COUNTRY_CODES.put("TR", "Turcja");
        COUNTRY_CODES.put("TT", "Trynidad i Tobago");
        COUNTRY_CODES.put("TV", "Tuvalu");
        COUNTRY_CODES.put("TW", "Tajwan");
        COUNTRY_CODES.put("TZ", "Tanzania");
        COUNTRY_CODES.put("UA", "Ukraina");
        COUNTRY_CODES.put("UG", "Uganda");
        COUNTRY_CODES.put("UY", "Urugwaj");
        COUNTRY_CODES.put("UZ", "Uzbekistan");
        COUNTRY_CODES.put("VA", "Watykan");
        COUNTRY_CODES.put("VC", "Saint Vincent i Grenadyny");
        COUNTRY_CODES.put("VE", "Wenezuela");
        COUNTRY_CODES.put("VN", "Wietnam");
        COUNTRY_CODES.put("VU", "Vanuatu");
        COUNTRY_CODES.put("WS", "Samoa");
        COUNTRY_CODES.put("YE", "Jemen");
        COUNTRY_CODES.put("ZA", "Republika Południowej Afryki");
        COUNTRY_CODES.put("ZM", "Zambia");
        COUNTRY_CODES.put("ZW", "Zimbabwe");
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