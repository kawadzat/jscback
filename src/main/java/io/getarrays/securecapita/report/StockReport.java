package io.getarrays.securecapita.report;

import io.getarrays.securecapita.domain.Stock;
import io.getarrays.securecapita.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Slf4j
public class StockReport {


    public static final String DATE_FORMATTER = "yyyy-MM-dd hh:mm:ss";
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Stock> stock;
    private static String[] HEADERS = { "ID", "DATE", "PRODUCT CODE", "PRODUCT NAME", "QUANTITY" };

    public StockReport(List<Stock> stock) {
        this.stock = stock;
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("Stock Report");
    }
//this library we try
    private void setHeaders() {
        Row headerRow = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        for (int index = 0; index < HEADERS.length; index++) {
            Cell cell = headerRow.createCell(index);
            cell.setCellValue(HEADERS[index]);
            cell.setCellStyle(style);
        }
    }

    public InputStreamResource export() {
        return generateReport();
    }

    private InputStreamResource generateReport() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            setHeaders();
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontHeightInPoints((short) 10);
            style.setFont(font);
            int rowIndex = 1;
            for (Stock stockItem : stock) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(stockItem.getId());
                row.createCell(1).setCellValue(stockItem.getDate());
                row.createCell(2).setCellValue(stockItem.getProductCode());
                row.createCell(3).setCellValue(stockItem.getProductName());
                row.createCell(4).setCellValue(stockItem.getQuantity());
                for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
                    row.getCell(columnIndex).setCellStyle(style);
                }
            }
            workbook.write(out);
            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Unable to export report file");
        }
    }









}
