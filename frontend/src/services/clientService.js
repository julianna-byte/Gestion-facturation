import api from "../api/axiosConfig";

// GET /api/clients/paginated?page=0&size=10 -> Page<ClientDto>
export async function getClientsPaginated(page = 0, size = 10) {
  const response = await api.get("/clients/paginated", {
    params: { page, size },
  });
  return response.data; // { content, totalPages, totalElements, number, ... }
}

// GET /api/clients/search?raisonsociale=...&page=0&size=10
export async function searchClients(raisonsociale, page = 0, size = 10) {
  const response = await api.get("/clients/search", {
    params: { raisonsociale, page, size },
  });
  return response.data;
}

// GET /api/clients/{id}
export async function getClientById(id) {
  const response = await api.get(`/clients/${id}`);
  return response.data;
}

// POST /api/clients
export async function createClient(clientData) {
  const response = await api.post("/clients", clientData);
  return response.data;
}

// PUT /api/clients/{id} - tous les champs @NotBlank sont obligatoires
export async function updateClient(id, clientData) {
  const response = await api.put(`/clients/${id}`, clientData);
  return response.data;
}

// PATCH /api/clients/{id}/desactiver - pas de body
export async function deactivateClient(id) {
  await api.patch(`/clients/${id}/desactiver`);
}
