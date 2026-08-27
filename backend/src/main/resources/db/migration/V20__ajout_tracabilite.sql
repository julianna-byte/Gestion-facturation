-- 1) Clients : ajout des colonnes de traçabilité
ALTER TABLE clients
    ADD COLUMN IF NOT EXISTS datecreation TIMESTAMP,
    ADD COLUMN IF NOT EXISTS datemodification TIMESTAMP,
    ADD COLUMN IF NOT EXISTS auteur VARCHAR(255);

-- 2) BonCommande : la colonne dateCreation existait déjà en DATE, on la convertit en TIMESTAMP
ALTER TABLE boncommande
    ALTER COLUMN datecreation TYPE TIMESTAMP USING datecreation::timestamp;

ALTER TABLE boncommande
    ADD COLUMN IF NOT EXISTS datemodification TIMESTAMP,
    ADD COLUMN IF NOT EXISTS auteur VARCHAR(255);

-- 3) Facture : idem, conversion DATE -> TIMESTAMP
ALTER TABLE facture
    ALTER COLUMN datecreation TYPE TIMESTAMP USING datecreation::timestamp;

ALTER TABLE facture
    ADD COLUMN IF NOT EXISTS datemodification TIMESTAMP,
    ADD COLUMN IF NOT EXISTS auteur VARCHAR(255);

-- 4) Suppression de la table Enregistrement (non utilisée, remplacée par
--    la traçabilité directement portée par chaque entité)
DROP TABLE IF EXISTS enregistrement;
