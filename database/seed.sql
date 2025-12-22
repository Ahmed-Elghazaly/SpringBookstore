-- ============================
-- SEED DEFAULT ADMIN
-- ============================

INSERT INTO Admin (admin_id, username, password, name)
VALUES (1,
        'system_admin',
        'system_admin_password',
        'System Admin')
ON CONFLICT (admin_id) DO NOTHING;