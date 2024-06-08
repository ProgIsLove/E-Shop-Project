CREATE TABLE IF NOT EXISTS product
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(256) NOT NULL UNIQUE,
    alias     VARCHAR(256)  NOT NULL UNIQUE,
    short_description VARCHAR(512) NOT NULL,
    full_description VARCHAR(4096) NOT NULL,
    created_time DATETIME,
    modified_time DATETIME,
    enabled   BOOLEAN DEFAULT TRUE,
    in_stock BOOLEAN DEFAULT TRUE,
    cost FLOAT DEFAULT 0,
    price FLOAT DEFAULT 0,
    discount_percent FLOAT DEFAULT 0,
    length FLOAT DEFAULT 0,
    width FLOAT DEFAULT 0,
    height FLOAT DEFAULT 0,
    weight FLOAT DEFAULT 0,
    category_id INT,
    brand_id INT,
    CONSTRAINT chk_products_enabled CHECK (enabled IN (0, 1))
);