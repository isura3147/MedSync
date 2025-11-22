package edu.icet.service;

import edu.icet.model.Medicine;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private MedicineService medicineService;

    public void generateInventoryReport(File fileToSave) throws JRException {
        // Get all medicines from the database
        List<Medicine> medicines = medicineService.getAllMedicines();

        // Load the .jrxml template
        InputStream reportStream = getClass().getResourceAsStream("/reports/InventoryReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Wrap our list of medicines in a Jasper data source
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(medicines);

        // Pass parameters to the report (e.g., report title)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "MedSync Application");

        // Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export the report to the specified PDF file
        JasperExportManager.exportReportToPdfFile(jasperPrint, fileToSave.getAbsolutePath());
    }
}