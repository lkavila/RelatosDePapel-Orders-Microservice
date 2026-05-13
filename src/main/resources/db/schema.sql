-- =====================================================
-- DDL para crear el esquema completo de Orders
-- =====================================================

-- Creacion de schema
CREATE SCHEMA IF NOT EXISTS orders;
USE orders;

-- Tabla principal de órdenes
CREATE TABLE orders (
                        id INTEGER NOT NULL AUTO_INCREMENT,
                        order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        total DECIMAL(10,2) NOT NULL,
                        comment TEXT,
                        owner_id INTEGER NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (id)
);

-- Tabla de ítems de órdenes
CREATE TABLE order_item (
                            id INTEGER NOT NULL AUTO_INCREMENT,
                            order_id INTEGER NOT NULL,
                            id_catalog INTEGER NOT NULL,
                            status ENUM('EN_PROCESO', 'CANCELADO', 'ENTREGADO', 'DEVUELTO') NOT NULL DEFAULT 'EN_PROCESO',
                            quantity INTEGER NOT NULL CHECK (quantity >= 0),
                            sub_total DECIMAL(10,2) NOT NULL,
                            PRIMARY KEY (id),
                            CONSTRAINT fk_order_item_order_id
                                FOREIGN KEY (order_id) REFERENCES orders(id)
                                    ON DELETE CASCADE ON UPDATE CASCADE
);