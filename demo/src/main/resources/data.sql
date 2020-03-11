DROP TABLE IF EXISTS user;

CREATE TABLE user (
  user_id INT AUTO_INCREMENT  PRIMARY KEY,
  uid VARCHAR(32) NOT NULL UNIQUE,
  name NVARCHAR(256) NOT NULL,
  update_date TIMESTAMP,
  insert_date TIMESTAMP
);

DROP TABLE IF EXISTS customer;

CREATE TABLE customer (
  customer_id INT AUTO_INCREMENT PRIMARY KEY,
  customer_name NVARCHAR(256) NOT NULL,
  customer_type VARCHAR(1) NOT NULL, -- A- khách hàng Vip, B - khách thàng thân thiết, C - Khách hàng thường, D - Khách hàng mới
  balance DECIMAL(16,2),
  phone VARCHAR(16) NOT NULL,
  email NVARCHAR(256),
  address NVARCHAR(256),
  status BOOLEAN, -- A- Active, I- Inactive
  account_number NVARCHAR(256),
  gender VARCHAR(1), -- M- nam, F- nữ
  update_date TIMESTAMP,
  insert_date TIMESTAMP
);
