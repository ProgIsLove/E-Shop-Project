CREATE TABLE settings
(
    `key` VARCHAR (128) NOT NULL PRIMARY KEY,
    value    VARCHAR(1024) NOT NULL,
    category VARCHAR(45)   NOT NULL
);