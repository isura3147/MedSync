package edu.icet.service;

import edu.icet.config.AppConfig;
import edu.icet.model.Medicine;
import edu.icet.repository.MedicineRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class}) // Load Spring config
@Transactional // Roll back changes after each test
public class MedicineServiceTest {

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private MedicineRepository medicineRepository;

    @BeforeEach
    public void setUp() {
        // Clear the repo before each test to ensure isolation
        medicineRepository.deleteAll();

        // Create test data
        Medicine lowStockItem = new Medicine();
        lowStockItem.setName("Aspirin");
        lowStockItem.setBatchNumber("A100");
        lowStockItem.setQuantityInStock(5); // <-- Low stock
        lowStockItem.setUnitPrice(10.0);
        lowStockItem.setExpiryDate(LocalDate.now().plusYears(1));

        Medicine highStockItem = new Medicine();
        highStockItem.setName("Paracetamol");
        highStockItem.setBatchNumber("P200");
        highStockItem.setQuantityInStock(100); // <-- High stock
        highStockItem.setUnitPrice(8.0);
        highStockItem.setExpiryDate(LocalDate.now().plusYears(1));

        medicineRepository.save(lowStockItem);
        medicineRepository.save(highStockItem);
    }

    @Test
    public void testGetLowStockMedicines() {
        // Define the threshold
        int threshold = 10;

        // Call the service method that is being tested
        List<Medicine> lowStockList = medicineService.getLowStockMedicines(threshold);

        // Verify the results
        Assertions.assertNotNull(lowStockList, "The returned list should not be null.");
        Assertions.assertEquals(1, lowStockList.size(), "The list should contain exactly one item.");
        Assertions.assertEquals("Aspirin", lowStockList.get(0).getName(), "The item name should be 'Aspirin'.");
    }

    @AfterEach
    public void tearDown() {
        medicineRepository.deleteAll();
    }
}