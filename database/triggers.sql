-- =========================================
-- TRIGGER: prevent negative stock before update
-- =========================================

CREATE OR REPLACE FUNCTION prevent_negative_stock()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.stock_quantity < 0 THEN
        RAISE EXCEPTION 'Stock quantity cannot be negative (book ISBN=%)', NEW.isbn;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_update_book_stock
    BEFORE UPDATE OF stock_quantity
    ON Book
    FOR EACH ROW
EXECUTE FUNCTION prevent_negative_stock();

-- =========================================
-- TRIGGER: auto place publisher order after drop below threshold
-- =========================================

CREATE OR REPLACE FUNCTION auto_place_publisher_order()
    RETURNS TRIGGER AS
$$
DECLARE
    new_order_id INTEGER;
BEGIN
    IF NEW.stock_quantity < NEW.threshold_quantity THEN
        INSERT INTO PublisherOrder (admin_id,
                                    publisher_id,
                                    order_date,
                                    order_quantity,
                                    status)
        VALUES (1,
                NEW.publisher_id,
                CURRENT_DATE,
                NEW.threshold_quantity * 2,
                'Pending')
        RETURNING publisher_order_id INTO new_order_id;

        INSERT INTO PublisherOrder_Book (publisher_order_id,
                                         book_isbn)
        VALUES (new_order_id, NEW.isbn);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_update_book_stock
    AFTER UPDATE OF stock_quantity
    ON Book
    FOR EACH ROW
EXECUTE FUNCTION auto_place_publisher_order();