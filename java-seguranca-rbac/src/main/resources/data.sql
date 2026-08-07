INSERT INTO tb_role (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO tb_role (id, name) VALUES (2, 'ROLE_USER');

INSERT INTO tb_user (id, username, password, enabled) VALUES (1, 'admin', '123', true); -- Senha SEM criptografia
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);

INSERT INTO tb_user (id, username, password, enabled) VALUES (2, 'user', '456', true); -- Senha SEM criptografia
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);
