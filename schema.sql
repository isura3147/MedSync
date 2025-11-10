-- Create a new database (if it doesn't exist)
CREATE DATABASE IF NOT EXISTS medsync;
USE medsync;

-- Table for User accounts
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL, -- Storing BCrypt hash
    role VARCHAR(50) NOT NULL,      -- e.g., 'Admin', 'Pharmacist'
    profile_pic_url VARCHAR(1024)       -- URL from AWS S3
);

-- Table for Medicines
CREATE TABLE IF NOT EXISTS medicines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    batch_number VARCHAR(100) NOT NULL,
    quantity_in_stock INT NOT NULL DEFAULT 0,
    unit_price DECIMAL(10, 2) NOT NULL,
    expiry_date DATE NOT NULL,
    supplier VARCHAR(255)
);

-- Table for Invoices (Sales)
CREATE TABLE IF NOT EXISTS invoices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255),
    total_amount DECIMAL(10, 2) NOT NULL,
    invoice_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    pharmacist_id BIGINT,

    FOREIGN KEY (pharmacist_id) REFERENCES users(id)
);

-- Join Table for items in an invoice (Many-to-Many between Invoices and Medicines)
CREATE TABLE IF NOT EXISTS invoice_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_id BIGINT NOT NULL,
    medicine_id BIGINT NOT NULL,
    quantity_sold INT NOT NULL,
    price_at_sale DECIMAL(10, 2) NOT NULL, -- Price when it was sold

    FOREIGN KEY (invoice_id) REFERENCES invoices(id),
    FOREIGN KEY (medicine_id) REFERENCES medicines(id)
);

-- Add a default admin user (password is 'admin123')
-- The hash was generated using a BCrypt generator for 'admin123'
INSERT INTO users (username, password_hash, role)
VALUES ('admin', '$2a$10$3z/t.1S.X.1K5.E.E.qg7u.V.1K5.E.E.qg7u.V.1K5.E.E.qg7', 'Admin')
ON DUPLICATE KEY UPDATE username = 'admin';