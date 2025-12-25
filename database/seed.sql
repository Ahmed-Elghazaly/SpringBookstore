-- =====================================================
-- GOLDEN BOOKS - SEED DATA
-- =====================================================
-- Run this AFTER schema_revised.sql and triggers_revised.sql
-- =====================================================

-- =====================================================
-- SEED: Default Admin Account
-- =====================================================
INSERT INTO admin (username, password, name) VALUES
    ('admin', 'admin123', 'System Administrator');

-- =====================================================
-- SEED: Publishers
-- =====================================================
INSERT INTO publisher (name, address, phone_number) VALUES
                                                        ('Penguin Random House', '1745 Broadway, New York, NY 10019', '212-782-9000'),
                                                        ('HarperCollins', '195 Broadway, New York, NY 10007', '212-207-7000'),
                                                        ('Simon & Schuster', '1230 Avenue of the Americas, NY', '212-698-7000'),
                                                        ('Oxford University Press', 'Great Clarendon Street, Oxford, UK', '+44-1865-556767'),
                                                        ('Cambridge University Press', 'Cambridge CB2 8BS, UK', '+44-1223-358331'),
                                                        ('Wiley', '111 River Street, Hoboken, NJ 07030', '201-748-6000'),
                                                        ('National Geographic', '1145 17th Street NW, Washington DC', '202-857-7000'),
                                                        ('Taschen', 'Hohenzollernring 53, Cologne, Germany', '+49-221-201800');

-- =====================================================
-- SEED: Authors
-- =====================================================
INSERT INTO author (name) VALUES
                              ('Stephen Hawking'),
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

-- =====================================================
-- SEED: Books (using the 5 hardcoded categories)
-- =====================================================

-- Science Books
INSERT INTO book (isbn, title, publication_year, selling_price, stock_quantity, threshold_quantity, publisher_id, category_name) VALUES
                                                                                                                                     ('978-0553380163', 'A Brief History of Time', 1988, 18.99, 50, 10, 1, 'Science'),
                                                                                                                                     ('978-0345539434', 'Cosmos', 1980, 17.99, 45, 8, 1, 'Science'),
                                                                                                                                     ('978-0393609394', 'Astrophysics for People in a Hurry', 2017, 14.99, 60, 12, 2, 'Science'),
                                                                                                                                     ('978-0618056736', 'The Selfish Gene', 1976, 16.99, 35, 7, 4, 'Science'),
                                                                                                                                     ('978-0393355680', 'The Origin of Species', 1859, 15.99, 40, 8, 4, 'Science');

-- Art Books
INSERT INTO book (isbn, title, publication_year, selling_price, stock_quantity, threshold_quantity, publisher_id, category_name) VALUES
                                                                                                                                     ('978-3836562867', 'Leonardo da Vinci Complete Works', 2019, 59.99, 25, 5, 8, 'Art'),
                                                                                                                                     ('978-3836559591', 'Van Gogh: Complete Works', 2015, 49.99, 30, 6, 8, 'Art'),
                                                                                                                                     ('978-0714847344', 'The Art Book', 1994, 29.99, 40, 8, 2, 'Art'),
                                                                                                                                     ('978-0500203163', 'The Story of Art', 1950, 39.99, 35, 7, 2, 'Art'),
                                                                                                                                     ('978-3836560894', 'Picasso', 2018, 54.99, 20, 4, 8, 'Art');

-- Religion Books
INSERT INTO book (isbn, title, publication_year, selling_price, stock_quantity, threshold_quantity, publisher_id, category_name) VALUES
                                                                                                                                     ('978-0345391469', 'A History of God', 1993, 18.99, 45, 9, 1, 'Religion'),
                                                                                                                                     ('978-0812981605', 'Zealot: The Life of Jesus', 2013, 16.99, 50, 10, 1, 'Religion'),
                                                                                                                                     ('978-0061122002', 'The Great Transformation', 2006, 17.99, 35, 7, 2, 'Religion'),
                                                                                                                                     ('978-0060654672', 'Buddha', 2001, 15.99, 40, 8, 2, 'Religion'),
                                                                                                                                     ('978-0199535644', 'The World Religions', 2009, 24.99, 30, 6, 4, 'Religion');

-- History Books
INSERT INTO book (isbn, title, publication_year, selling_price, stock_quantity, threshold_quantity, publisher_id, category_name) VALUES
                                                                                                                                     ('978-0062316097', 'Sapiens: A Brief History', 2014, 24.99, 70, 15, 2, 'History'),
                                                                                                                                     ('978-0743226721', '1776', 2005, 18.99, 55, 10, 3, 'History'),
                                                                                                                                     ('978-1476748658', 'Team of Rivals', 2005, 22.99, 45, 9, 3, 'History'),
                                                                                                                                     ('978-0393354324', 'Guns, Germs, and Steel', 1997, 19.99, 50, 10, 4, 'History'),
                                                                                                                                     ('978-0767908184', 'A Short History of Nearly Everything', 2003, 18.99, 60, 12, 1, 'History');

-- Geography Books
INSERT INTO book (isbn, title, publication_year, selling_price, stock_quantity, threshold_quantity, publisher_id, category_name) VALUES
                                                                                                                                     ('978-0393352009', 'The Map That Changed the World', 2001, 16.99, 40, 8, 4, 'Geography'),
                                                                                                                                     ('978-0767903851', 'In a Sunburned Country', 2000, 15.99, 45, 9, 1, 'Geography'),
                                                                                                                                     ('978-1426217814', 'Atlas of the World', 2019, 79.99, 20, 4, 7, 'Geography'),
                                                                                                                                     ('978-0143126805', 'Prisoners of Geography', 2015, 17.99, 55, 10, 1, 'Geography'),
                                                                                                                                     ('978-0062466686', 'The Geography of Genius', 2016, 16.99, 35, 7, 3, 'Geography');

-- =====================================================
-- SEED: Author-Book Relationships
-- =====================================================

-- Science books
INSERT INTO author_book (author_id, book_isbn) VALUES
                                                   (1, '978-0553380163'),  -- Hawking - Brief History
                                                   (2, '978-0345539434'),  -- Sagan - Cosmos
                                                   (3, '978-0393609394'),  -- Tyson - Astrophysics
                                                   (4, '978-0618056736');  -- Dawkins - Selfish Gene

-- Art books (using author_id for da Vinci, van Gogh, Picasso)
INSERT INTO author_book (author_id, book_isbn) VALUES
                                                   (5, '978-3836562867'),  -- da Vinci - Complete Works
                                                   (6, '978-3836559591');  -- van Gogh - Complete Works

-- Religion books
INSERT INTO author_book (author_id, book_isbn) VALUES
                                                   (8, '978-0345391469'),  -- Armstrong - History of God
                                                   (9, '978-0812981605'),  -- Aslan - Zealot
                                                   (8, '978-0061122002'),  -- Armstrong - Great Transformation
                                                   (8, '978-0060654672');  -- Armstrong - Buddha

-- History books
INSERT INTO author_book (author_id, book_isbn) VALUES
                                                   (10, '978-0062316097'), -- Harari - Sapiens
                                                   (11, '978-0743226721'), -- McCullough - 1776
                                                   (12, '978-1476748658'), -- Goodwin - Team of Rivals
                                                   (13, '978-0393354324'), -- Diamond - Guns Germs Steel
                                                   (14, '978-0767908184'); -- Bryson - Short History

-- Geography books
INSERT INTO author_book (author_id, book_isbn) VALUES
                                                   (15, '978-0393352009'), -- Winchester - Map That Changed
                                                   (14, '978-0767903851'), -- Bryson - Sunburned Country
                                                   (14, '978-0062466686'); -- Bryson - Geography of Genius (actually Eric Weiner, but for demo)

-- =====================================================
-- SEED: Test Customer
-- =====================================================
INSERT INTO customer (username, password, first_name, last_name, email, phone_number, shipping_address) VALUES
                                                                                                            ('testuser', 'test123', 'John', 'Doe', 'john.doe@example.com', '555-123-4567', '123 Main Street, New York, NY 10001'),
                                                                                                            ('janedoe', 'jane123', 'Jane', 'Doe', 'jane.doe@example.com', '555-987-6543', '456 Oak Avenue, Los Angeles, CA 90001'),
                                                                                                            ('bobsmith', 'bob123', 'Bob', 'Smith', 'bob.smith@example.com', NULL, '789 Pine Road, Chicago, IL 60601');

