import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getArticlesPaginated, searchArticles, deleteArticle } from "../services/articleService";
import { useAuth } from "../context/useAuth";

export default function ArticleList() {
  const [pageData, setPageData] = useState(null);
  const [page, setPage] = useState(0);
  const [recherche, setRecherche] = useState("");
  const [chargement, setChargement] = useState(true);
  const [erreur, setErreur] = useState("");

  const { isAdmin } = useAuth();
  const navigate = useNavigate();
  const taillePage = 10;

  useEffect(() => {
    chargerArticles();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page]);

  async function chargerArticles() {
    setChargement(true);
    setErreur("");
    try {
      const data = recherche.trim()
        ? await searchArticles(recherche, page, taillePage)
        : await getArticlesPaginated(page, taillePage);
      setPageData(data);
    } catch {
      setErreur("Impossible de charger les articles.");
    } finally {
      setChargement(false);
    }
  }

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    setPage(0);
    chargerArticles();
  };

  const handleDelete = async (id, libelle) => {
    const confirmed = window.confirm(`Supprimer l'article "${libelle}" ?`);
    if (!confirmed) return;

    try {
      await deleteArticle(id);
      chargerArticles();
    } catch (err) {
      if (err.response?.status === 403) {
        alert("Action réservée aux administrateurs.");
      } else {
        alert("Erreur lors de la suppression.");
      }
    }
  };

  if (chargement && !pageData) {
    return <div>Chargement des articles...</div>;
  }

  return (
    <div className="article-list">
      <header className="article-list-header">
        <h1>Catalogue</h1>
        {isAdmin && (
          <button onClick={() => navigate("/catalogue/nouveau")}>
            + Nouvel article
          </button>
        )}
      </header>

      <form onSubmit={handleSearchSubmit} className="article-search">
        <input
          type="text"
          placeholder="Rechercher par libellé..."
          value={recherche}
          onChange={(e) => setRecherche(e.target.value)}
        />
        <button type="submit">Rechercher</button>
      </form>

      {erreur && <p className="erreur">{erreur}</p>}

      <table>
        <thead>
          <tr>
            <th>Code</th>
            <th>Libellé</th>
            <th>Prix unitaire HT</th>
            <th>TVA</th>
            {isAdmin && <th>Actions</th>}
          </tr>
        </thead>
        <tbody>
          {pageData?.content?.map((article) => (
            <tr key={article.idArticles}>
              <td>{article.code}</td>
              <td>{article.libelle}</td>
              <td>{article.prixunitaireHT} XOF</td>
              <td>{article.tauxTva} %</td>
              {isAdmin && (
                <td>
                  <button onClick={() => navigate(`/catalogue/${article.idArticles}`)}>
                    Modifier
                  </button>
                  <button onClick={() => handleDelete(article.idArticles, article.libelle)}>
                    Supprimer
                  </button>
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>

      {pageData && pageData.content.length === 0 && <p>Aucun article trouvé.</p>}

      {pageData && pageData.totalPages > 1 && (
        <div className="pagination">
          <button disabled={page === 0} onClick={() => setPage((p) => p - 1)}>
            Précédent
          </button>
          <span>
            Page {page + 1} / {pageData.totalPages}
          </span>
          <button
            disabled={page >= pageData.totalPages - 1}
            onClick={() => setPage((p) => p + 1)}
          >
            Suivant
          </button>
        </div>
      )}
    </div>
  );
}
