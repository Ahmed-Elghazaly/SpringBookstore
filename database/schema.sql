-- ============================
-- TABLE: Author
-- ============================
CREATE TABLE Author
(
    author_id SERIAL PRIMARY KEY,
    name      VARCHAR(255) NOT NULL
);

-- ============================
-- TABLE: Publisher
-- ============================
CREATE TABLE Publisher
(
    publisher_id SERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    address      VARCHAR(500),
    phone_number VARCHAR(50)
);

-- ============================
-- TABLE: Category
-- ============================
CREATE TABLE Category
(
    category_name VARCHAR(100) PRIMARY KEY
);

-- ============================
-- TABLE: Book
-- ============================
CREATE TABLE Book
(
    isbn               VARCHAR(20) PRIMARY KEY,
    title              VARCHAR(255)   NOT NULL,
    publication_year   INTEGER CHECK (publication_year >= 0),
    selling_price      NUMERIC(12, 2) NOT NULL CHECK (selling_price >= 0),
    stock_quantity     INTEGER        NOT NULL CHECK (stock_quantity >= 0),
    threshold_quantity INTEGER        NOT NULL CHECK (threshold_quantity >= 0),
    publisher_id       INTEGER        NOT NULL,
    category_name      VARCHAR(100)   NOT NULL,
    FOREIGN KEY (publisher_id) REFERENCES Publisher (publisher_id),
    FOREIGN KEY (category_name) REFERENCES Category (category_name)
);

-- ============================
-- RELATIONSHIP TABLE: Author_Book
-- ============================
CREATE TABLE Author_Book
(
    author_id INTEGER     NOT NULL,
    book_isbn VARCHAR(20) NOT NULL,
    PRIMARY KEY (author_id, book_isbn),
    FOREIGN KEY (author_id) REFERENCES Author (author_id),
    FOREIGN KEY (book_isbn) REFERENCES Book (isbn)
);

-- ============================
-- TABLE: Customer
-- ============================
CREATE TABLE Customer
(
    customer_id      SERIAL PRIMARY KEY,
    username         VARCHAR(100) UNIQUE NOT NULL,
    password         VARCHAR(255)        NOT NULL,
    first_name       VARCHAR(100)        NOT NULL,
    last_name        VARCHAR(100)        NOT NULL,
    email            VARCHAR(255) UNIQUE NOT NULL CHECK (POSITION('@' IN email) > 1),
    phone            VARCHAR(50),
    shipping_address VARCHAR(500)        NOT NULL
);

-- ============================
-- TABLE: Admin
-- ============================
CREATE TABLE Admin
(
    admin_id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    name     VARCHAR(255)        NOT NULL
);

-- ============================
-- TABLE: "Order"
-- ============================
CREATE TABLE "Order"
(
    order_id    SERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL,
    order_date  DATE    NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer (customer_id)
);

-- ============================
-- RELATIONSHIP TABLE: Order_Book
-- ============================
CREATE TABLE Order_Book
(
    order_id          INTEGER        NOT NULL,
    book_isbn         VARCHAR(20)    NOT NULL,
    quantity          INTEGER        NOT NULL CHECK (quantity >= 1),
    price_at_purchase NUMERIC(12, 2) NOT NULL CHECK (price_at_purchase >= 0),
    PRIMARY KEY (order_id, book_isbn),
    FOREIGN KEY (order_id) REFERENCES "Order" (order_id),
    FOREIGN KEY (book_isbn) REFERENCES Book (isbn)
);

-- ============================
-- TABLE: ShoppingCart
-- ============================
CREATE TABLE ShoppingCart
(
    cart_id     SERIAL PRIMARY KEY,
    customer_id INTEGER UNIQUE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer (customer_id)
);

-- ============================
-- RELATIONSHIP TABLE: Cart_Book
-- ============================
CREATE TABLE Cart_Book
(
    cart_id   INTEGER     NOT NULL,
    book_isbn VARCHAR(20) NOT NULL,
    quantity  INTEGER     NOT NULL CHECK (quantity >= 1),
    PRIMARY KEY (cart_id, book_isbn),
    FOREIGN KEY (cart_id) REFERENCES ShoppingCart (cart_id),
    FOREIGN KEY (book_isbn) REFERENCES Book (isbn)
);

-- ============================
-- TABLE: PublisherOrder
-- ============================
CREATE TYPE order_status AS ENUM ('Pending', 'Confirmed', 'Cancelled');

CREATE TABLE PublisherOrder
(
    publisher_order_id SERIAL PRIMARY KEY,
    admin_id           INTEGER      NOT NULL,
    publisher_id       INTEGER      NOT NULL,
    order_date         DATE         NOT NULL,
    order_quantity     INTEGER      NOT NULL CHECK (order_quantity >= 1),
    status             order_status NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES Admin (admin_id),
    FOREIGN KEY (publisher_id) REFERENCES Publisher (publisher_id)
);

-- ============================
-- RELATIONSHIP TABLE: PublisherOrder_Book
-- ============================
CREATE TABLE PublisherOrder_Book
(
    publisher_order_id INTEGER     NOT NULL,
    book_isbn          VARCHAR(20) NOT NULL,
    PRIMARY KEY (publisher_order_id, book_isbn),
    FOREIGN KEY (publisher_order_id) REFERENCES PublisherOrder (publisher_order_id),
    FOREIGN KEY (book_isbn) REFERENCES Book (isbn)
);