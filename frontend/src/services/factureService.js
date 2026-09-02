import api from "../api/axiosConfig";

// GET /api/factures/paginated?page=0&size=10
export async function getFacturesPaginated(page = 0, size = 10) {
  const response = await api.get("/factures/paginated", {
    params: { page, size },
  });
  return response.data;
}

// GET /api/factures/{id}
export async function getFactureById(id) {
  const response = await api.get(`/factures/${id}`);
  return response.data;
}

// POST /api/factures/generer/{idBonCommande}?type=PROFORMA|DEFINITIVE
export async function genererFacture(idBonCommande, type) {
  const response = await api.post(`/factures/generer/${idBonCommande}`, null, {
    params: { type },
  });
  return response.data;
}

// POST /api/factures/{id}/reglements - { montant, mode }
export async function enregistrerReglement(idFacture, reglementData) {
  const response = await api.post(`/factures/${idFacture}/reglements`, reglementData);
  return response.data;
}

// PATCH /api/factures/{id}/annuler?motif=...
export async function annulerFacture(idFacture, motif) {
  const response = await api.patch(`/factures/${idFacture}/annuler`, null, {
    params: { motif },
  });
  return response.data;
}

// PATCH /api/factures/{id}/conditions - body texte brut
export async function modifierConditions(idFacture, conditions) {
  const response = await api.patch(`/factures/${idFacture}/conditions`, conditions, {
    headers: { "Content-Type": "text/plain" },
  });
  return response.data;
}

// GET /api/factures/{id}/pdf - telechargement binaire (declenche le download)
export async function telechargerPdfFacture(idFacture, inclureSuiviPaiement) {
  const response = await api.get(`/factures/${idFacture}/pdf`, {
    responseType: "blob",
    params: inclureSuiviPaiement !== undefined ? { inclureSuiviPaiement } : {},
  });
  const url = window.URL.createObjectURL(new Blob([response.data]));
  const link = document.createElement("a");
  link.href = url;
  link.setAttribute("download", `facture-${idFacture}.pdf`);
  document.body.appendChild(link);
  link.click();
  link.remove();
  window.URL.revokeObjectURL(url);
}

// GET /api/factures/{id}/pdf - retourne une URL blob SANS telecharger (apercu inline)
export async function getFacturePdfBlobUrl(idFacture, inclureSuiviPaiement) {
  const response = await api.get(`/factures/${idFacture}/pdf`, {
    responseType: "blob",
    params: inclureSuiviPaiement !== undefined ? { inclureSuiviPaiement } : {},
  });
  return window.URL.createObjectURL(new Blob([response.data], { type: "application/pdf" }));
}