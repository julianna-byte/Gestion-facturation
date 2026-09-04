import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  getBonCommandeById,
  validerBonCommande,
  annulerBonCommande,
} from "../services/bonCommandeService";
import { genererFacture } from "../services/factureService";

export default function BonCommandeDetail() {
  const { id } = useParams();
  const [bc, setBc] = useState(null);
  const [erreur, setErreur] = useState("");
  const [chargement, setChargement] = useState(true);
  const [actionEnCours, setActionEnCours] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    chargerBonCommande();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  async function chargerBonCommande() {
    setChargement(true);
    setErreur("");
    try {
      const data = await getBonCommandeById(id);
      setBc(data);
    } catch {
      setErreur("Impossible de charger ce bon de commande.");
    } finally {
      setChargement(false);
    }
  }

  const handleValider = async () => {
    if (!window.confirm("Valider ce bon de commande ? Il ne sera plus modifiable ensuite.")) {
      return;
    }
    setActionEnCours(true);
    try {
      const updated = await validerBonCommande(id);
      setBc(updated);
    } catch (err) {
      alert(err.response?.data?.message || "Erreur lors de la validation.");
    } finally {
      setActionEnCours(false);
    }
  };

  const handleAnnuler = async () => {
    if (!window.confirm("Annuler ce bon de commande ?")) return;
    setActionEnCours(true);
    try {
      const updated = await annulerBonCommande(id);
      setBc(updated);
    } catch (err) {
      alert(err.response?.data?.message || "Erreur lors de l'annulation.");
    } finally {
      setActionEnCours(false);
    }
  };

  const handleGenererFacture = async (type) => {
    const libelle = type === "DEFINITIVE" ? "définitive" : "proforma";
    if (!window.confirm(`Générer une facture ${libelle} à partir de ce bon de commande ?`)) {
      return;
    }
    setActionEnCours(true);
    try {
      const facture = await genererFacture(id, type);
      navigate(`/factures/${facture.idFacture}`);
    } catch (err) {
      alert(err.response?.data?.message || "Erreur lors de la génération de la facture.");
    } finally {
      setActionEnCours(false);
    }
  };

  if (chargement) return <div>Chargement...</div>;
  if (erreur) return <p className="erreur">{erreur}</p>;
  if (!bc) return null;

  return (
    <div className="boncommande-detail">
      <header>
        <h1>Bon de commande {bc.numeroBon}</h1>
        <p>
          Client : <strong>{bc.nomClient}</strong> — Statut :{" "}
          <strong>{bc.statut}</strong>
        </p>
      </header>

      {bc.dateCreation && (
       <p className="note">
       Créé le {new Date(bc.dateCreation).toLocaleString("fr-FR")}
       {bc.auteur && ` par ${bc.auteur}`}
       {bc.dateModification && ` — Modifié le ${new Date(bc.dateModification).toLocaleString("fr-FR")}`}
       </p>
      )}

      <table>
        <thead>
          <tr>
            <th>Article</th>
            <th>Quantité</th>
            <th>Prix unitaire HT</th>
            <th>Remise</th>
          </tr>
        </thead>
        <tbody>
          {bc.lignes.map((ligne) => (
            <tr key={ligne.idLignesCommande}>
              <td>Article #{ligne.idArticles}</td>
              <td>{ligne.quantite}</td>
              <td>{ligne.prixunitaire} XOF</td>
              <td>{ligne.remise} XOF</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="recapitulatif">
        <p>Total HT : {bc.totalHT} XOF</p>
        <p>TVA : {bc.tva ?? bc.Tva} XOF</p>
        <p>
          <strong>Total TTC : {bc.totalTtc} XOF</strong>
        </p>
      </div>

      <div className="form-actions">
        <button onClick={() => navigate("/bons-commande")}>Retour à la liste</button>

        {bc.statut === "BROUILLON" && (
          <>
            <button onClick={() => navigate(`/bons-commande/${bc.idBonCommande}/modifier`)}>
              Modifier
            </button>

            <button onClick={handleValider} disabled={actionEnCours}>
              Valider
            </button>
            <button onClick={handleAnnuler} disabled={actionEnCours}>
              Annuler
            </button>
          </>
        )}

        {bc.statut === "VALIDE" && (
          <>
            <button onClick={() => handleGenererFacture("PROFORMA")} disabled={actionEnCours}>
              Générer facture proforma
            </button>
            <button onClick={() => handleGenererFacture("DEFINITIVE")} disabled={actionEnCours}>
              Générer facture définitive
            </button>
            <button onClick={handleAnnuler} disabled={actionEnCours}>
              Annuler
            </button>
          </>
        )}
      </div>
    </div>
  );
}
