import api from "../api/axiosConfig";
 
// Correspond exactement au AuthController.java :
// POST /api/auth/login attend { identifiant, motdepasse }
// et renvoie { token, role }
export async function login(identifiant, motdepasse) {
  const response = await api.post("/auth/login", { identifiant, motdepasse });
  return response.data; // { token: "...", role: "ADMIN" | "COMMERCIAL" }
}
 
