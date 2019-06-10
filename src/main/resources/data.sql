
INSERT INTO user (username, password, role, created_on) VALUES
('user', '$2a$10$ggOZSoLpYVNpmL0M9xVtp.e.cvk2copyXcYEZO4P8zhMOTrYqIGMO', 'user', CURRENT_TIMESTAMP),
('admin', '$2a$10$ggOZSoLpYVNpmL0M9xVtp.e.cvk2copyXcYEZO4P8zhMOTrYqIGMO', 'admin', CURRENT_TIMESTAMP);

INSERT INTO category (name, description, created_on) VALUES
('Meat', 'Fresh Meat', CURRENT_TIMESTAMP),
('Dairy', 'Dairy Products', CURRENT_TIMESTAMP),
('Canned', 'Canned food', CURRENT_TIMESTAMP),
('Confectionery', 'Sweets/confectionery', CURRENT_TIMESTAMP),
('Vegetables', 'Fresh produced Veges', CURRENT_TIMESTAMP);

INSERT INTO item (name, description, sku, dimensions, made_in, price, created_on) VALUES
('Coca Cola', 'Coca Cola 330ml can', '134243254', '10*5cm', 'Australia', 2.0, CURRENT_TIMESTAMP),
('Guylian Chocolate', 'Belgian Assorted Chocolate', '76394820', '10*15cm', 'Belgian', 15.0, CURRENT_TIMESTAMP),
('Full Cream Milk', 'Full Cream Milk 1L', '785675', '10*10cm', 'Belgian', 3.0, CURRENT_TIMESTAMP);

INSERT INTO item_category (item_id, category_id) VALUES
(1,3),
(2,2),
(2,4),
(3,2);