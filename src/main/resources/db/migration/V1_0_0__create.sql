-- DROP TABLE IF EXISTS todo;
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE todo (id uuid PRIMARY KEY DEFAULT uuid_generate_v4(), task VARCHAR(255), status boolean);
