CREATE SCHEMA invoicedb2;

USE invoicedb2;

-- Create table for customers
CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(20)
);

CREATE TABLE cashier (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    price DECIMAL(10, 2)
);

CREATE TABLE invoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    cashier_id INT,
    amount DECIMAL(10, 2),
    created_date DATE,
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (cashier_id) REFERENCES cashier(id)
);

CREATE TABLE invoice_detail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    quantity INT,
    product_id INT,
    product_price DECIMAL(10, 2),
    invoice_id INT,
    amount DECIMAL(10, 2) GENERATED ALWAYS AS (quantity * product_price) STORED,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (invoice_id) REFERENCES invoice(id)
);

-- Insert mock data into customer table
INSERT INTO customer (name, phone) VALUES
('John Doe', '123-456-7890'),
('Jane Smith', '987-654-3210'),
('Alice Johnson', '555-555-5555');

-- Insert mock data into cashier table
INSERT INTO cashier (name) VALUES
('Michael Brown'),
('Sarah Davis'),
('Emily White');

-- Insert mock data into product table
INSERT INTO product (name, price) VALUES
('Product A', 10.00),
('Product B', 15.50),
('Product C', 7.25);

-- Insert mock data into invoice table
INSERT INTO invoice (customer_id, cashier_id, amount, created_date) VALUES
(1, 1, 0.00, '2024-06-01'),
(2, 2, 0.00, '2024-06-02'),
(3, 3, 0.00, '2024-06-03');

-- Insert mock data into invoice_detail table
INSERT INTO invoice_detail (quantity, product_id, product_price, invoice_id) VALUES
(2, 1, 10.00, 1),
(1, 2, 15.50, 1),
(3, 3, 7.25, 2),
(1, 1, 10.00, 3),
(4, 2, 15.50, 3);

SET SQL_SAFE_UPDATES = 0;

-- Update invoice amounts
UPDATE invoice
SET amount = (SELECT SUM(amount) FROM invoice_detail WHERE invoice_id = invoice.id);

SET SQL_SAFE_UPDATES = 1;

CREATE TABLE revenue_report (
    id INT AUTO_INCREMENT PRIMARY KEY,
    year INT,
    month INT,
    day INT,
    amount DECIMAL(10, 2)
);

CREATE VIEW customer_purchases AS
SELECT 
    c.id AS customer_id,
    c.name AS customer_name,
    p.id AS product_id,
    p.name AS product_name,
    id.quantity,
    id.amount,
    i.created_date
FROM 
    invoice_detail id
JOIN 
    invoice i ON id.invoice_id = i.id
JOIN 
    customer c ON i.customer_id = c.id
JOIN 
    product p ON id.product_id = p.id;
    
DELIMITER //

CREATE FUNCTION calculate_revenue_by_cashier(input_cashier_id INT) 
RETURNS DECIMAL(10, 2)
READS SQL DATA
BEGIN
    DECLARE total_revenue DECIMAL(10, 2);
    
    SELECT SUM(i.amount) INTO total_revenue
    FROM invoice i
    WHERE i.cashier_id = input_cashier_id;
    
    RETURN total_revenue;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE calculate_revenue_of_day(input_year INT, input_day_of_year INT)
BEGIN
    DECLARE total_revenue DECIMAL(10, 2);
    
    SELECT SUM(amount) INTO total_revenue
    FROM invoice
    WHERE YEAR(created_date) = input_year AND DAYOFYEAR(created_date) = input_day_of_year;
    
    INSERT INTO revenue_report (year, month, day, amount)
    VALUES (input_year, NULL, input_day_of_year, total_revenue);
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE calculate_revenue_of_month(input_year INT, input_month INT)
BEGIN
    DECLARE total_revenue DECIMAL(10, 2);
    
    SELECT SUM(amount) INTO total_revenue
    FROM invoice
    WHERE YEAR(created_date) = input_year AND MONTH(created_date) = input_month;
    
    INSERT INTO revenue_report (year, month, day, amount)
    VALUES (input_year, input_month, NULL, total_revenue);
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE calculate_revenue_of_year(input_year INT)
BEGIN
    DECLARE total_revenue DECIMAL(10, 2);
    
    SELECT SUM(amount) INTO total_revenue
    FROM invoice
    WHERE YEAR(created_date) = input_year;
    
    INSERT INTO revenue_report (year, month, day, amount)
    VALUES (input_year, NULL, NULL, total_revenue);
END //

DELIMITER ;

CALL calculate_revenue_of_day(2024, 15);

CALL calculate_revenue_of_month(2024, 6);

CALL calculate_revenue_of_year(2024);
