CREATE TABLE users
  (
    id    INTEGER PRIMARY KEY,
    name  VARCHAR2
  );

CREATE TABLE orders
  (
     orderId      INTEGER PRIMARY KEY,
     userId       INTEGER,
     date         VARCHAR2,
     FOREIGN KEY (userId) REFERENCES users(id)
  );

CREATE TABLE order_products(
    orderId      INTEGER REFERENCES orders(orderId),
    productId    INTEGER,
    productValue DECIMAL(10, 2)
);

