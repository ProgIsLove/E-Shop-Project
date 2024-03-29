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
    id        INT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(128) NOT NULL UNIQUE,
    alias     VARCHAR(64)  NOT NULL UNIQUE,
    image     VARCHAR(128) NOT NULL,
    enabled   BOOLEAN      NOT NULL,
    parent_id INT,
    FOREIGN KEY (parent_id) REFERENCES categories (id),
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES categories (id),
    CONSTRAINT chk_categories_enabled CHECK (enabled IN (0, 1)),
    CONSTRAINT chk_categories_alias UNIQUE (alias),
    CONSTRAINT chk_categories_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS persistent_logins
(
    username  VARCHAR(64) NOT NULL,
    series    VARCHAR(64) NOT NULL,
    token     VARCHAR(64) NOT NULL,
    last_used TIMESTAMP   NOT NULL,
    PRIMARY KEY (series)
);

CREATE UNIQUE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_first_name ON users(first_name);
CREATE INDEX idx_users_last_name ON users(last_name);
CREATE INDEX idx_users_enabled ON users(enabled);