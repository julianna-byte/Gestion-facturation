-- Ajout de la colonne role à la table Utilisateur
ALTER TABLE Utilisateur ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'COMMERCIAL';

-- Création d'un utilisateur admin de test
-- Mot de passe en clair : admin123 (haché en BCrypt)
INSERT INTO Utilisateur (identifiant, motdepasse, role) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN');
