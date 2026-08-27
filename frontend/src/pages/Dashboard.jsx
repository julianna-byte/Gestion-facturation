import { useEffect, useState } from "react";
import { getDashboard } from "../services/dashboardService";
import { useAuth } from "../context/useAuth";
import { useNavigate } from "react-router-dom";

// Formatte un montant en Francs CFA, sans decimale, avec separateur de milliers
// (coherent avec la regle RG-01 du cahier des charges)
function formatMontant(montant) {
  if (montant === null || montant === undefined) return "0 XOF";
  return new Intl.NumberFormat("fr-FR", {
    maximumFractionDigits: 0,
  }).format(montant) + " XOF";
}

export default function Dashboard() {
  const [data, setData] = useState(null);
  const [erreur, setErreur] = useState("");
  const [chargement, setChargement] = useState(true);

  const { logoutUser, role } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    async function fetchDashboard() {
      try {
        const result = await getDashboard();
        setData(result);
      } catch (err) {
        setErreur("Impossible de charger le tableau de bord.");
      } finally {
        setChargement(false);
      }
    }
    fetchDashboard();
  }, []);

  const handleLogout = () => {
    logoutUser();
    navigate("/login");
  };

  if (chargement) {
    return <div className="dashboard-loading">Chargement...</div>;
  }

  if (erreur) {
    return <div className="dashboard-erreur">{erreur}</div>;
  }

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>Tableau de bord</h1>
        <div className="dashboard-user-info">
          <span>Rôle : {role}</span>
          <button onClick={handleLogout}>Déconnexion</button>
        </div>
      </header>

      <section className="dashboard-cards">
        <div className="dashboard-card">
          <h2>Chiffre d'affaires du mois</h2>
          <p className="dashboard-card-value">
            {formatMontant(data.chiffreAffairesDuMois)}
          </p>
        </div>

        <div className="dashboard-card">
          <h2>Factures impayées</h2>
          <p className="dashboard-card-value">{data.nombreFacturesImpayees}</p>
        </div>
      </section>

      <section className="dashboard-top-clients">
        <h2>Top 5 des clients</h2>
        {data.topClients && data.topClients.length > 0 ? (
          <table>
            <thead>
              <tr>
                <th>Raison sociale</th>
                <th>Total facturé</th>
              </tr>
            </thead>
            <tbody>
              {data.topClients.map((client, index) => (
                <tr key={index}>
                  <td>{client.raisonSociale}</td>
                  <td>{formatMontant(client.totalFacture)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p>Aucune donnée disponible.</p>
        )}
      </section>
    </div>
  );
}

