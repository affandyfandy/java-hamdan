CREATE TABLE IF NOT EXISTS "user" (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255)
);

INSERT INTO "user" (username, password)
VALUES ('testuser', 'password123');