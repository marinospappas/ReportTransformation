DROP TABLE report IF EXISTS;

CREATE TABLE report  (
    report_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    contract_id VARCHAR(10),
    last_name VARCHAR(20),
    first_name VARCHAR(20),
    item_type VARCHAR(2),
    jurisdiction VARCHAR(10),
    end_date VARCHAR(20),
    agreement_number VARCHAR(20)
);