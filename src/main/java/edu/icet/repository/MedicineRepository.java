package edu.icet.repository;

import edu.icet.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    /**
     * Finds all medicines with a quantity in stock less than the specified threshold.
     */
    List<Medicine> findByQuantityInStockLessThan(int threshold);

    /**
     * Finds all medicines that will expire between two dates.
     */
    List<Medicine> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);
}