-- =====================================================
-- GOLDEN BOOKS - DATABASE TRIGGERS
-- =====================================================
-- These triggers implement business logic that requires
-- cross-table operations or complex conditions that
-- cannot be expressed with simple CHECK constraints
-- =====================================================

-- =====================================================
-- TRIGGER 1: prevent_negative_stock
-- =====================================================
-- Purpose: Ensure stock never goes negative
-- Note: We have CHECK (stock_quantity >= 0) in schema,
--       but this trigger provides a better error message
-- =====================================================

CREATE OR REPLACE FUNCTION prevent_negative_stock()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.stock_quantity < 0 THEN
        RAISE EXCEPTION 'Insufficient stock for book "%". Available: %, Requested reduction would result in: %',
            NEW.title,
            OLD.stock_quantity,
            NEW.stock_quantity;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_prevent_negative_stock
    BEFORE UPDATE OF stock_quantity
    ON book
    FOR EACH ROW
EXECUTE FUNCTION prevent_negative_stock();


-- =====================================================
-- TRIGGER 2: auto_place_publisher_order
-- =====================================================
-- Purpose: Automatically create a publisher order when
--          stock DROPS BELOW the threshold quantity
-- Key Logic: Only triggers when CROSSING the threshold
--            (was above/at threshold, now below)
--            This prevents infinite loops and duplicate orders
-- =====================================================

CREATE OR REPLACE FUNCTION auto_place_publisher_order()
    RETURNS TRIGGER AS
$$
BEGIN
    -- Only trigger when stock CROSSES below threshold
    -- OLD.stock >= threshold AND NEW.stock < threshold
    IF OLD.stock_quantity >= OLD.threshold_quantity
        AND NEW.stock_quantity < NEW.threshold_quantity THEN

        -- Create publisher order for 2x threshold quantity
        INSERT INTO publisher_order (book_isbn, order_date, quantity, status)
        VALUES (NEW.isbn,
                CURRENT_DATE,
                NEW.threshold_quantity * 2, -- Order double the threshold
                'Pending');

        RAISE NOTICE 'Auto-created publisher order for book "%" (ISBN: %). Quantity ordered: %',
            NEW.title, NEW.isbn, NEW.threshold_quantity * 2;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_auto_publisher_order
    AFTER UPDATE OF stock_quantity
    ON book
    FOR EACH ROW
EXECUTE FUNCTION auto_place_publisher_order();


-- =====================================================
-- TRIGGER 3: update_stock_on_order_confirm
-- =====================================================
-- Purpose: When a publisher order is confirmed,
--          automatically increase the book's stock
-- =====================================================

CREATE OR REPLACE FUNCTION update_stock_on_order_confirm()
    RETURNS TRIGGER AS
$$
BEGIN
    -- Only act when status changes TO 'Confirmed'
    IF OLD.status = 'Pending' AND NEW.status = 'Confirmed' THEN
        UPDATE book
        SET stock_quantity = stock_quantity + NEW.quantity
        WHERE isbn = NEW.book_isbn;

        RAISE NOTICE 'Stock updated for book ISBN %. Added % units.',
            NEW.book_isbn, NEW.quantity;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_stock_on_confirm
    AFTER UPDATE OF status
    ON publisher_order
    FOR EACH ROW
EXECUTE FUNCTION update_stock_on_order_confirm();


-- =====================================================
-- SUMMARY OF TRIGGERS
-- =====================================================
--
-- 1. trg_prevent_negative_stock (BEFORE UPDATE on book)
--    - Prevents stock from going negative
--    - Provides helpful error message
--
-- 2. trg_auto_publisher_order (AFTER UPDATE on book)
--    - Creates publisher order when stock drops below threshold
--    - Only fires once when crossing the threshold
--
-- 3. trg_update_stock_on_confirm (AFTER UPDATE on publisher_order)
--    - Increases stock when order status changes to Confirmed
--    - Simplifies the confirmation process
--
-- =====================================================