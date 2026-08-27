--clees etrangeres LignesCommande
ALTER TABLE LignesCommande
ADD CONSTRAINT fk_lignescommande_boncommande
FOREIGN KEY (idBonCommande)
REFERENCES BonCommande(idBonCommande)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE LignesCommande
ADD CONSTRAINT fk_lignescommande_articles
FOREIGN KEY (idArticles)
REFERENCES Articles(idArticles)
ON DELETE RESTRICT ON UPDATE CASCADE;

--clees etrangeres facture
ALTER TABLE Facture
ADD CONSTRAINT fk_facture_boncommande
FOREIGN KEY (idBonCommande)
REFERENCES BonCommande(idBonCommande)
ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE Facture
ADD CONSTRAINT fk_facture_utilisateur
FOREIGN KEY (idUtilisateur)
REFERENCES Utilisateur(idUtilisateur)
ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE Facture
ADD CONSTRAINT fk_facture_clients
FOREIGN KEY (idClients)
REFERENCES Clients(idClient)
ON DELETE RESTRICT ON UPDATE CASCADE;

--clees etrangeres Enregistrement
ALTER TABLE Enregistrement
ADD CONSTRAINT fk_enregistrement_boncommande
FOREIGN KEY (idBonCommande)
REFERENCES BonCommande(idBonCommande)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Enregistrement
ADD CONSTRAINT fk_enregistrement_facture
FOREIGN KEY (idFacture)
REFERENCES Facture(idFacture)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Enregistrement
ADD CONSTRAINT fk_enregistrement_clients
FOREIGN KEY (idClients)
REFERENCES Clients(idClient)
ON DELETE CASCADE ON UPDATE CASCADE;

--clees etrangeres BonCommande
ALTER TABLE BonCommande
ADD CONSTRAINT fk_boncommande_clients
FOREIGN KEY (idClient)
REFERENCES Clients(idClient)
ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE BonCommande
ADD CONSTRAINT fk_boncommande_utilisateur
FOREIGN KEY (idUtilisateur)
REFERENCES Utilisateur(idUtilisateur)
ON DELETE SET NULL ON UPDATE CASCADE;