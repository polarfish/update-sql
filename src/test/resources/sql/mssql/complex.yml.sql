-- Changeset changelog.yml::1::nvoxland
CREATE TABLE person (id int IDENTITY (1, 1) NOT NULL, firstname varchar(50), lastname varchar(50) NOT NULL, state char(2), CONSTRAINT PK_PERSON PRIMARY KEY (id))
GO

-- Changeset changelog.yml::2::nvoxland
ALTER TABLE person ADD username varchar(8)
GO

-- Changeset changelog.yml::3::nvoxland
SELECT DISTINCT state AS id INTO state FROM person WHERE state IS NOT NULL
GO

ALTER TABLE state ALTER COLUMN id char(2) NOT NULL
GO

ALTER TABLE state ADD PRIMARY KEY (id)
GO

ALTER TABLE person ADD CONSTRAINT FK_PERSON_STATE FOREIGN KEY (state) REFERENCES state (id)
GO

