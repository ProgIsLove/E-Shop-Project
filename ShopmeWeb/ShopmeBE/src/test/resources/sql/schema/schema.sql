CREATE TABLE IF NOT EXISTS users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(128) NOT NULL UNIQUE,
    enabled    BOOLEAN      NOT NULL,
    first_name VARCHAR(45)  NOT NULL,
    last_name  VARCHAR(45)  NOT NULL,
    password   VARCHAR(64)  NOT NULL,
    photos     VARCHAR(64)  NULL,
    CONSTRAINT chk_users_enabled CHECK (enabled IN (0, 1))
);

CREATE TABLE IF NOT EXISTS roles
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(40)  NOT NULL UNIQUE,
    description VARCHAR(150) NOT NULL,
    CONSTRAINT uk_roles_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id integer not null,
    role_id integer not null,
    primary key (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id             INT PRIMARY KEY AUTO_INCREMENT,
    name           VARCHAR(128) NOT NULL UNIQUE,
    alias          VARCHAR(64)  NOT NULL UNIQUE,
    image          VARCHAR(128) NOT NULL,
    enabled        BOOLEAN      NOT NULL,
    parent_id      INT,
    all_parent_ids VARCHAR(256) null,
    FOREIGN KEY (parent_id) REFERENCES categories (id),
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES categories (id),
    CONSTRAINT chk_categories_enabled CHECK (enabled IN (0, 1)),
    CONSTRAINT chk_categories_alias UNIQUE (alias),
    CONSTRAINT chk_categories_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS brands
(
    id   INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(45)  NOT NULL UNIQUE,
    logo VARCHAR(128) NOT NULL,
    CONSTRAINT chk_brands_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS brand_categories
(
    brand_id    integer not null,
    category_id integer not null,
    primary key (brand_id, category_id)
);

CREATE TABLE IF NOT EXISTS products
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    name              VARCHAR(256)  NOT NULL UNIQUE,
    alias             VARCHAR(256)  NOT NULL UNIQUE,
    short_description VARCHAR(512)  NOT NULL,
    full_description  VARCHAR(4096) NOT NULL,
    created_time      DATETIME,
    updated_time      DATETIME,
    enabled           BOOLEAN DEFAULT TRUE,
    in_stock          BOOLEAN DEFAULT TRUE,
    cost              FLOAT   DEFAULT 0,
    price             FLOAT   DEFAULT 0,
    discount_percent  FLOAT   DEFAULT 0,
    length            FLOAT   DEFAULT 0,
    width             FLOAT   DEFAULT 0,
    height            FLOAT   DEFAULT 0,
    weight            FLOAT   DEFAULT 0,
    category_id       INT,
    brand_id          INT,
    main_image        VARCHAR(255)  NOT NULL,
    CONSTRAINT chk_products_enabled CHECK (enabled IN (0, 1))
);

CREATE TABLE IF NOT EXISTS product_details
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    value      VARCHAR(255) NOT NULL,
    product_id INT,
    CONSTRAINT FK_product_detail_product FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE IF NOT EXISTS product_images
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    product_id INT
);

CREATE TABLE IF NOT EXISTS settings
(
    `key`    VARCHAR(128)  NOT NULL PRIMARY KEY,
    value    VARCHAR(1024) NOT NULL,
    category VARCHAR(45)   NOT NULL
);

CREATE TABLE IF NOT EXISTS currency
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(64) NOT NULL,
    symbol VARCHAR(3)  NOT NULL,
    code   VARCHAR(4)  NOT NULL
);

CREATE TABLE IF NOT EXISTS countries
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    code VARCHAR(5)  NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS states
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(45) NOT NULL,
    country_id INT,
    FOREIGN KEY (country_id) REFERENCES countries (id)
);

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