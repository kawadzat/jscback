package io.getarrays.securecapita.jasper.pdf;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.service.AssertService;
import io.getarrays.securecapita.dto.AssetsStats;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.jasper.downloadtoken.DownloadToken;
import io.getarrays.securecapita.jasper.downloadtoken.DownloadTokenService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/jasper/pdf")
@RequiredArgsConstructor
public class PdfDownloadController {
    private final JasperPdfService jasperPdfService;

    private final AssertService assertService;
    private final DownloadTokenService downloadTokenService;
    private static final String ASSET_TEMPLATE = "/jasper/assettemplate.jrxml";
    private static final String STATION_ASSET_TEMPLATE = "/jasper/specificassettemplate.jrxml";

    private static final String ASSET_REPORT_NAME = "assetreport.pdf";
    private static final String STATION_ASSET_REPORT_NAME = "stationassetreport.pdf";


    @GetMapping("/asset")
    public ResponseEntity<?> downloadAssetsPdf(@RequestParam("token") UUID token) throws JRException, IOException {
        Optional<DownloadToken> downloadTokenOptional = downloadTokenService.validateDownloadToken(token);
        if (downloadTokenOptional.isPresent() && downloadTokenOptional.get().isValid() && downloadTokenOptional.get().isAssertStat()) {
            AssetsStats assetStats = assertService.getStatsUsingToken(downloadTokenOptional.get());
            assetStats.getAssetsStats().addAll(assetStats.getAssetsStats());
            List<Map<String, Object>> dataSource = new ArrayList<>();
            assetStats.getAssetsStats().forEach((assetItemStat -> dataSource.add(Map.of(
                    "name", assetItemStat.getName(),
                    "total", assetItemStat.getTotal()
            ))));
            System.out.println("total assets: "+dataSource.size());
            JRBeanCollectionDataSource jrDataSource = new JRBeanCollectionDataSource(dataSource);
            return jasperPdfService.generateAssetReport(ASSET_TEMPLATE, jrDataSource, 10, ASSET_REPORT_NAME);
        }
        return ResponseEntity.badRequest().body(new CustomMessage("You are not authorized."));
    }
//<field name="date" class="java.util.Date"/>
//    <field name="initialRemarks" class="java.lang.String"/>
//    <field name="assetDisc" class="java.lang.String"/>
//    <field name="assetQty" class="java.lang.Integer"/>
//    <field name="serialNo" class="java.lang.String"/>
//    <field name="invoiceNo" class="java.lang.String"/>
//    <field name="location" class="java.lang.String"/>
//    <field name="assetType" class="java.lang.String"/>
//    <field name="preparedBy" class="java.lang.String"/>
    @GetMapping("/station/assets")
    public ResponseEntity<?> downloadStationAssetsPdf(@RequestParam("stationId") Long stationId,@RequestParam(name = "query",defaultValue = "") String query) throws JRException, IOException {
        List<AssertEntity> assets = assertService.getAssetPDFStation(stationId,query);
        List<Map<String, Object>> dataSource = new ArrayList<>();
        assets.forEach((assetItem -> dataSource.add(Map.of(
                "date", assetItem.getDate() == null ? "" : assetItem.getDate(),
                "initialRemarks", assetItem.getInitialRemarks() == null ? "" : assetItem.getInitialRemarks(),
                "assetDisc", assetItem.getAssetDisc() == null ? "" : assetItem.getAssetDisc(),
//                "assetQty", assetItem.getQuantity(),
                "serialNo", assetItem.getSerialNumber() == null ? "" : assetItem.getSerialNumber(),
                "invoiceNo", assetItem.getInvoiceNumber() == null ? "" : assetItem.getInvoiceNumber(),
                "location", assetItem.getLocation() == null ? "" : assetItem.getLocation(),
                "assetType", assetItem.getAssertType() == null ? "" : assetItem.getAssertType(),
                "preparedBy", assetItem.getPreparedBy() == null ? "" : assetItem.getPreparedBy().getFirstName()
        ))));
        JRBeanCollectionDataSource jrDataSource = new JRBeanCollectionDataSource(dataSource);
        return jasperPdfService.generateAssetReport(STATION_ASSET_TEMPLATE, jrDataSource, 10, STATION_ASSET_REPORT_NAME);
    }
}
