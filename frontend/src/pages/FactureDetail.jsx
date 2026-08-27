import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  getFactureById,
  enregistrerReglement,
  annulerFacture,
  telechargerPdfFacture,
} from "../services/factureService";

const MODES_REGLEMENT = ["Espèces", "Virement", "Chèque", "Mobile Money"];

export default function FactureDetail() {
  const { id } = useParams();
  const [facture, setFacture] = useState(null);
  const [erreur, setErreur] = useState("");
  const [chargement, setChargement] = useState(true);
  const [actionEnCours, setActionEnCours] = useState(false);

  // Formulaire d'enregistrement de reglement
  const [montant, setMontant] = useState("");
  const [mode, setMode] = useState(MODES_REGLEMENT[0]);
  const [erreurReglement, setErreurReglement] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    chargerFacture();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  async function chargerFacture() {
    setChargement(true);
    setErreur("");
    try {
      const data = await getFactureById(id);
      setFacture(data);
    } catch {
      setErreur("Impossible de charger cette facture.");
    } finally {
      setChargement(false);
    }
  }

  const handleReglementSubmit = async (e) => {
    e.preventDefault();
    setErreurReglement("");

    if (!montant || Number(montant) <= 0) {
      setErreurReglement("Le montant doit être supérieur à 0.");
      return;
    }

    setActionEnCours(true);
    try {
      const updated = await enregistrerReglement(id, {
        montant: Number(montant),
        mode,
      });
      setFacture(updated);
      setMontant("");
    } catch (err) {
      setErreurReglement(
        err.response?.data?.message || "Erreur lors de l'enregistrement du règlement."
      );
    } finally {
      setActionEnCours(false);
    }
  };

  const handleAnnuler = async () => {
    const motif = window.prompt("Motif d'annulation (obligatoire) :");
    if (!motif || !motif.trim()) {
      if (motif !== null) alert("Le motif est obligatoire pour annuler une facture.");
      return;
    }

    setActionEnCours(true);
    try {
      const updated = await annulerFacture(id, motif);
      setFacture(updated);
    } catch (err) {
      alert(err.response?.data?.message || "Erreur lors de l'annulation.");
    } finally {
      setActionEnCours(false);
    }
  };

  const handleTelechargerPdf = async () => {
    try {
      await telechargerPdfFacture(id);
    } catch (err) {
      alert("Erreur lors du téléchargement du PDF.");
    }
  };

  if (chargement) return <div>Chargement...</div>;
  if (erreur) return <p className="erreur">{erreur}</p>;
  if (!facture) return null;

  const peutEnregistrerReglement =
    facture.statut === "EMISE" || facture.statut === "PARTIELLEMENT_PAYEE";
  const peutAnnuler = facture.statut !== "ANNULEE";

  return (
    <div className="facture-detail">
      <header>
        <h1>Facture {facture.numerofacture}</h1>
        <p>
          Client : <strong>{facture.nomClient}</strong> — Type : {facture.type} — Statut :{" "}
          <strong>{facture.statut}</strong>
        </p>
      </header>

      <div className="recapitulatif">
        <p>Total TTC : {facture.totalTtc} XOF</p>
        <p>Montant payé : {facture.montantPaye} XOF</p>
        <p>
          <strong>Reste à payer : {facture.resteAPayer} XOF</strong>
        </p>
      </div>

      <h2>Historique des règlements</h2>
      {facture.reglements && facture.reglements.length > 0 ? (
        <table>
          <thead>
            <tr>
              <th>Date</th>
              <th>Montant</th>
              <th>Mode</th>
            </tr>
          </thead>
          <tbody>
            {facture.reglements.map((r) => (
              <tr key={r.idReglement}>
                <td>{r.dateReglement}</td>
                <td>{r.montant} XOF</td>
                <td>{r.mode}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>Aucun règlement enregistré pour l'instant.</p>
      )}

      {peutEnregistrerReglement && (
        <>
          <h2>Enregistrer un règlement</h2>
          {erreurReglement && <p className="erreur">{erreurReglement}</p>}
          <form onSubmit={handleReglementSubmit} className="reglement-form">
            <div className="form-group">
              <label htmlFor="montant">Montant *</label>
              <input
                id="montant"
                type="number"
                step="0.01"
                value={montant}
                onChange={(e) => setMontant(e.target.value)}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="mode">Mode de règlement *</label>
              <select id="mode" value={mode} onChange={(e) => setMode(e.target.value)}>
                {MODES_REGLEMENT.map((m) => (
                  <option key={m} value={m}>
                    {m}
                  </option>
                ))}
              </select>
            </div>
            <button type="submit" disabled={actionEnCours}>
              Enregistrer le règlement
            </button>
          </form>
        </>
      )}

      <div className="form-actions">
        <button onClick={() => navigate("/factures")}>Retour à la liste</button>
        <button onClick={handleTelechargerPdf}>Télécharger le PDF</button>
        {peutAnnuler && (
          <button onClick={handleAnnuler} disabled={actionEnCours}>
            Annuler la facture
          </button>
        )}
      </div>
    </div>
  );
}

