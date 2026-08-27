import api from "../api/axiosConfig";

// Correspond exactement a DashboardController.java :
// GET /api/dashboard renvoie { chiffreAffairesDuMois, nombreFacturesImpayees, topClients }
export async function getDashboard() {
  const response = await api.get("/dashboard");
  return response.data;
}
