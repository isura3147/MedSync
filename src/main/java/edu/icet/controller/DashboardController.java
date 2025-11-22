package edu.icet.controller;

import edu.icet.model.Medicine;
import edu.icet.service.MedicineService;
import edu.icet.service.ReportService;
import edu.icet.util.QRCodeUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class DashboardController {

    // Constants for alerts
    private static final int LOW_STOCK_THRESHOLD = 10;
    private static final int EXPIRY_DAYS_THRESHOLD = 30;
    @Autowired
    private MedicineService medicineService;
    @Autowired
    private ApplicationContext springContext;
    @Autowired
    private ReportService reportService;
    @FXML
    private Button reportsButton;
    @FXML
    private Button manageUsersButton;
    @FXML
    private Button profileButton;
    // --- Low Stock Table ---
    @FXML
    private TableView<Medicine> lowStockTable;
    @FXML
    private TableColumn<Medicine, String> lowStockNameCol;
    @FXML
    private TableColumn<Medicine, Integer> lowStockQtyCol;
    // --- Expiry Table ---
    @FXML
    private TableView<Medicine> expiryTable;
    @FXML
    private TableColumn<Medicine, String> expiryNameCol;
    @FXML
    private TableColumn<Medicine, LocalDate> expiryDateCol;

    @FXML
    public void initialize() {
        // Setup Low Stock Table
        lowStockNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        lowStockQtyCol.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));

        // Setup Expiry Table
        expiryNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        expiryDateCol.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        // Load data into both tables
        loadAlerts();
    }

    private void loadAlerts() {
        // Load low stock medicines
        List<Medicine> lowStock = medicineService.getLowStockMedicines(LOW_STOCK_THRESHOLD);
        lowStockTable.setItems(FXCollections.observableArrayList(lowStock));

        // Load expiring medicines
        List<Medicine> expiring = medicineService.getMedicinesExpiringSoon(EXPIRY_DAYS_THRESHOLD);
        expiryTable.setItems(FXCollections.observableArrayList(expiring));
    }

    @FXML
    private void onManageMedicineClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MedicineManagement.fxml"));
            fxmlLoader.setControllerFactory(springContext::getBean); // Use Spring to get the controller
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Manage Medicines");
            stage.setScene(new Scene(root));

            // Use Modality.APPLICATION_MODAL to block interaction with the dashboard
            // until this new window is closed.
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait(); // Show the window and wait for it to be closed

            // After the modal is closed, refresh the dashboard alerts
            loadAlerts();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onReportsButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Inventory Report");
        fileChooser.setInitialFileName("MedSync_Inventory_Report.pdf");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        // Get the current stage to show the dialog
        Stage stage = (Stage) reportsButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                reportService.generateInventoryReport(file);

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Report Generated");
                alert.setHeaderText("Success!");
                alert.setContentText("Inventory report saved to: " + file.getAbsolutePath());
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
                // Show error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Report Error");
                alert.setHeaderText("Failed to generate report.");
                alert.setContentText("Error: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onManageUsersClick() {
        try {
            // Generate a sample QR code
            String invoiceId = "INV-2025-1116-001";
            Image qrImage = QRCodeUtil.generateQRCode(invoiceId, 200, 200);

            // Show it in an alert dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("QR Code Generator");
            alert.setHeaderText("Successfully generated QR Code for:");
            alert.setContentText(invoiceId);

            // Set the image in the alert
            ImageView imageView = new ImageView(qrImage);
            alert.setGraphic(imageView);

            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            // Handle error
        }
    }

    @FXML
    private void onProfileClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/UserProfile.fxml"));
            fxmlLoader.setControllerFactory(springContext::getBean);
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("User Profile");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadAlerts();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}