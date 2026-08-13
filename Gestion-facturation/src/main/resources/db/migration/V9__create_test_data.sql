--clients
INSERT INTO Clients (RCCM, raisonsociale, adresse, ville, pays, telephone, email, nomcontact, NIF) VALUES
('RCCM011', 'Banque Ouest Africaine', 'Avenue de la République', 'Lomé', 'Togo', '+22890123470', 'contact@boa.tg', 'Komi Agbeko', 'NIF011'),
('RCCM012', 'Université de Kara', 'Campus Universitaire', 'Kara', 'Togo', '+22890123471', 'info@univkara.tg', 'Dr. Mawuli K.', 'NIF012'),
('RCCM013', 'Ministère des Finances', 'Immeuble Administratif', 'Lomé', 'Togo', '+22890123472', 'minfin@togo.tg', 'Mme. Adjo K.', 'NIF013'),
('RCCM014', 'Société Télécom Togo', 'Boulevard du 13 Janvier', 'Lomé', 'Togo', '+22890123473', 'support@telecomtg.tg', 'Yao Mensah', 'NIF014'),
('RCCM015', 'Port Autonome de Lomé', 'Zone Portuaire', 'Lomé', 'Togo', '+22890123474', 'info@pal.tg', 'Awa Diallo', 'NIF015'),
('RCCM016', 'Hôpital Central', 'Rue de la Santé', 'Sokodé', 'Togo', '+22890123475', 'contact@hopital.tg', 'Dr. Sena A.', 'NIF016'),
('RCCM017', 'ONG Développement Rural', 'Rue des Plantes', 'Atakpamé', 'Togo', '+22890123476', 'ong@devrural.tg', 'Ama Kouassi', 'NIF017'),
('RCCM018', 'Société Énergie Togo', 'Boulevard de l’Indépendance', 'Lomé', 'Togo', '+22890123477', 'energie@togo.tg', 'Koffi Amouzou', 'NIF018'),
('RCCM019', 'Université de Lomé', 'Campus Nord', 'Lomé', 'Togo', '+22890123478', 'info@univlome.tg', 'Prof. Kodjo A.', 'NIF019'),
('RCCM020', 'Société Agricole Togo', 'Route de Kpalimé', 'Kpalimé', 'Togo', '+22890123479', 'agri@togo.tg', 'Jean Dogbe', 'NIF020');



-- Articles (matériel)
INSERT INTO Articles (code, libelle, description, unite, TauxTva, prixunitaireHT) VALUES
('ART016', 'Serveur Dell PowerEdge', 'Serveur rack pour datacenter', 1, 18, 2500000),
('ART017', 'Firewall Fortinet', 'Équipement de sécurité réseau', 1, 18, 1200000),
('ART018', 'Switch Cisco 24 ports', 'Switch réseau pour entreprise', 1, 18, 800000),
('ART019', 'Laptop HP ProBook', 'Ordinateur portable professionnel', 1, 18, 600000),
('ART020', 'Imprimante multifonction', 'Imprimante réseau avec scanner', 1, 18, 350000);

-- prestations 
INSERT INTO Articles (code, libelle, description, unite, TauxTva, prixunitaireHT) VALUES
('SERV006', 'Audit de sécurité informatique', 'Analyse des vulnérabilités et recommandations', 1, 18, 200000),
('SERV007', 'Formation Power BI', 'Session de formation pour 15 personnes', 1, 18, 150000),
('SERV008', 'Déploiement Cloud AWS', 'Migration et configuration d’infrastructures cloud', 1, 18, 500000),
('SERV009', 'Maintenance annuelle systèmes', 'Contrat de maintenance sur serveurs et réseaux', 1, 18, 300000),
('SERV010', 'Développement application mobile', 'Création d’une app Android/iOS sur mesure', 1, 18, 800000),
('SERV011', 'Support technique 24/7', 'Assistance technique illimitée pour entreprise', 1, 18, 250000),
('SERV012', 'Formation cybersécurité', 'Atelier de sensibilisation et bonnes pratiques', 1, 18, 180000),
('SERV013', 'Intégration Docker/Jenkins', 'Mise en place CI/CD pour projet logiciel', 1, 18, 400000),
('SERV014', 'Migration Oracle vers PostgreSQL', 'Transfert et optimisation de bases de données', 1, 18, 600000),
('SERV015', 'Consulting transformation digitale', 'Accompagnement stratégique pour modernisation SI', 1, 18, 350000);
