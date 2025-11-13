package edu.icet.service;

import edu.icet.model.Medicine;
import edu.icet.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    /**
     * Retrieves all medicines from the database.
     * @return A list of all Medicine objects.
     */
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    /**
     * Finds a single medicine by its ID.
     * @param id The ID of the medicine.
     * @return An Optional containing the medicine if found, or empty if not.
     */
    public Optional<Medicine> findMedicineById(Long id) {
        return medicineRepository.findById(id);
    }

    /**
     * Adds a new medicine to the database.
     * @param medicine The Medicine object to be saved.
     * @return The saved Medicine object (with its generated ID).
     */
    public Medicine addMedicine(Medicine medicine) {
        // add validation logic
        return medicineRepository.save(medicine);
    }

    /**
     * Updates an existing medicine.
     * @param updatedMedicine The medicine object with updated fields.
     * @return The updated Medicine object.
     * @throws RuntimeException if the medicine with the given ID is not found.
     */
    public Medicine updateMedicine(Medicine updatedMedicine) {
        // Find the existing medicine by ID
        Medicine existingMedicine = medicineRepository.findById(updatedMedicine.getId())
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + updatedMedicine.getId()));

        // Apply the updates
        existingMedicine.setName(updatedMedicine.getName());
        existingMedicine.setBatchNumber(updatedMedicine.getBatchNumber());
        existingMedicine.setQuantityInStock(updatedMedicine.getQuantityInStock());
        existingMedicine.setUnitPrice(updatedMedicine.getUnitPrice());
        existingMedicine.setExpiryDate(updatedMedicine.getExpiryDate());
        existingMedicine.setSupplier(updatedMedicine.getSupplier());

        // Save the updated entity
        return medicineRepository.save(existingMedicine);
    }

    /**
     * Deletes a medicine from the database by its ID.
     * @param id The ID of the medicine to delete.
     * @throws RuntimeException if the medicine with the given ID is not found.
     */
    public void deleteMedicine(Long id) {
        if (!medicineRepository.existsById(id)) {
            throw new RuntimeException("Medicine not found with id: " + id);
        }
        medicineRepository.deleteById(id);
    }
}
