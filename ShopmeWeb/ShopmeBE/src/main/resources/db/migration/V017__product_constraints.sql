ALTER TABLE product_images
    ADD CONSTRAINT fk_product_image_product
        FOREIGN KEY (product_id)
            REFERENCES products(id);

ALTER TABLE products
    ADD CONSTRAINT fk_products_category
        FOREIGN KEY (category_id)
            REFERENCES categories(id);

ALTER TABLE products
    ADD CONSTRAINT fk_products_brand
        FOREIGN KEY (brand_id)
            REFERENCES brands(id);

ALTER TABLE products
    ADD CONSTRAINT chk_products_dimensions
        CHECK (length >= 0 AND width >= 0 AND height >= 0 AND weight >= 0);

ALTER TABLE products
    ADD CONSTRAINT chk_products_discount_percent
        CHECK (discount_percent BETWEEN 0 AND 100);

ALTER TABLE products
    ADD CONSTRAINT chk_products_price
        CHECK (price >= 0);

ALTER TABLE products
    ADD CONSTRAINT chk_products_cost
        CHECK (cost  >= 0);

ALTER TABLE products
    ADD CONSTRAINT chk_products_in_stock
        CHECK (in_stock IN (0, 1));

ALTER TABLE products
    ADD CONSTRAINT uc_products_name
        UNIQUE (name);

ALTER TABLE products
    ADD CONSTRAINT uc_products_alias
        UNIQUE (alias);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email
        UNIQUE (email);