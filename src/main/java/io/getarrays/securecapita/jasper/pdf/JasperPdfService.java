package io.getarrays.securecapita.jasper.pdf;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class JasperPdfService {
    private static final String LOGO_PATH = "/images/logo.png";
    private static final String COMPANY_ADDRESS = "1234 Street, City, Country";

    public ResponseEntity<?> generateAssetReport(String templateName, JRBeanCollectionDataSource jrDataSource, int pageLimit, String reportName) throws JRException, IOException {
        InputStream inputStream = getClass().getResourceAsStream(templateName);
        JasperDesign design = JRXmlLoader.load(inputStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(design);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("COMPANY_ADDRESS", COMPANY_ADDRESS);
//        ImageIcon imageIcon = new ImageIcon(new ImageIcon(LOGO_PATH).getImage());
//        parameters.put("LOGO_PATH", imageIcon.getImage());
        Logger.getLogger(getClass().getName()).info("Total records: " + jrDataSource.getRecordCount());
        int totalRecords = jrDataSource.getRecordCount();
        int pages = (int) Math.ceil((double) totalRecords / pageLimit);

        List<JasperPrint> jasperPrintList = new ArrayList<>();

        List<Object> allData = new ArrayList<>(jrDataSource.getData());

        for (int i = 0; i < pages; i++) {
            int startIndex = i * pageLimit;
            int endIndex = Math.min(startIndex + pageLimit, totalRecords);

            List<Object> pageData = new ArrayList<>(allData.subList(startIndex, endIndex));
            JRBeanCollectionDataSource pageDataSource = new JRBeanCollectionDataSource(pageData);

            JasperPrint pageJasperPrint = JasperFillManager.fillReport(jasperReport, parameters, pageDataSource);
            jasperPrintList.add(pageJasperPrint);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        configuration.setCreatingBatchModeBookmarks(true);
        exporter.setConfiguration(configuration);

        exporter.exportReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", reportName);
        headers.setContentLength(outputStream.size());
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }
}