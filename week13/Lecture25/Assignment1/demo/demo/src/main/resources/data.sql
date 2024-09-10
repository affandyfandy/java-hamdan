CREATE TABLE IF NOT EXISTS employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(255),
    salary DECIMAL(10, 2)
);

INSERT INTO employee (name, phone, email, salary)
VALUES
    ('Alice', '123-456-7890', 'alice@example.com', 50000),
    ('Bob', '234-567-8901', 'bob@example.com', 60000),
    ('Charlie', '345-678-9012', 'charlie@example.com', 55000),
    ('David', '456-789-0123', 'david@example.com', 62000);
