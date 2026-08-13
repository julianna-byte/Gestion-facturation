CREATE TABLE LignesCommande(
idLignesCommande BIGSERIAL PRIMARY KEY,
quantite integer NOT NULL,
remise numeric NOT NULL,
prixunitaire numeric NOT NULL
);

