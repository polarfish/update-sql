--  Changeset changelog.xml::1::nvoxland
CREATE TABLE person (id INT AUTO_INCREMENT NOT NULL, firstname VARCHAR(50) NULL, lastname VARCHAR(50) NOT NULL, state CHAR(2) NULL, CONSTRAINT PK_PERSON PRIMARY KEY (id));

--  Changeset changelog.xml::2::nvoxland
ALTER TABLE person ADD username VARCHAR(8) NULL;

--  Changeset changelog.xml::3::nvoxland
CREATE TABLE state AS SELECT DISTINCT state AS id FROM person WHERE state IS NOT NULL;

ALTER TABLE state MODIFY id CHAR(2) NOT NULL;

ALTER TABLE state ADD PRIMARY KEY (id);

ALTER TABLE person ADD CONSTRAINT FK_PERSON_STATE FOREIGN KEY (state) REFERENCES state (id);

