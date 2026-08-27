CREATE TABLE Enregistrement(
idEnregistrement BIGSERIAL PRIMARY KEY,
datecreation Date NOT NULL,
datemodification Date NOT NULL,
auteur VARCHAR(50) NOT NULL,
idBonCommande BIGINT,
idFacture BIGINT,
idClients BIGINT
);

