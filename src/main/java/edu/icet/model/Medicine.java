package edu.icet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "medicines")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "batch_number", nullable = false)
    private String batchNumber;

    @Column(name = "quantity_in_stock", nullable = false)
    private int quantityInStock;

    @Column(name = "unit_price", nullable = false)
    private double unitPrice;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    private String supplier;

}