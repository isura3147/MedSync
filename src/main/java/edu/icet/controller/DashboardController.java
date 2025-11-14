package edu.icet.controller;

import edu.icet.model.Medicine;
import edu.icet.service.MedicineService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class DashboardController {

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private AnnotationConfigApplicationContext springContext;

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

    // Constants for alerts
    private static final int LOW_STOCK_THRESHOLD = 10;
    private static final int EXPIRY_DAYS_THRESHOLD = 30;

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
            // Load the FXML file for the medicine management screen
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MedicineManagement.fxml"));
            fxmlLoader.setControllerFactory(springContext::getBean); // Use Spring to get the controller
            Parent root = fxmlLoader.load();

            // Create a new stage (window) for the modal
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
}