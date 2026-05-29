# Documentación de la API - Microservicio de Pedidos (Orders)

Este microservicio gestiona los pedidos y compras de la librería **Relatos de Papel**. Permite a los usuarios realizar pedidos, consultar el historial de órdenes recientes y actualizar el estado de los artículos individuales del pedido. También interactúa con el microservicio de **Catálogo** para validar o consultar información sobre los libros suministrados.

---

## Características Principales
* **Gestión de Órdenes:** Creación de nuevas órdenes conteniendo uno o múltiples libros y cantidades asociadas.
* **Historial de Pedidos:** Consulta de los pedidos realizados de manera reciente en el sistema.
* **Control de Estado de Items:** Actualización individual del estado de los libros pertenecientes a un pedido específico (ej: `EN_PROCESO`, `CANCELADO`, `ENTREGADO`, `DEVUELTO`).
* **Capa de Base de Datos Independiente:** Usa su propio esquema relacional `orders` sobre MySQL.

---

## Configuración y Puertos

* **Puerto Local por Defecto:** `8081` (Nota: Si se ejecuta junto al microservicio de Catálogo localmente, se recomienda cambiarlo para evitar colisiones, por ejemplo, utilizando `8082` mediante la variable de entorno `PORT=8082`).
* **Puerto en Docker Compose:** `8082`
* **Base de Datos Local:** `jdbc:mysql://localhost:3309/orders`
  * **Usuario:** `orders_user`
  * **Contraseña:** `orders_pass`
* **Registro en Eureka Server:** `http://localhost:8761/eureka`

---

## Endpoints de la API

Todas las rutas locales tienen el prefijo base `/api/v1/` (si accedes a través del Gateway, debes anteponer `/orders/api/v1/`).

### 1. Gestión de Pedidos (Orders)

| Método     | Endpoint                        | Descripción                                             | Request Body                                                 | Response                                                |
|:-----------|:--------------------------------|:--------------------------------------------------------|:-------------------------------------------------------------|:--------------------------------------------------------|
| **GET**    | `/api/v1/orders`                | Obtener listado de pedidos recientes.                   | Ninguno                                                      | `GetOrdersResponseDto`                                  |
| **POST**   | `/api/v1/orders`                | Crear y registrar una nueva orden de compra.            | `CreateOrderRequestDto` (JSON con libros y cantidades)       | `CreateOrderResponseDto` (HTTP 201 Created)             |
| **POST**   | `/api/v1/orders/user/{ownerId}` | Obtener listado de pedidos asociados a un id Cliente.   | Ninguno(JSON con "targetMethod":"GET", paginas y paginación) | `CreateOrderResGetOrdersOwnerResponseDto` (HTTP 200 Ok) |
| **DELETE** | `/api/v1/orders/id/{Id}`        | Borra las ordenes asociadas un  id de orden Especifica. | Ninguno (JSON con "targetMethod":"DELETE"                    | Ninguno (HTTP 204 Ok)                                   |

###Nota para el Caso de Delete, se usaría en Casos Excepcionales, ya que a nivel funcional no se deberían borrar ordénes

### 2. Gestión de Items del Pedido (Order Items)

| Método | Endpoint | Descripción | Request Body | Response |
| :--- | :--- | :--- | :--- | :--- |
| **PATCH** | `/api/v1/order-items` | Actualizar el estado de entrega o devolución de un item específico en una orden. | `UpdateOrderItemStatusDto` (JSON con id de item y estado) | `UpdateOrderItemStatusDto` (HTTP 200 OK) |

---

## Estados de los Items (`OrderStatus`)

Los items de un pedido pueden pasar por los siguientes estados:
* `EN_PROCESO` (Estado inicial)
* `CANCELADO`
* `ENTREGADO`
* `DEVUELTO`

---

## Modelos de Datos (DTOs)

### `CreateOrderRequestDto` (Creación de Pedido)
```json
{
  "supplies": [
    {
      "id": 1,
      "quantity": 2
    },
    {
      "id": 5,
      "quantity": 1
    }
  ]
}
```

### `CreateOrderResponseDto` (Respuesta de Creación)
```json
{
  "id": 10
}
```

### `UpdateOrderItemStatusDto` (Actualización de Estado de Item)
```json
{
  "id": 15,
  "status": "ENTREGADO"
}
```

### `GetOrdersResponseDto` (Ejemplo de listado de pedidos recientes)
```json
{
  "orders": [
    {
      "id": 10,
      "orderDate": "2026-05-27T16:00:00",
      "total": 55.50,
      "comment": "Entrega en horario de tarde",
      "ownerId": 1,
      "items": [
        {
          "id": 15,
          "catalogId": 1,
          "title": "Cien años de soledad",
          "quantity": 2,
          "subTotal": 37.00,
          "status": "ENTREGADO"
        },
        {
          "id": 16,
          "catalogId": 5,
          "title": "El Alquimista",
          "quantity": 1,
          "subTotal": 18.50,
          "status": "EN_PROCESO"
        }
      ]
    }
  ]
}

```
### `GetOrdersOwnerResponseDto` (listado de pedidos recientes id de Ciente)
```json
{
  "currentPage": 0,
  "ordersDetails": [
    {
      "comment": "Pruebas",
      "id": 17,
      "order_date": "2026-05-24T23:24:45",
      "total": 47,
      "updated_at": "2026-05-25T23:21:31"
    }
  ],
  "ownerId": 11,
  "pageSize": 0,
  "totalItems": 0,
  "totalPages": 0
}

```

### Entidades JPA

| Entidad     | Tabla         | Campos principales                                                   |
|-------------|---------------|----------------------------------------------------------------------|
| `Orders`    | `orders`      | `id`, `orderDate`, `total`, `comment`, `ownerId`, `updated_at`       |
| `OrderItem` | `order_item`  | `id`, `order_id` (FK), `id_catalog`,`status`, `quantity`, `subTotal` |

### `OrderStatus` (Enum)

```java
EN_PROCESO, CANCELADO, ENTREGADO
```

### Relaciones entre entidades

- **`Order` → `OrderItem`**: Relación **1:N** con `CascadeType.ALL` y `orphanRemoval = true`. Los ítems se persisten y eliminan automáticamente con la orden.
- **`OrderItem` → `Order`**: `@ManyToOne` (Lazy).

---

## Modelo relacional de base de datos

```
┌──────────────────────────────┐
│           orders             │
├──────────────────────────────┤
│ id          INTEGER (PK)     │──────┐        
│ order_date  TIMESTAMP        │      │
│ total       DECIMAL(10,2)    │      │
│ comment     TEXT             │      │     
│ owner_id    INTEGER          │      │
│ updated_at  TIMESTAMP        │      │
└──────────────────────────────┘      │
                                      │
                                 1:N  │
                                      ▼
                    ┌──────────────────────────────┐
                    │        order_item            │
                    ├──────────────────────────────┤
                    │ id           INTEGER (PK)    │
                    │ order_id     INTEGER (FK)    │
                    │ id_catalog   INTEGER         │  
                    │ status       ENUM            │
                    │ quantity     INTEGER (≥ 0)   │
                    │ sub_total    DECIMAL(10,2)   │
                    └──────────────────────────────┘
```

### Relaciones

- **`orders` → `order_item`**: Relación **1:N**. Cascade `ON DELETE CASCADE`. Cada orden puede tener múltiples ítems.
- **`order_item.id_catalogue`**: Referencia lógica al `supply.id` del microservicio de catálogo (no hay FK física, ya que están en bases de datos distintas).

---

## Reconstrucción de la base de datos para pruebas

El script SQL se encuentra en `src/main/resources/db/schema.sql`.

### Paso 1: Crear el esquema y las tablas

```bash
mysql -u root -p < src/main/resources/db/schema.sql
```

Este script:
- Crea el schema `orders`.
- Crea la tabla `orders`.
- Crea la tabla `order_item` con FK a `orders`,y con campo `status` de tipo ENUM. .

### Reconstrucción desde cero

```bash
mysql -u root -p -e "DROP SCHEMA IF EXISTS orders;" && \
mysql -u root -p < src/main/resources/db/schema.sql
```
---

## Configuración

Variables de entorno configurables (`application.yml`):

| Variable                  | Valor por defecto                    | Descripción                                  |
|---------------------------|--------------------------------------|----------------------------------------------|
| `DB_URL`                  | `jdbc:mysql://localhost:3307/orders` | URL de conexión JDBC (puerto 3307)           |
| `DB_DRIVER`               | `com.mysql.cj.jdbc.Driver`           | Driver JDBC                                  |
| `DB_USER`                 | `orders_user`                        | Usuario de base de datos                     |
| `DB_PASSWORD`             | `orders_pass`                        | Contraseña de base de datos                  |
| `EUREKA_URL`              | `http://localhost:8761/eureka`       | URL del servidor Eureka                      |
| `SUPPLIES_CATALOGUE_URL`  | `http://orders/api/v1`               | URL base del microservicio de catálogo (resuelta vía Eureka) |


