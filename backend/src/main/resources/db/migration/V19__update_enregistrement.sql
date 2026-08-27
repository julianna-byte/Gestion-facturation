ALTER TABLE enregistrement
ALTER COLUMN datecreation TYPE TIMESTAMP(6)
USING datecreation::TIMESTAMP(6);

ALTER TABLE enregistrement
ALTER COLUMN datemodification TYPE TIMESTAMP(6)
USING datemodification::TIMESTAMP(6);
