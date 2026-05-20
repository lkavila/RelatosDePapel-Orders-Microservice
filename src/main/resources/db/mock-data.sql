USE orders;

INSERT INTO orders (id, order_date, total, comment, owner_id, created_at, updated_at) VALUES
(1, '2026-05-01 09:00:00', 32.00, 'Pedido de material de oficina', 1, '2026-05-01 09:00:00', '2026-05-01 09:00:00'),
(2, '2026-05-01 10:15:00', 24.00, 'Reposición para recepción', 2, '2026-05-01 10:15:00', '2026-05-01 10:15:00'),
(3, '2026-05-01 11:30:00', 34.25, 'Compra para almacén central', 3, '2026-05-01 11:30:00', '2026-05-01 11:30:00'),
(4, '2026-05-02 08:45:00', 29.50, 'Suministros para sucursal norte', 4, '2026-05-02 08:45:00', '2026-05-02 08:45:00'),
(5, '2026-05-02 12:00:00', 45.00, 'Pedido especial de papelería', 5, '2026-05-02 12:00:00', '2026-05-02 12:00:00'),
(6, '2026-05-03 09:20:00', 22.40, 'Material para atención al cliente', 5, '2026-05-03 09:20:00', '2026-05-03 09:20:00'),
(7, '2026-05-03 14:10:00', 32.00, 'Reabastecimiento de inventario', 5, '2026-05-03 14:10:00', '2026-05-03 14:10:00'),
(8, '2026-05-04 08:30:00', 42.80, 'Pedido de útiles escolares', 2, '2026-05-04 08:30:00', '2026-05-04 08:30:00'),
(9, '2026-05-04 16:45:00', 30.00, 'Compra para campaña editorial', 2, '2026-05-04 16:45:00', '2026-05-04 16:45:00'),
(10, '2026-05-05 10:05:00', 58.00, 'Pedido mixto para oficina principal', 1, '2026-05-05 10:05:00', '2026-05-05 10:05:00');

INSERT INTO order_item (order_id, id_catalog, status, quantity, sub_total) VALUES
(1, 1, 'EN_PROCESO', 2, 25.00),
(1, 2, 'EN_PROCESO', 1, 7.00),
(2, 3, 'ENTREGADO', 3, 24.00),
(3, 4, 'EN_PROCESO', 1, 15.75),
(3, 5, 'EN_PROCESO', 2, 18.50),
(4, 6, 'ENTREGADO', 4, 26.00),
(4, 7, 'ENTREGADO', 1, 3.50),
(5, 8, 'ENTREGADO', 2, 22.00),
(5, 9, 'DEVUELTO', 2, 23.00),
(6, 10, 'ENTREGADO', 1, 22.40),
(7, 11, 'EN_PROCESO', 5, 24.00),
(7, 12, 'EN_PROCESO', 1, 8.00),
(8, 13, 'CANCELADO', 2, 26.60),
(8, 14, 'ENTREGADO', 3, 16.20),
(9, 15, 'ENTREGADO', 1, 19.99),
(9, 16, 'ENTREGADO', 1, 10.01),
(10, 17, 'EN_PROCESO', 3, 42.00),
(10, 18, 'EN_PROCESO', 2, 15.00),
(10, 19, 'EN_PROCESO', 1, 1.00);

