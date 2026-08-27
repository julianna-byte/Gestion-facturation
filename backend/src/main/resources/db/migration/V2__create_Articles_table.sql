CREATE TABLE Articles(
idArticles BIGSERIAL PRIMARY KEY,
code CHAR(10) NOT NULL,
libelle VARCHAR(150) NOT NULL,
description VARCHAR(255) NOT NULL,
unite integer NOT NULL,
TauxTva decimal NOT NULL,
prixunitaireHT decimal NOT NULL
);

