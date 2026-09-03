import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  createBonCommande,
  updateBonCommande,
  getBonCommandeById,
} from "../services/bonCommandeService";
import { getClientsPaginated } from "../services/clientService";
import { getArticlesPaginated } from "../services/articleService";

function nouvelleLigne() {
  return { idArticles: "", quantite: 1, prixunitaire: "", remise: 0 };
}

export default function BonCommandeForm() {
  const { id } = useParams();
  const estModification = !!id;

  const [clients, setClients] = useState([]);
  const [articles, setArticles] = useState([]);
  const [idClient, setIdClient] = useState("");
  const [lignes, setLignes] = useState([nouvelleLigne()]);
  const [erreurGenerale, setErreurGenerale] = useState("");
  const [chargement, setChargement] = useState(true);
  const [envoi, setEnvoi] = useState(false);
  const [tracabilite, setTracabilite] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    async function init() {
      try {
        const [clientsData, articlesData] = await Promise.all([
          getClientsPaginated(0, 100),
          getArticlesPaginated(0, 100),
        ]);
        setClients(clientsData.content.filter((c) => c.actif));
        setArticles(articlesData.content);

        if (estModification) {
          const bc = await getBonCommandeById(id);

          if (bc.statut !== "BROUILLON") {
            setErreurGenerale(
              "Ce bon de commande n'est plus modifiable (statut : " + bc.statut + ")."
            );
            setChargement(false);
            return;
          }

          setIdClient(String(bc.idClients));
          setLignes(
            bc.lignes.map((l) => ({
              idArticles: String(l.idArticles),
              quantite: l.quantite,
              prixunitaire: l.prixunitaire,
              remise: l.remise || 0,
            }))
          );
          setTracabilite({
            dateCreation: bc.dateCreation,
            dateModification: bc.dateModification,
            auteur: bc.auteur,
          });
        }
      } catch {
        setErreurGenerale("Impossible de charger les données.");
      } finally {
        setChargement(false);
      }
    }
    init();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

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

      const resultat = estModification
        ? await updateBonCommande(id, dto)
        : await createBonCommande(dto);

      navigate(`/bons-commande/${resultat.idBonCommande}`);
    } catch (err) {
      setErreurGenerale(
        err.response?.data?.message || "Erreur lors de l'enregistrement du bon de commande."
      );
    } finally {
      setEnvoi(false);
    }
  };

  if (chargement) {
    return <div>Chargement...</div>;
  }

  // Bon non-BROUILLON : on bloque l'edition, on affiche juste l'erreur + retour
  if (estModification && erreurGenerale && lignes.length === 1 && !lignes[0].idArticles) {
    return (
      <div className="boncommande-form">
        <p className="erreur">{erreurGenerale}</p>
        <button onClick={() => navigate(`/bons-commande/${id}`)}>Retour au détail</button>
      </div>
    );
  }

  return (
    <div className="boncommande-form">
      <h1>{estModification ? "Modifier le bon de commande" : "Nouveau bon de commande"}</h1>

      {tracabilite && tracabilite.dateCreation && (
        <p className="note" style={{ marginBottom: "16px" }}>
          Créé le {new Date(tracabilite.dateCreation).toLocaleString("fr-FR")}
          {tracabilite.auteur && ` par ${tracabilite.auteur}`}
          {tracabilite.dateModification &&
            ` — Modifié le ${new Date(tracabilite.dateModification).toLocaleString("fr-FR")}`}
        </p>
      )}

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
                    min="0"
                    value={ligne.prixunitaire}
                    onChange={(e) => handleLigneChange(index, "prixunitaire", e.target.value)}
                  />
                </td>
                <td>
                  <input
                    type="number"
                    step="0.01"
                    min="0"
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
          Les totaux (HT, TVA, TTC) seront calculés automatiquement par le serveur après enregistrement.
        </p>

        <div className="form-actions">
          <button type="button" onClick={() => navigate(estModification ? `/bons-commande/${id}` : "/bons-commande")}>
            Annuler
          </button>
          <button type="submit" disabled={envoi}>
            {envoi ? "Enregistrement..." : estModification ? "Enregistrer les modifications" : "Enregistrer le brouillon"}
          </button>
        </div>
      </form>
    </div>
  );
}