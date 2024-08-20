ALTER TABLE products
ADD FULLTEXT INDEX `products_fts` (name, short_description, full_description);