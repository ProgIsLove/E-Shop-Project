CREATE TABLE IF NOT EXISTS brands
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(45) NOT NULL UNIQUE,
    logo      VARCHAR(128)  NOT NULL,
    parent_id INT,
    CONSTRAINT chk_brands_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS brand_categories
(
    brand_id integer not null,
    category_id integer not null,
    primary key (brand_id, category_id)
);
