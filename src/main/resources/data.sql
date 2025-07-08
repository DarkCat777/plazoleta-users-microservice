-- Script de inicialización para entorno de pruebas (H2)|

MERGE INTO roles (id, nombre, descripcion) VALUES
    (1, 'ADMINISTRATOR', 'Usuario con acceso total al sistema.'),
    (2, 'OWNER', 'Propietario de un centro u organización.'),
    (3, 'EMPLOYEE', 'Empleado que trabaja para un propietario.'),
    (4, 'CUSTOMER', 'Usuario cliente del sistema.');

MERGE INTO usuarios (
    id, nombre, apellido, numero_documento, celular,
    fecha_nacimiento, correo, clave, id_role
) VALUES (
    1, 'Admin', 'Principal', '00000000', '999999999',
    '1990-01-01', 'admin@pragma.com',
    '$2a$12$f5v6nYeKzgiKjrxwx2xYf.LDHb7Hihe/uRJFlGyOwwvYEQkHShDv6', -- "adminPassword123"
    1
);