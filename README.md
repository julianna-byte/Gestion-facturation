# Gestion-facturation
## Description
Application Java Spring Boot pour gérer la facturation :
- Clients
- Articles
- Bons de commande 
- Factures
- Utilisateurs
- Lignes de commande
- Tableau de bord

## Technologies
- Spring Boot
- PostgreSQL
- java 17
- Spring security(JWT)
- Swagger/ OpenAPI
- Postman

## Lancer le backend
mvn spring-boot:run

## Lancer le frontend
npm run dev

## Accéder à Swagger UI
http://localhost:8081/swagger-ui/index.html

## Documentation API

- Authentification
POST -	/api/auth/login - Authentifie un utilisateur et retourne un token JWT + rôle
  
- Clients

GET - /api/clients - Lister tous les clients

GET	- /api/clients/paginated?page=&size= -	Lister les clients avec pagination

GET - /api/clients/{id} - Récupérer un client par ID

GET	- /api/clients/search?raisonsociale= -	Rechercher des clients par raison sociale

POST - /api/clients	- Créer un client

PUT - /api/clients/{id} - Mettre à jour un client

PATCH - /api/clients/{id}/desactiver -	Désactiver un client

- Articles

GET	- /api/articles - Lister tous les articles

GET	- /api/articles/paginated?page=&size= -	Lister les articles avec pagination

GET	- /api/articles/{id} - Récupérer un article par ID

GET	- /api/articles/search?libelle= - 	Rechercher des articles par libellé

POST - /api/articles - Créer un article

PUT	- /api/articles/{id} - Mettre à jour un article

DELETE	- /api/articles/{id} - Supprimer un article

- Bons de commande

POST - /api/bons_commande - Créer un bon de commande

GET	- /api/bons_commande/paginated?page=&size= - Lister les bons de commande avec pagination

GET	- /api/bons_commande/{id} - Récupérer un bon de commande par ID

PUT	- /api/bons_commande/{id} - Mettre à jour un bon de commande

GET	- /api/bons_commande/{id}/valider - 	Valider un bon de commande

GET	- /api/bons_commande/{id}/annuler - 	Annuler un bon de commande

- Facture


POST - /api/factures/generer/{idBonCommande}?type=	- Générer une facture depuis un bon de commande

GET	- /api/factures/paginated?page=&size= - Lister les factures avec pagination

GET	- /api/factures/{id} - Récupérer une facture par ID

POST - /api/factures/{id}/reglements - 	Enregistrer un règlement sur une facture

PATCH - /api/factures/{id}/annuler?motif= - Annuler une facture avec motif

GET	- /api/factures/{id}/pdf - Télécharger une facture en PDF


- Dashboard

GET	- /api/dashboard - Obtenir les statistiques globales du tableau de bord

