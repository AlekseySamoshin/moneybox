drop table if exists money_transfers;
drop table if exists moneyboxes; -- дроп таблиц, чтобы избежать конфликтов в тестовых данных при перезапуске

CREATE TABLE IF NOT EXISTS moneyboxes (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	sum BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS money_transfers (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	increase BOOLEAN,
	moneybox_id BIGINT NOT NULL REFERENCES moneyboxes(id),
	sum BIGINT NOT NULL
);

INSERT INTO moneyboxes (id, sum) VALUES
	 (1, 1000);