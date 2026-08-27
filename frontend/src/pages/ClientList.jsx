import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  getClientsPaginated,
  searchClients,
  deactivateClient,
} from "../services/clientService";

export default function ClientList() {
  const [pageData, setPageData] = useState(null);
  const [page, setPage] = useState(0);
  const [recherche, setRecherche] = useState("");
  const [chargement, setChargement] = useState(true);
  const [erreur, setErreur] = useState("");

  const navigate = useNavigate();
  const taillePage = 10;

  useEffect(() => {
    chargerClients();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page]);

  async function chargerClients() {
    setChargement(true);
    setErreur("");
    try {
      const data = recherche.trim()
        ? await searchClients(recherche, page, taillePage)
        : await getClientsPaginated(page, taillePage);
      setPageData(data);
    } catch {
      setErreur("Impossible de charger les clients.");
    } finally {
      setChargement(false);
    }
  }

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    setPage(0);
    chargerClients();
  };

  const handleDeactivate = async (id, raisonsociale) => {
    const confirmed = window.confirm(
      `Désactiver le client "${raisonsociale}" ? Cette action est réversible uniquement en base.`
    );
    if (!confirmed) return;

    try {
      await deactivateClient(id);
      chargerClients(); // rafraichit la liste
    } catch  {
      alert("Erreur lors de la désactivation.");
    }
  };

  if (chargement && !pageData) {
    return <div>Chargement des clients...</div>;
  }

  return (
    <div className="client-list">
      <header className="client-list-header">
        <h1>Clients</h1>
        <button onClick={() => navigate("/clients/nouveau")}>
          + Nouveau client
        </button>
      </header>

      <form onSubmit={handleSearchSubmit} className="client-search">
        <input
          type="text"
          placeholder="Rechercher par raison sociale..."
          value={recherche}
          onChange={(e) => setRecherche(e.target.value)}
        />
        <button type="submit">Rechercher</button>
      </form>

      {erreur && <p className="erreur">{erreur}</p>}

      <table>
        <thead>
          <tr>
            <th>Raison sociale</th>
            <th>NIF</th>
            <th>Ville</th>
            <th>Téléphone</th>
            <th>Statut</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {pageData?.content?.map((client) => (
            <tr key={client.idClient}>
              <td>{client.raisonsociale}</td>
              <td>{client.nif}</td>
              <td>{client.ville}</td>
              <td>{client.telephone}</td>
              <td>{client.actif ? "Actif" : "Désactivé"}</td>
              <td>
                <button onClick={() => navigate(`/clients/${client.idClient}`)}>
                  Modifier
                </button>
                {client.actif && (
                  <button
                    onClick={() =>
                      handleDeactivate(client.idClient, client.raisonsociale)
                    }
                  >
                    Désactiver
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {pageData && pageData.content.length === 0 && (
        <p>Aucun client trouvé.</p>
      )}

      {pageData && pageData.totalPages > 1 && (
        <div className="pagination">
          <button
            disabled={page === 0}
            onClick={() => setPage((p) => p - 1)}
          >
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
