CREATE TABLE countries
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    code VARCHAR(5)  NOT NULL UNIQUE
);


CREATE TABLE states
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(45) NOT NULL,
    country_id INT,
    FOREIGN KEY (country_id) REFERENCES countries (id)
);