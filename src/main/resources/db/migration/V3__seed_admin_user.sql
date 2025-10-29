-- Crear usuario admin: fcaminos@puente.com / puente
-- BCrypt hash de "puente"
INSERT INTO users(name, email, password_hash, created_at, updated_at)
VALUES ('Franco Caminos', 'fcaminos@puente.com', '$2b$12$WNLkafsG1D5rPXELbeKKJuFP65umuqo2jXXloOtXYFw3BWRbBrsJi', NOW(), NOW());

-- Asignar rol ADMIN al usuario (role_id=1 es ADMIN según V2__seed_instruments.sql)
INSERT INTO user_roles(user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'fcaminos@puente.com' AND r.name = 'ADMIN';

-- También asignar rol USER para que pueda hacer todo
INSERT INTO user_roles(user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'fcaminos@puente.com' AND r.name = 'USER';

