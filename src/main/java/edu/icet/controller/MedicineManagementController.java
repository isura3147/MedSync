package edu.icet.controller;

import edu.icet.model.Medicine;
import edu.icet.service.MedicineService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class MedicineManagementController {

    @FXML
    private TableView<Medicine> medicineTable;

    @FXML
    private TableColumn<Medicine, Long> idColumn;

    @FXML
    private TableColumn<Medicine, String> nameColumn;

    @FXML
    private TableColumn<Medicine, String> batchNumberColumn;

    @FXML
    private TableColumn<Medicine, Integer> quantityColumn;

    @FXML
    private TableColumn<Medicine, Double> priceColumn;

    @FXML
    private TableColumn<Medicine, LocalDate> expiryDateColumn;

    @FXML
    private TableColumn<Medicine, String> supplierColumn;

    @Autowired
    private MedicineService medicineService;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        batchNumberColumn.setCellValueFactory(new PropertyValueFactory<>("batchNumber"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        expiryDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));

        // Load the medicine data from the service
        loadMedicines();
    }

    // Helper method to load/refresh data
    private void loadMedicines() {
        medicineTable.getItems().clear(); // Clear existing data
        List<Medicine> medicines = medicineService.getAllMedicines();
        medicineTable.setItems(FXCollections.observableArrayList(medicines));
    }

    @FXML
    private void onRefreshButtonClick() {
        loadMedicines();
    }

    @FXML
    private void onDeleteButtonClick() {
        Medicine selectedMedicine = medicineTable.getSelectionModel().getSelectedItem();

        if (selectedMedicine == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a medicine to delete.");
            return;
        }

        // Show confirmation dialog
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Medicine: " + selectedMedicine.getName());
        confirmation.setContentText("Are you sure you want to delete this medicine?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                medicineService.deleteMedicine(selectedMedicine.getId());
                loadMedicines();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete medicine: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onAddButtonClick() {
        // Open a new dialog window for adding a medicine
        showAlert(Alert.AlertType.INFORMATION, "Not Implemented", "The 'Add' feature will be implemented in a later step.");
    }

    @FXML
    private void onEditButtonClick() {
        // Open a new dialog window with the selected medicine's data
        showAlert(Alert.AlertType.INFORMATION, "Not Implemented", "The 'Edit' feature will be implemented in a later step.");
    }

    // Helper method for showing alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}