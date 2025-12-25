-- =========================================
-- TRIGGER 1: prevent negative stock
-- =========================================

CREATE OR REPLACE FUNCTION prevent_negative_stock()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.stock_quantity < 0 THEN
        RAISE EXCEPTION
            'Stock quantity cannot be negative (book ISBN=%)',
            NEW.isbn;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_update_book_stock
    BEFORE UPDATE OF stock_quantity
    ON book
    FOR EACH ROW
EXECUTE FUNCTION prevent_negative_stock();

-- =========================================
-- TRIGGER 2: auto place publisher order
-- ONLY when stock DROPS below threshold
-- =========================================

CREATE OR REPLACE FUNCTION auto_place_publisher_order()
    RETURNS TRIGGER AS
$$
DECLARE
    new_order_id INTEGER;
BEGIN
    -- Check if stock dropped below threshold (crossing the boundary)
    IF NEW.stock_quantity < NEW.threshold_quantity
        AND OLD.stock_quantity >= OLD.threshold_quantity THEN

        -- Create the publisher order
        INSERT INTO PublisherOrder (order_date, order_quantity, status)
        VALUES (CURRENT_DATE, NEW.threshold_quantity * 2, 'Pending')
        RETURNING publisher_order_id INTO new_order_id;

        -- Link the book to this order with quantity and price
        INSERT INTO Publisher_Order_Book (publisher_order_id, book_isbn, quantity, price_at_order)
        VALUES (new_order_id, NEW.isbn, NEW.threshold_quantity * 2, NEW.selling_price);

        RAISE NOTICE 'Auto-created publisher order % for book %', new_order_id, NEW.isbn;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_update_book_stock
    AFTER UPDATE OF stock_quantity
    ON book
    FOR EACH ROW
EXECUTE FUNCTION auto_place_publisher_order();