-- Datos de prueba para User Management Service
-- Estos datos se cargarán en PostgreSQL Testcontainers durante los tests

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    curp VARCHAR(18) PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellido_paterno VARCHAR(50),
    apellido_materno VARCHAR(50),
    email VARCHAR(100),
    email_personal VARCHAR(100),
    telefono VARCHAR(15),
    dependencia VARCHAR(100),
    puesto VARCHAR(100),
    nivel_organizacional INTEGER,
    jefe_inmediato_curp VARCHAR(18),
    status_usuario VARCHAR(20) DEFAULT 'ACTIVO',
    fecha_ingreso DATE,
    fecha_baja DATE,
    motivo_baja VARCHAR(200),
    activo BOOLEAN DEFAULT true,
    ultimo_acceso TIMESTAMP,
    intentos_fallidos INTEGER DEFAULT 0,
    cuenta_bloqueada BOOLEAN DEFAULT false,
    fecha_bloqueo TIMESTAMP,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por VARCHAR(18),
    actualizado_por VARCHAR(18),
    FOREIGN KEY (jefe_inmediato_curp) REFERENCES users(curp)
);

-- Tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(200),
    dependencia VARCHAR(100),
    nivel VARCHAR(20) DEFAULT 'OPERATIVO',
    activo BOOLEAN DEFAULT true,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por VARCHAR(18),
    actualizado_por VARCHAR(18),
    UNIQUE(nombre, dependencia)
);

-- Tabla de permisos
CREATE TABLE IF NOT EXISTS permissions (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(200),
    recurso VARCHAR(100) NOT NULL,
    accion VARCHAR(20) NOT NULL,
    sistema VARCHAR(50),
    activo BOOLEAN DEFAULT true,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por VARCHAR(18),
    actualizado_por VARCHAR(18),
    UNIQUE(nombre, recurso)
);

-- Tabla de relación usuario-rol
CREATE TABLE IF NOT EXISTS user_roles (
    user_curp VARCHAR(18),
    role_id BIGINT,
    PRIMARY KEY (user_curp, role_id),
    FOREIGN KEY (user_curp) REFERENCES users(curp),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Tabla de relación rol-permiso
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id BIGINT,
    permission_id BIGINT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

-- Insertar usuarios de prueba
INSERT INTO users (curp, nombres, apellido_paterno, apellido_materno, email, dependencia, puesto, status_usuario, fecha_ingreso, creado_por, actualizado_por) VALUES
('AAAA800101HTABCD01', 'Juan Carlos', 'García', 'López', 'juan.garcia@tabasco.gob.mx', 'CGMAIG', 'Coordinador de TI', 'ACTIVO', '2020-01-15', 'SYSTEM', 'SYSTEM'),
('BBBB850215MTABCD02', 'María Elena', 'Martínez', 'Hernández', 'maria.martinez@tabasco.gob.mx', 'Secretaría de Finanzas', 'Analista de Sistemas', 'ACTIVO', '2019-03-10', 'SYSTEM', 'SYSTEM'),
('CCCC900301HTABCD03', 'Roberto', 'Pérez', 'Sánchez', 'roberto.perez@tabasco.gob.mx', 'CGMAIG', 'Desarrollador', 'ACTIVO', '2021-06-01', 'SYSTEM', 'SYSTEM'),
('DDDD750720MTABCD04', 'Ana Patricia', 'González', 'Rodríguez', 'ana.gonzalez@tabasco.gob.mx', 'Secretaría de Salud', 'Administradora de BD', 'ACTIVO', '2018-09-20', 'SYSTEM', 'SYSTEM');

-- Insertar roles de prueba
INSERT INTO roles (nombre, descripcion, dependencia, nivel, creado_por, actualizado_por) VALUES
('ADMIN_CGMAIG', 'Administrador de CGMAIG', 'CGMAIG', 'ADMIN_DEPENDENCIA', 'SYSTEM', 'SYSTEM'),
('DESARROLLADOR', 'Desarrollador de Sistemas', 'CGMAIG', 'OPERATIVO', 'SYSTEM', 'SYSTEM'),
('ANALISTA_FINANZAS', 'Analista de Sistemas Financieros', 'Secretaría de Finanzas', 'OPERATIVO', 'SYSTEM', 'SYSTEM'),
('ADMIN_BD', 'Administrador de Base de Datos', 'Secretaría de Salud', 'SUPERVISOR', 'SYSTEM', 'SYSTEM');

-- Insertar permisos de prueba
INSERT INTO permissions (nombre, descripcion, recurso, accion, sistema, creado_por, actualizado_por) VALUES
('USER_READ', 'Leer información de usuarios', 'users', 'READ', 'USER_MANAGEMENT', 'SYSTEM', 'SYSTEM'),
('USER_CREATE', 'Crear nuevos usuarios', 'users', 'CREATE', 'USER_MANAGEMENT', 'SYSTEM', 'SYSTEM'),
('USER_UPDATE', 'Actualizar información de usuarios', 'users', 'UPDATE', 'USER_MANAGEMENT', 'SYSTEM', 'SYSTEM'),
('USER_DELETE', 'Eliminar usuarios', 'users', 'DELETE', 'USER_MANAGEMENT', 'SYSTEM', 'SYSTEM'),
('ROLE_READ', 'Leer información de roles', 'roles', 'READ', 'USER_MANAGEMENT', 'SYSTEM', 'SYSTEM'),
('ROLE_CREATE', 'Crear nuevos roles', 'roles', 'CREATE', 'USER_MANAGEMENT', 'SYSTEM', 'SYSTEM'),
('ROLE_ADMIN', 'Administrar roles completo', 'roles', 'ADMIN', 'USER_MANAGEMENT', 'SYSTEM', 'SYSTEM'),
('SYSTEM_ADMIN', 'Administración completa del sistema', 'system', 'ADMIN', 'USER_MANAGEMENT', 'SYSTEM', 'SYSTEM');

-- Asignar roles a usuarios
INSERT INTO user_roles (user_curp, role_id) VALUES
('AAAA800101HTABCD01', 1), -- Juan García - ADMIN_CGMAIG
('BBBB850215MTABCD02', 3), -- María Martínez - ANALISTA_FINANZAS
('CCCC900301HTABCD03', 2), -- Roberto Pérez - DESARROLLADOR
('DDDD750720MTABCD04', 4); -- Ana González - ADMIN_BD

-- Asignar permisos a roles
INSERT INTO role_permissions (role_id, permission_id) VALUES
-- ADMIN_CGMAIG tiene permisos de administración
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7),
-- DESARROLLADOR tiene permisos básicos
(2, 1), (2, 5),
-- ANALISTA_FINANZAS tiene permisos de lectura
(3, 1), (3, 5),
-- ADMIN_BD tiene permisos de administración de usuarios
(4, 1), (4, 2), (4, 3), (4, 5);