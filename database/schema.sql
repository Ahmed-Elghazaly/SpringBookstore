CREATE TABLE admin
(
    admin_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    name     VARCHAR(50) NOT NULL
);

CREATE TABLE publisher
(
    publisher_id SERIAL PRIMARY KEY,
    name         VARCHAR(50) NOT NULL UNIQUE, -- Publisher names must be unique
    address      VARCHAR(50),                 -- Optional, some publishers may not provide it
    phone_number VARCHAR(50)                  -- Optional, not unique
);

-- Using category_name as PK (NOT NULL + UNIQUE)
CREATE TABLE category
(
    category_name VARCHAR(50) PRIMARY KEY
);

-- Insert the 5 categories
INSERT INTO category (category_name)
VALUES ('Science'),
       ('Art'),
       ('Religion'),
       ('History'),
       ('Geography');


CREATE TABLE author
(
    author_id SERIAL PRIMARY KEY,
    name      VARCHAR(50) NOT NULL -- Name required, but NOT unique (John Smith can exist twice)
);


CREATE TABLE book
(
    isbn               VARCHAR(50) PRIMARY KEY,
    title              VARCHAR(50)    NOT NULL,                      -- Title required but not unique (two books can have same title)
    publication_year   INTEGER
        CHECK (publication_year IS NULL OR (publication_year >= 0)), -- Can be not set or set to a year >= 0
    selling_price      NUMERIC(10, 2) NOT NULL
        CHECK (selling_price > 0),                                   -- Price must be +ve
    stock_quantity     INTEGER        NOT NULL DEFAULT 0
        CHECK (stock_quantity >= 0),                                 -- Can't be -ve
    threshold_quantity INTEGER        NOT NULL
        CHECK (threshold_quantity >= 0),                             -- Can't be -ve
    publisher_id       INTEGER        NOT NULL
        REFERENCES publisher (publisher_id)
            ON DELETE RESTRICT,                                      -- Prevent deleting publisher with books
    category_name      VARCHAR(50)    NOT NULL
        REFERENCES category (category_name)
            ON DELETE RESTRICT                                       -- Prevent deleting category with books
);



-- author_book (M:N relationship)
-- Links authors to books (a book can have multiple authors)
CREATE TABLE author_book
(
    author_id INTEGER     NOT NULL
        REFERENCES author (author_id)
            ON DELETE RESTRICT, -- author can't be deleted if he has books
    book_isbn VARCHAR(50) NOT NULL
        REFERENCES book (isbn)
            ON DELETE CASCADE,  -- If book deleted, remove link
    PRIMARY KEY (author_id, book_isbn)
);


CREATE TABLE customer
(
    customer_id      SERIAL PRIMARY KEY,
    username         VARCHAR(50) NOT NULL UNIQUE, -- For login - must be unique
    password         VARCHAR(50) NOT NULL,        -- Required for authentication
    first_name       VARCHAR(50) NOT NULL,
    last_name        VARCHAR(50) NOT NULL,
    email            VARCHAR(50) NOT NULL UNIQUE, -- Must be unique
    phone_number     VARCHAR(50),                 -- Optional
    shipping_address VARCHAR(50) NOT NULL         -- Required for delivery, not unique (roommates)
);


-- Shopping cart items, directly linked to customer
CREATE TABLE cart_book
(
    customer_id INTEGER     NOT NULL
        REFERENCES customer (customer_id)
            ON DELETE CASCADE, -- If customer deleted, clear their cart
    book_isbn   VARCHAR(50) NOT NULL
        REFERENCES book (isbn)
            ON DELETE CASCADE, -- If book deleted, remove from carts
    quantity    INTEGER     NOT NULL
        CHECK (quantity >= 1), -- Must have at least 1
    PRIMARY KEY (customer_id, book_isbn)
);


-- Customer orders (completed purchases)
CREATE TABLE "order"
(
    order_id    SERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL
        REFERENCES customer (customer_id)
            ON DELETE RESTRICT, -- Don't delete customers with orders
    order_date  DATE    NOT NULL DEFAULT CURRENT_DATE
);


-- (M:N relationship)
-- Books in each order with quantity and price at time of purchase
CREATE TABLE order_book
(
    order_id          INTEGER        NOT NULL
        REFERENCES "order" (order_id)
            ON DELETE CASCADE,
    book_isbn         VARCHAR(50)    NOT NULL
        REFERENCES book (isbn)
            ON DELETE RESTRICT,        -- Don't delete books that were ordered
    quantity          INTEGER        NOT NULL
        CHECK (quantity >= 1),
    price_at_purchase NUMERIC(10, 2) NOT NULL
        CHECK (price_at_purchase > 0), -- Store price at time of order
    PRIMARY KEY (order_id, book_isbn)
);



-- Orders placed to publishers for restocking
-- 1 order = 1 book
CREATE TABLE publisher_order
(
    publisher_order_id SERIAL PRIMARY KEY,
    book_isbn          VARCHAR(50) NOT NULL
        REFERENCES book (isbn)
            ON DELETE RESTRICT, -- Don't delete books that has been ordered from publishers before
    order_date         DATE        NOT NULL DEFAULT CURRENT_DATE,
    quantity           INTEGER     NOT NULL
        CHECK (quantity >= 1),
    status             VARCHAR(50) NOT NULL DEFAULT 'Pending'
        CHECK (status IN ('Pending', 'Confirmed', 'Cancelled'))
);

