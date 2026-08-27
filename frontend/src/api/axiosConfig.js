import axios from "axios";
 
const api = axios.create({
  baseURL: "http://localhost:8081/api", 
});
 
// Ajoute automatiquement le token JWT a chaque requete sortante
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
 
// Gestion centralisee des erreurs d'authentification
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
 
    // 401 = pas connecte / token invalide ou expire -> on deconnecte
    if (status === 401) {
      localStorage.removeItem("token");
      localStorage.removeItem("role");
      window.location.href = "/login";
    }
 
    // 403 = connecte mais role insuffisant (ex: COMMERCIAL sur route ADMIN)
    // On laisse le composant appelant gerer l'affichage du message
    // (voir SecurityConfig.java : ADMIN requis pour POST/PUT/DELETE articles)
 
    return Promise.reject(error);
  }
);
 
export default api;
 
