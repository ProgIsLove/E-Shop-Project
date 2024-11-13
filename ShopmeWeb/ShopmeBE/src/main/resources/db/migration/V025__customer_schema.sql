CREATE TABLE IF NOT EXISTS customers
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(45) NOT NULL UNIQUE ,
    password VARCHAR(64) NOT NULL,
    first_name VARCHAR(45) NOT NULL,
    last_name VARCHAR(45) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    address_line_1 VARCHAR(64) NOT NULL,
    address_line_2 VARCHAR(64),
    city VARCHAR(45) NOT NULL,
    state VARCHAR(45) NOT NULL,
    postal_code VARCHAR(10) NOT NULL,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    enabled BOOLEAN NOT NULL,
    verification_code VARCHAR(64),
    country_id INT,
    FOREIGN KEY (country_id) REFERENCES countries(id),
    CONSTRAINT chk_customer_enabled CHECK (enabled IN (0, 1))
)