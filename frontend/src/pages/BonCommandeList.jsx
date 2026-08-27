import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getBonsCommandePaginated } from "../services/bonCommandeService";

const STATUT_STYLE = {
  BROUILLON: { background: "#e0e0e0", color: "#333" },
  VALIDE: { background: "#fff3cd", color: "#856404" },
  FACTURE: { background: "#d4edda", color: "#155724" },
  ANNULE: { background: "#f8d7da", color: "#721c24" },
};

function Badge({ statut }) {
  const style = STATUT_STYLE[statut] || {};
  return (
    <span
      style={{
        ...style,
        padding: "2px 8px",
        borderRadius: "4px",
        fontSize: "0.85em",
        fontWeight: "bold",
      }}
    >
      {statut}
    </span>
  );
}

export default function BonCommandeList() {
  const [pageData, setPageData] = useState(null);
  const [page, setPage] = useState(0);
  const [chargement, setChargement] = useState(true);
  const [erreur, setErreur] = useState("");

  const navigate = useNavigate();
  const taillePage = 10;

  async function chargerBons() {
    setChargement(true);
    setErreur("");
    try {
      const data = await getBonsCommandePaginated(page, taillePage);
      setPageData(data);
    } catch {
      setErreur("Impossible de charger les bons de commande.");
    } finally {
      setChargement(false);
    }
  }

  useEffect(() => {
    chargerBons();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page]);

  if (chargement && !pageData) {
    return <div>Chargement des bons de commande...</div>;
  }

  return (
    <div className="boncommande-list">
      <header className="boncommande-list-header">
        <h1>Bons de commande</h1>
        <button onClick={() => navigate("/bons-commande/nouveau")}>
          + Nouveau bon de commande
        </button>
      </header>

      {erreur && <p className="erreur">{erreur}</p>}

      <table>
        <thead>
          <tr>
            <th>N° Bon</th>
            <th>Client</th>
            <th>Total TTC</th>
            <th>Statut</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {pageData?.content?.map((bc) => (
            <tr key={bc.idBonCommande}>
              <td>{bc.numeroBon}</td>
              <td>{bc.nomClient || bc.idClients}</td>
              <td>{bc.totalTtc} XOF</td>
              <td>
                <Badge statut={bc.statut} />
              </td>
              <td>
                <button onClick={() => navigate(`/bons-commande/${bc.idBonCommande}`)}>
                  Voir / Gérer
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {pageData && pageData.content.length === 0 && (
        <p>Aucun bon de commande pour l'instant.</p>
      )}

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