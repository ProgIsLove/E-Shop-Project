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