-- TechShop schema (limpio para MySQL Workbench)
DROP DATABASE IF EXISTS techshop;
CREATE DATABASE techshop CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE techshop;

-- Usuarios de BD (opcional; requiere privilegios de root)
DROP USER IF EXISTS 'usuario_prueba'@'%';
DROP USER IF EXISTS 'usuario_reportes'@'%';
CREATE USER 'usuario_prueba'@'%' IDENTIFIED BY 'Usuar1o_Clave.';
CREATE USER 'usuario_reportes'@'%' IDENTIFIED BY 'Usuar1o_Reportes.';
GRANT ALL PRIVILEGES ON techshop.* TO 'usuario_prueba'@'%';
GRANT SELECT ON techshop.* TO 'usuario_reportes'@'%';
FLUSH PRIVILEGES;

create table categoria (
  id_categoria INT NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(30) NOT NULL,
  ruta_imagen varchar(1024),
  activo bool,
  PRIMARY KEY (id_categoria))
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

create table producto (
  id_producto INT NOT NULL AUTO_INCREMENT,
  id_categoria INT NOT NULL,
  descripcion VARCHAR(30) NOT NULL,  
  detalle VARCHAR(1600) NOT NULL, 
  precio double,
  existencias int,  
  ruta_imagen varchar(1024),
  activo bool,
  PRIMARY KEY (id_producto),
  foreign key fk_producto_caregoria (id_categoria) references categoria(id_categoria)  
)
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

CREATE TABLE usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  username varchar(20) NOT NULL,
  password varchar(512) NOT NULL,
  nombre VARCHAR(20) NOT NULL,
  apellidos VARCHAR(30) NOT NULL,
  correo VARCHAR(75) NULL,
  telefono VARCHAR(15) NULL,
  ruta_imagen varchar(1024),
  activo boolean,
  PRIMARY KEY (`id_usuario`))
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

create table rol (
  id_rol INT NOT NULL AUTO_INCREMENT,
  nombre varchar(20),
  id_usuario int,
  PRIMARY KEY (id_rol)
)
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

CREATE TABLE ruta (
    id_ruta INT AUTO_INCREMENT NOT NULL,
    patron VARCHAR(255) NOT NULL,
    rol_name VARCHAR(50) NOT NULL,
	PRIMARY KEY (id_ruta))
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

CREATE TABLE ruta_permit (
    id_ruta INT AUTO_INCREMENT NOT NULL,
    patron VARCHAR(255) NOT NULL,
	PRIMARY KEY (id_ruta))
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

CREATE TABLE constante (
    id_constante INT AUTO_INCREMENT NOT NULL,
    atributo VARCHAR(25) NOT NULL,
    valor VARCHAR(150) NOT NULL,
	PRIMARY KEY (id_constante))
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

create table factura (
  id_factura INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  fecha date,  
  total double,
  estado int,
  PRIMARY KEY (id_factura),
  foreign key fk_factura_usuario (id_usuario) references usuario(id_usuario)  
)
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

create table venta (
  id_venta INT NOT NULL AUTO_INCREMENT,
  id_factura INT NOT NULL,
  id_producto INT NOT NULL,
  precio double, 
  cantidad int,
  PRIMARY KEY (id_venta),
  foreign key fk_ventas_factura (id_factura) references factura(id_factura),
  foreign key fk_ventas_producto (id_producto) references producto(id_producto) 
)
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;

-- Datos mínimos de ejemplo
INSERT INTO categoria (descripcion, ruta_imagen, activo) VALUES
('Monitores','',true),
('Teclados','',true);

INSERT INTO producto (id_categoria, descripcion, detalle, precio, existencias, ruta_imagen, activo) VALUES
(1,'Monitor 24"','Full HD 75Hz',89900,15,'',true),
(2,'Teclado mecánico','Switches azules',35000,40,'',true);

-- Usuario admin (password=123456 en bcrypt)
INSERT INTO usuario (username, password, nombre, apellidos, correo, telefono, ruta_imagen, activo) VALUES
('admin','$2b$10$UwQPYh54X28seqIR0KYHYOJVu2XNi3g84jNzx1f1A8bAOnsJ4G9n2','Admin','General','admin@demo.com','00000000','',true);

INSERT INTO rol (nombre, id_usuario) VALUES
('ADMIN', 1);

INSERT INTO ruta (patron, rol_name) VALUES
('/producto/**','ADMIN'),
('/categoria/**','ADMIN'),
('/usuario/**','ADMIN');

INSERT INTO ruta_permit (patron) VALUES
('/','/login');
