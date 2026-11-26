INSERT INTO users (user_id, username, password, first_name, last_name, email, role, department_id, deleted,
                   created_at, updated_at, language, initial, operator_id)
VALUES (1101, 'e2e.mcp.manager@manager.local', '$2y$10$BR2Q1zrLZka3GHcKxEv3TumUwKrt2Z5MOROGtRlhIqHEQeQxWnjFq',
        'Manager', 'Mcp', 'e2e.mcp.manager@manager.local', 'MANAGER', 1, false, now(), now(), 'pl', false, 1),
       (1102, 'e2e.mcp.user@manager.local', '$2y$10$BR2Q1zrLZka3GHcKxEv3TumUwKrt2Z5MOROGtRlhIqHEQeQxWnjFq',
        'User', 'Mcp', 'e2e.mcp.user@manager.local', 'USER', 1, false, now(), now(), 'pl', false, 1),
       (1103, 'e2e.mcp.revoke@manager.local', '$2y$10$BR2Q1zrLZka3GHcKxEv3TumUwKrt2Z5MOROGtRlhIqHEQeQxWnjFq',
        'Revoke', 'Mcp', 'e2e.mcp.revoke@manager.local', 'MANAGER', 1, false, now(), now(), 'pl', false, 1)
ON CONFLICT DO NOTHING;

INSERT INTO operator_subscriptions (subscription_id, operator_id, plan, start_date, end_date, updated_by, updated_at)
VALUES ('5b0c2f3e-7c1d-4e8a-9f21-0e2d3c4b5a61', 1, 'ENTERPRISE', current_date - 1, current_date + 365, 'e2e', now())
ON CONFLICT DO NOTHING;
