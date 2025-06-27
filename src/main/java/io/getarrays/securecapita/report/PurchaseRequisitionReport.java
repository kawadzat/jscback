package io.getarrays.securecapita.report;

import io.getarrays.securecapita.domain.PurchaseRequisition;


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

import static java.util.stream.IntStream.range;

@Slf4j
public class PurchaseRequisitionReport {
    public static final String DATE_FORMATTER = "yyyy-MM-dd hh:mm:ss";
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<PurchaseRequisition> purchaseRequisition;
    private static String[] HEADERS = { "ID", "date", " departmentCode", "reason","itemNumber","ItemDescription","unitPrice","quantity" ," estimatedValue"," receiverEmail","signature"};

    public PurchaseRequisitionReport(List<PurchaseRequisition> purchaseRequisition) {
        this.purchaseRequisition = purchaseRequisition;
        workbook = new XSSFWorkbook();
        sheet=workbook.createSheet("PurchaseRequests");
        setHeaders();
    }

private void setHeaders(){
    Row headerRow = sheet.createRow(0);
    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setFontHeight(14);
    style.setFont(font);
    range(0, HEADERS.length).forEach(index -> {
        Cell cell = headerRow.createCell(index);
        cell.setCellValue(HEADERS[index]);
        cell.setCellStyle(style);
    });

}

    public InputStreamResource export() {
        return generateReport();
    }



    private InputStreamResource generateReport() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontHeight(10);
            style.setFont(font);
            int rowIndex = 1;
            for(PurchaseRequisition purchaseRequisition: purchaseRequisition) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(purchaseRequisition.getId());
                row.createCell(1).setCellValue(purchaseRequisition.getDate());
                row.createCell(2).setCellValue(purchaseRequisition.getDepartmentCode());
                row.createCell(3).setCellValue(purchaseRequisition.getReason());
                row.createCell(4).setCellValue(purchaseRequisition.getItemNumber());
                row.createCell(5).setCellValue(purchaseRequisition.getItemDescription());
                     row.createCell(6).setCellValue(purchaseRequisition.getUnitPrice());
                row.createCell(7).setCellValue(purchaseRequisition.getQuantity());
                row.createCell(8).setCellValue(purchaseRequisition.getEstimatedValue());
                row.createCell(9).setCellValue(purchaseRequisition.getReceiverEmail());
                row.createCell(10).setCellValue(purchaseRequisition.getSignature());



            }
            workbook.write(out);
            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Unable to export report file");
        }
    }







}
