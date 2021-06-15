-- Changeset changelog.xml::JIRA-1234-extend-model-label-max-length::Eugene Grybinnyk
-- Extends (factory generated) model label max length to 40 characters.
ALTER TABLE product MODIFY model_label VARCHAR2(40);

