CREATE TABLE currency
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(64) NOT NULL,
    symbol VARCHAR(3)  NOT NULL,
    code   VARCHAR(4)  NOT NULL
);