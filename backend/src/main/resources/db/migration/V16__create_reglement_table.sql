CREATE TABLE Reglement(
    idreglement BIGSERIAL PRIMARY KEY,
    datereglement DATE NOT NULL,
    montant NUMERIC NOT NULL,
    mode VARCHAR(50) NOT NULL,
    idfacture BIGINT NOT NULL,
    CONSTRAINT fk_reglement_facture FOREIGN KEY (idfacture) REFERENCES Facture(idfacture)
);
