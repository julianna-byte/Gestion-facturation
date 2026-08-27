import api from "../api/axiosConfig";
 
// GET /api/articles/paginated?page=0&size=10
export async function getArticlesPaginated(page = 0, size = 10) {
  const response = await api.get("/articles/paginated", {
    params: { page, size },
  });
  return response.data;
}
 
// GET /api/articles/search?libelle=...&page=0&size=10
export async function searchArticles(libelle, page = 0, size = 10) {
  const response = await api.get("/articles/search", {
    params: { libelle, page, size },
  });
  return response.data;
}
 
// GET /api/articles/{id}
export async function getArticleById(id) {
  const response = await api.get(`/articles/${id}`);
  return response.data;
}
 
// POST /api/articles - reserve ADMIN cote backend
export async function createArticle(articleData) {
  const response = await api.post("/articles", articleData);
  return response.data;
}
 
// PUT /api/articles/{id} - reserve ADMIN cote backend
export async function updateArticle(id, articleData) {
  const response = await api.put(`/articles/${id}`, articleData);
  return response.data;
}
 
// DELETE /api/articles/{id} - reserve ADMIN cote backend
export async function deleteArticle(id) {
  await api.delete(`/articles/${id}`);
}
