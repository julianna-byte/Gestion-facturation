CREATE TABLE BonCommande(
idBonCommande BIGSERIAL PRIMARY KEY,
numeroBon VARCHAR(50) NOT NULL,
totalHT numeric NOT NULL,
Tva numeric NOT NULL,
totalTtc numeric NOT NULL
);

