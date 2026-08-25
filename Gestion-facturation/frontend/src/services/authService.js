import api from "./api";
 
export async function login(identifiant, motdepasse) {
  try {
    const response = await api.post("/auth/login", {
      identifiant,
      motdepasse,
    });
 
    const { token, role } = response.data;
 
    // Stocke le token pour les prochaines requêtes
    localStorage.setItem("token", token);
    localStorage.setItem("role", role);
 
    return response.data;
  } catch (error) {
    // Récupère le vrai message renvoyé par GlobalExceptionHandler si présent
    const message =
      error.response?.data?.message || "Identifiants invalides";
    throw new Error(message , { cause: error });
  }
}
 
export function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("role");
}
 
export function getRole() {
  return localStorage.getItem("role");
}
 
export function isAuthenticated() {
  return !!localStorage.getItem("token");
}
 
