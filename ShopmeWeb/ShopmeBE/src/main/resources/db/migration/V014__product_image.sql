CREATE TABLE IF NOT EXISTS product_image
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    product_id INT
);

ALTER TABLE products
MODIFY COLUMN main_image VARCHAR(255) NOT NULL;