-- Changeset changelog.yml::1::nvoxland
CREATE TABLE person (id INTEGER NOT NULL, firstname VARCHAR2(50), lastname VARCHAR2(50) NOT NULL, state CHAR(2), CONSTRAINT PK_PERSON PRIMARY KEY (id));

-- Changeset changelog.yml::2::nvoxland
ALTER TABLE person ADD username VARCHAR2(8);

-- Changeset changelog.yml::3::nvoxland
CREATE TABLE state AS SELECT DISTINCT state AS id FROM person WHERE state IS NOT NULL;

ALTER TABLE state ADD PRIMARY KEY (id);

ALTER TABLE person ADD CONSTRAINT FK_PERSON_STATE FOREIGN KEY (state) REFERENCES state (id);
