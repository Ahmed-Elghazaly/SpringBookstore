-- Run this AFTER schema.sql and triggers.sql

-- Default Admin Account
INSERT INTO admin (username, password, name)
VALUES ('admin', 'admin123', 'System Administrator');


-- Publishers
INSERT INTO publisher (name, address, phone_number)
VALUES ('Penguin Random House', '1745 Broadway, New York, NY 10019', '212-782-9000'),
       ('HarperCollins', '195 Broadway, New York, NY 10007', '212-207-7000'),
       ('Simon & Schuster', '1230 Avenue of the Americas, NY', '212-698-7000'),
       ('Oxford University Press', 'Great Clarendon Street, Oxford, UK', '+44-1865-556767'),
       ('Cambridge University Press', 'Cambridge CB2 8BS, UK', '+44-1223-358331'),
       ('Wiley', '111 River Street, Hoboken, NJ 07030', '201-748-6000'),
       ('National Geographic', '1145 17th Street NW, Washington DC', '202-857-7000'),
       ('Taschen', 'Hohenzollernring 53, Cologne, Germany', '+49-221-201800');

-- Authors
INSERT INTO author (name)
VALUES ('Stephen Hawking'),
       ('Carl Sagan'),
       ('Neil deGrasse Tyson'),
       ('Richard Dawkins'),
       ('Leonardo da Vinci'),
       ('Vincent van Gogh'),
       ('Pablo Picasso'),
       ('Karen Armstrong'),
       ('Reza Aslan'),
       ('Yuval Noah Harari'),
       ('David McCullough'),
       ('Doris Kearns Goodwin'),
       ('Jared Diamond'),
       ('Bill Bryson'),
       ('Simon Winchester');

-- Science Books
INSERT INTO book (isbn, title, publication_year, selling_price, stock_quantity, threshold_quantity, publisher_id,
                  category_name)
VALUES ('978-0553380163', 'A Brief History of Time', 1988, 18.99, 50, 10, 1, 'Science'),
       ('978-0345539434', 'Cosmos', 1980, 17.99, 45, 8, 1, 'Science'),
       ('978-0393609394', 'Astrophysics for People in a Hurry', 2017, 14.99, 60, 12, 2, 'Science'),
       ('978-0618056736', 'The Selfish Gene', 1976, 16.99, 35, 7, 4, 'Science'),
       ('978-0393355680', 'The Origin of Species', 1859, 15.99, 40, 8, 4, 'Science');

-- Art Books
INSERT INTO book (isbn, title, publication_year, selling_price, stock_quantity, threshold_quantity, publisher_id,
                  category_name)
VALUES ('978-3836562867', 'Leonardo da Vinci Complete Works', 2019, 59.99, 25, 5, 8, 'Art'),
       ('978-3836559591', 'Van Gogh: Complete Works', 2015, 49.99, 30, 6, 8, 'Art'),
       ('978-0714847344', 'The Art Book', 1994, 29.99, 40, 8, 2, 'Art'),
       ('978-0500203163', 'The Story of Art', 1950, 39.99, 35, 7, 2, 'Art'),
       ('978-3836560894', 'Picasso', 2018, 54.99, 20, 4, 8, 'Art');

-- Religion Books
INSERT INTO book (isbn, title, publication_year, selling_price, stock_quantity, threshold_quantity, publisher_id,
                  category_name)
VALUES ('978-0345391469', 'A History of God', 1993, 18.99, 45, 9, 1, 'Religion'),
       ('978-0812981605', 'Zealot: The Life of Jesus', 2013, 16.99, 50, 10, 1, 'Religion'),
       ('978-0061122002', 'The Great Transformation', 2006, 17.99, 35, 7, 2, 'Religion'),
       ('978-0060654672', 'Buddha', 2001, 15.99, 40, 8, 2, 'Religion'),
       ('978-0199535644', 'The World Religions', 2009, 24.99, 30, 6, 4, 'Religion');

-- History Books
INSERT INTO book (isbn, title, publication_year, selling_price, stock_quantity, threshold_quantity, publisher_id,
                  category_name)
VALUES ('978-0062316097', 'Sapiens: A Brief History', 2014, 24.99, 70, 15, 2, 'History'),
       ('978-0743226721', '1776', 2005, 18.99, 55, 10, 3, 'History'),
       ('978-1476748658', 'Team of Rivals', 2005, 22.99, 45, 9, 3, 'History'),
       ('978-0393354324', 'Guns, Germs, and Steel', 1997, 19.99, 50, 10, 4, 'History'),
       ('978-0767908184', 'A Short History of Nearly Everything', 2003, 18.99, 60, 12, 1, 'History');

-- Geography Books
INSERT INTO book (isbn, title, publication_year, selling_price, stock_quantity, threshold_quantity, publisher_id,
                  category_name)
VALUES ('978-0393352009', 'The Map That Changed the World', 2001, 16.99, 40, 8, 4, 'Geography'),
       ('978-0767903851', 'In a Sunburned Country', 2000, 15.99, 45, 9, 1, 'Geography'),
       ('978-1426217814', 'Atlas of the World', 2019, 79.99, 20, 4, 7, 'Geography'),
       ('978-0143126805', 'Prisoners of Geography', 2015, 17.99, 55, 10, 1, 'Geography'),
       ('978-0062466686', 'The Geography of Genius', 2016, 16.99, 35, 7, 3, 'Geography');


-- Author-Book Relationships

-- Science books
INSERT INTO author_book (author_id, book_isbn)
VALUES (1, '978-0553380163'),
       (2, '978-0345539434'),
       (3, '978-0393609394'),
       (4, '978-0618056736');

INSERT INTO author_book (author_id, book_isbn)
VALUES (5, '978-3836562867'),
       (6, '978-3836559591');

-- Religion books
INSERT INTO author_book (author_id, book_isbn)
VALUES (8, '978-0345391469'),
       (9, '978-0812981605'),
       (8, '978-0061122002'),
       (8, '978-0060654672');
-- Armstrong - Buddha

-- History books
INSERT INTO author_book (author_id, book_isbn)
VALUES (10, '978-0062316097'),
       (11, '978-0743226721'),
       (12, '978-1476748658'),
       (13, '978-0393354324'),
       (14, '978-0767908184');

-- Geography books
INSERT INTO author_book (author_id, book_isbn)
VALUES (15, '978-0393352009'),
       (14, '978-0767903851'),
       (14, '978-0062466686');


















INSERT INTO customer (username, password, first_name, last_name, email, shipping_address, phone_number)
VALUES
    ('a', 'a', 'a', 'a', 'a@a.com', '101 A St, City A', '555-0001'),
    ('b', 'b', 'b', 'b', 'b@b.com', '102 B St, City B', '555-0002'),
    ('c', 'c', 'c', 'c', 'c@c.com', '103 C St, City C', '555-0003'),
    ('d', 'd', 'd', 'd', 'd@d.com', '104 D St, City D', '555-0004'),
    ('e', 'e', 'e', 'e', 'e@e.com', '105 E St, City E', '555-0005'),
    ('f', 'f', 'f', 'f', 'f@f.com', '106 F St, City F', '555-0006');



-- (Last 4 Months: Sep, Oct, Nov, Dec 2025)
INSERT INTO "order" (order_id, customer_id, order_date) VALUES
-- MONTH 1: September 2025 (Oldest)
(1, 1, '2025-09-10'), -- Customer 'a' buys heavily
(2, 2, '2025-09-15'), -- Customer 'b' buys

-- MONTH 2: October 2025
(3, 1, '2025-10-05'), -- Customer 'a' buys again
(4, 3, '2025-10-12'), -- Customer 'c' buys
(5, 4, '2025-10-20'), -- Customer 'd' buys

-- MONTH 3: November 2025 (Previous Month for Reports)
(6, 1, '2025-11-02'), -- Customer 'a' big purchase
(7, 2, '2025-11-15'), -- Customer 'b'
(8, 5, '2025-11-20'), -- Customer 'e' buys once

-- MONTH 4: December 2025 (Current Month)
(9, 1, '2025-12-05'), -- Customer 'a' recent purchase
(10, 6, '2025-12-10'); -- Customer 'f' buys small item

-- Reset order sequence so new orders don't crash
SELECT setval('order_order_id_seq', (SELECT MAX(order_id) FROM "order"));


-- Top Selling Book Strategy:
-- '978-0062316097' (Sapiens) is bought frequently
-- '978-3836562867' (Da Vinci) is expensive (boosts top customer stats)

INSERT INTO order_book (order_id, book_isbn, quantity, price_at_purchase) VALUES
-- Order 1 (Sep - Cust A)
(1, '978-3836562867', 1, 59.99), -- Expensive Art Book
(1, '978-0062316097', 2, 24.99), -- Sapiens (History)

-- Order 2 (Sep - Cust B)
(2, '978-0553380163', 1, 18.99), -- Brief History of Time

-- Order 3 (Oct - Cust A)
(3, '978-3836559591', 1, 49.99), -- Van Gogh (Expensive)
(3, '978-0062316097', 1, 24.99), -- Sapiens again

-- Order 4 (Oct - Cust C)
(4, '978-0393609394', 3, 14.99), -- Astrophysics (Qty: 3)

-- Order 5 (Oct - Cust D)
(5, '978-0743226721', 1, 18.99), -- 1776

-- Order 6 (Nov - Cust A - Previous Month Sale)
(6, '978-3836562867', 2, 59.99), -- 2x Da Vinci ($120 purchase!)
(6, '978-0345391469', 1, 18.99), -- History of God

-- Order 7 (Nov - Cust B - Previous Month Sale)
(7, '978-0062316097', 1, 24.99), -- Sapiens
(7, '978-0393352009', 1, 16.99), -- Map That Changed the World

-- Order 8 (Nov - Cust E - Previous Month Sale)
(8, '978-0553380163', 1, 18.99), -- Brief History of Time

-- Order 9 (Dec - Cust A)
(9, '978-1476748658', 1, 22.99), -- Team of Rivals

-- Order 10 (Dec - Cust F)
(10, '978-0393609394', 1, 14.99); -- Astrophysics (Small purchase)