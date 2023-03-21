DROP TABLE report IF EXISTS;

CREATE TABLE report  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    item_type VARCHAR(2)
);