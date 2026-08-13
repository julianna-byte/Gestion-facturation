CREATE TABLE Facture(
idFacture BIGSERIAL PRIMARY KEY,
numerofacture VARCHAR (50) NOT NULL,
idUtilisateur BIGINT,
idBonCommande BIGINT,
idClients BIGINT
);
