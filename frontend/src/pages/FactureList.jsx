import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getFacturesPaginated } from "../services/factureService";

const STATUT_STYLE = {
  EMISE: { background: "#e0e0e0", color: "#333" },
  PARTIELLEMENT_PAYEE: { background: "#fff3cd", color: "#856404" },
  PAYEE: { background: "#d4edda", color: "#155724" },
  ANNULEE: { background: "#f8d7da", color: "#721c24" },
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

export default function FactureList() {
  const [pageData, setPageData] = useState(null);
  const [page, setPage] = useState(0);
  const [chargement, setChargement] = useState(true);
  const [erreur, setErreur] = useState("");

  const navigate = useNavigate();
  const taillePage = 10;

  useEffect(() => {
    chargerFactures();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page]);

  async function chargerFactures() {
    setChargement(true);
    setErreur("");
    try {
      const data = await getFacturesPaginated(page, taillePage);
      setPageData(data);
    } catch  {
      setErreur("Impossible de charger les factures.");
    } finally {
      setChargement(false);
    }
  }

  if (chargement && !pageData) {
    return <div>Chargement des factures...</div>;
  }

  return (
    <div className="facture-list">
      <header>
        <h1>Factures</h1>
      </header>

      {erreur && <p className="erreur">{erreur}</p>}

      <table>
        <thead>
          <tr>
            <th>N° Facture</th>
            <th>Client</th>
            <th>Type</th>
            <th>Total TTC</th>
            <th>Reste à payer</th>
            <th>Statut</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {pageData?.content?.map((f) => (
            <tr key={f.idFacture}>
              <td>{f.numerofacture}</td>
              <td>{f.nomClient || f.idClients}</td>
              <td>{f.type}</td>
              <td>{f.totalTtc} XOF</td>
              <td>{f.resteAPayer} XOF</td>
              <td>
                <Badge statut={f.statut} />
              </td>
              <td>
                <button onClick={() => navigate(`/factures/${f.idFacture}`)}>
                  Voir / Gérer
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {pageData && pageData.content.length === 0 && <p>Aucune facture pour l'instant.</p>}

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

