import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { createBonCommande } from "../services/bonCommandeService";
import { getClientsPaginated } from "../services/clientService";
import { getArticlesPaginated } from "../services/articleService";

function nouvelleLigne() {
  return { idArticles: "", quantite: 1, prixunitaire: "", remise: 0 };
}

export default function BonCommandeForm() {
  const [clients, setClients] = useState([]);
  const [articles, setArticles] = useState([]);
  const [idClient, setIdClient] = useState("");
  const [lignes, setLignes] = useState([nouvelleLigne()]);
  const [erreurGenerale, setErreurGenerale] = useState("");
  const [chargement, setChargement] = useState(true);
  const [envoi, setEnvoi] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    Promise.all([getClientsPaginated(0, 100), getArticlesPaginated(0, 100)])
      .then(([clientsData, articlesData]) => {
        setClients(clientsData.content.filter((c) => c.actif));
        setArticles(articlesData.content);
      })
      .catch(() => setErreurGenerale("Impossible de charger clients/articles."))
      .finally(() => setChargement(false));
  }, []);

  const handleLigneChange = (index, champ, valeur) => {
    setLignes((prev) => {
      const copie = [...prev];
      copie[index] = { ...copie[index], [champ]: valeur };

      if (champ === "idArticles") {
        const article = articles.find((a) => a.idArticles === Number(valeur));
        if (article) {
          copie[index].prixunitaire = article.prixunitaireHT;
        }
      }
      return copie;
    });
  };

  const ajouterLigne = () => setLignes((prev) => [...prev, nouvelleLigne()]);

  const supprimerLigne = (index) => {
    setLignes((prev) => prev.filter((_, i) => i !== index));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErreurGenerale("");

    if (!idClient) {
      setErreurGenerale("Veuillez sélectionner un client.");
      return;
    }
    if (lignes.some((l) => !l.idArticles || !l.quantite)) {
      setErreurGenerale("Chaque ligne doit avoir un article et une quantité.");
      return;
    }

    setEnvoi(true);
    try {
      const dto = {
        idClients: Number(idClient),
        lignes: lignes.map((l) => ({
          idArticles: Number(l.idArticles),
          quantite: Number(l.quantite),
          prixunitaire: l.prixunitaire !== "" ? Number(l.prixunitaire) : undefined,
          remise: l.remise ? Number(l.remise) : 0,
        })),
      };
      const created = await createBonCommande(dto);
      navigate(`/bons-commande/${created.idBonCommande}`);
    } catch (err) {
      setErreurGenerale(
        err.response?.data?.message || "Erreur lors de la création du bon de commande."
      );
    } finally {
      setEnvoi(false);
    }
  };

  if (chargement) {
    return <div>Chargement...</div>;
  }

  return (
    <div className="boncommande-form">
      <h1>Nouveau bon de commande</h1>

      {erreurGenerale && <p className="erreur">{erreurGenerale}</p>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="client">Client *</label>
          <select
            id="client"
            value={idClient}
            onChange={(e) => setIdClient(e.target.value)}
            required
          >
            <option value="">-- Sélectionner un client --</option>
            {clients.map((c) => (
              <option key={c.idClient} value={c.idClient}>
                {c.raisonsociale}
              </option>
            ))}
          </select>
        </div>

        <h2>Lignes</h2>
        <table>
          <thead>
            <tr>
              <th>Article</th>
              <th>Quantité</th>
              <th>Prix unitaire HT</th>
              <th>Remise</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {lignes.map((ligne, index) => (
              <tr key={index}>
                <td>
                  <select
                    value={ligne.idArticles}
                    onChange={(e) => handleLigneChange(index, "idArticles", e.target.value)}
                    required
                  >
                    <option value="">-- Article --</option>
                    {articles.map((a) => (
                      <option key={a.idArticles} value={a.idArticles}>
                        {a.libelle} ({a.code})
                      </option>
                    ))}
                  </select>
                </td>
                <td>
                  <input
                    type="number"
                    min="1"
                    value={ligne.quantite}
                    onChange={(e) => handleLigneChange(index, "quantite", e.target.value)}
                    required
                  />
                </td>
                <td>
                  <input
                    type="number"
                    step="0.01"
                    value={ligne.prixunitaire}
                    onChange={(e) => handleLigneChange(index, "prixunitaire", e.target.value)}
                  />
                </td>
                <td>
                  <input
                    type="number"
                    step="0.01"
                    value={ligne.remise}
                    onChange={(e) => handleLigneChange(index, "remise", e.target.value)}
                  />
                </td>
                <td>
                  {lignes.length > 1 && (
                    <button type="button" onClick={() => supprimerLigne(index)}>
                      Supprimer
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        <button type="button" onClick={ajouterLigne}>
          + Ajouter une ligne
        </button>

        <p className="note">
          Les totaux (HT, TVA, TTC) seront calculés automatiquement par le
          serveur après enregistrement.
        </p>

        <div className="form-actions">
          <button type="button" onClick={() => navigate("/bons-commande")}>
            Annuler
          </button>
          <button type="submit" disabled={envoi}>
            {envoi ? "Enregistrement..." : "Enregistrer le brouillon"}
          </button>
        </div>
      </form>
    </div>
  );
}
