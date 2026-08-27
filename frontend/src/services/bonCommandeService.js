import api from "../api/axiosConfig";

// GET /api/bons_commande/paginated?page=0&size=10
export async function getBonsCommandePaginated(page = 0, size = 10) {
  const response = await api.get("/bons_commande/paginated", {
    params: { page, size },
  });
  return response.data;
}

// GET /api/bons_commande/{id}
export async function getBonCommandeById(id) {
  const response = await api.get(`/bons_commande/${id}`);
  return response.data;
}

// POST /api/bons_commande - cree en statut BROUILLON
// dto attendu : { idClients, lignes: [{ idArticles, quantite, prixunitaire, remise }] }
export async function createBonCommande(dto) {
  const response = await api.post("/bons_commande", dto);
  return response.data;
}

// PUT /api/bons_commande/{id} - uniquement si statut = BROUILLON
export async function updateBonCommande(id, dto) {
  const response = await api.put(`/bons_commande/${id}`, dto);
  return response.data;
}

// GET /api/bons_commande/{id}/valider - passe BROUILLON -> VALIDE
export async function validerBonCommande(id) {
  const response = await api.get(`/bons_commande/${id}/valider`);
  return response.data;
}

// GET /api/bons_commande/{id}/annuler
export async function annulerBonCommande(id) {
  const response = await api.get(`/bons_commande/${id}/annuler`);
  return response.data;
}