-- Changeset changelog.yml::JIRA-1234-extend-model-label-max-length::Eugene Grybinnyk
-- Extends (factory generated) model label max length to 40 characters.
ALTER TABLE product ALTER COLUMN model_label varchar(40)
GO

