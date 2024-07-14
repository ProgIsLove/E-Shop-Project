CREATE TABLE IF NOT EXISTS product_image
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    value      VARCHAR(255) NOT NULL,
    product_id INT,
    CONSTRAINT FK_product_detail_product FOREIGN KEY (product_id) REFERENCES products(id)
);

