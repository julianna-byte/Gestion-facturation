import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getClientById, createClient, updateClient } from "../services/clientService";

const CHAMPS_VIDES = {
  raisonsociale: "",
  nif: "",
  rccm: "",
  adresse: "",
  ville: "",
  pays: "",
  telephone: "",
  email: "",
  nomcontact: "",
};

export default function ClientForm() {
  const { id } = useParams(); // present uniquement en mode modification
  const estModification = !!id;

  const [formData, setFormData] = useState(CHAMPS_VIDES);
  const [erreurs, setErreurs] = useState({});
  const [erreurGenerale, setErreurGenerale] = useState("");
  const [chargement, setChargement] = useState(estModification);
  const [envoi, setEnvoi] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    if (estModification) {
      getClientById(id)
        .then((data) => setFormData(data))
        .catch(() => setErreurGenerale("Impossible de charger ce client."))
        .finally(() => setChargement(false));
    }
  }, [id, estModification]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErreurGenerale("");
    setErreurs({});
    setEnvoi(true);

    try {
      if (estModification) {
        await updateClient(id, { ...formData, idClient: Number(id) });
      } else {
        await createClient(formData);
      }
      navigate("/clients");
    } catch (err) {
      if (err.response?.status === 400 && err.response?.data) {
        setErreurs(err.response.data);
      } else {
        setErreurGenerale("Une erreur est survenue lors de l'enregistrement.");
      }
    } finally {
      setEnvoi(false);
    }
  };

  if (chargement) {
    return <div>Chargement...</div>;
  }

  return (
    <div className="client-form">
      <h1>{estModification ? "Modifier le client" : "Nouveau client"}</h1>

      {erreurGenerale && <p className="erreur">{erreurGenerale}</p>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="raisonsociale">Raison sociale *</label>
          <input
            id="raisonsociale"
            name="raisonsociale"
            value={formData.raisonsociale}
            onChange={handleChange}
            required
          />
          {erreurs.raisonsociale && <span className="erreur-champ">{erreurs.raisonsociale}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="nif">NIF *</label>
          <input id="nif" name="nif" value={formData.nif} onChange={handleChange} required />
          {erreurs.nif && <span className="erreur-champ">{erreurs.nif}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="rccm">RCCM *</label>
          <input id="rccm" name="rccm" value={formData.rccm} onChange={handleChange} required />
          {erreurs.rccm && <span className="erreur-champ">{erreurs.rccm}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="adresse">Adresse *</label>
          <input id="adresse" name="adresse" value={formData.adresse} onChange={handleChange} required />
          {erreurs.adresse && <span className="erreur-champ">{erreurs.adresse}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="ville">Ville *</label>
          <input id="ville" name="ville" value={formData.ville} onChange={handleChange} required />
          {erreurs.ville && <span className="erreur-champ">{erreurs.ville}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="pays">Pays *</label>
          <input id="pays" name="pays" value={formData.pays} onChange={handleChange} required />
          {erreurs.pays && <span className="erreur-champ">{erreurs.pays}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="telephone">Téléphone *</label>
          <input id="telephone" name="telephone" value={formData.telephone} onChange={handleChange} required />
          {erreurs.telephone && <span className="erreur-champ">{erreurs.telephone}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="email">Email *</label>
          <input
            id="email"
            name="email"
            type="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
          {erreurs.email && <span className="erreur-champ">{erreurs.email}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="nomcontact">Nom du contact *</label>
          <input
            id="nomcontact"
            name="nomcontact"
            value={formData.nomcontact}
            onChange={handleChange}
            required
          />
          {erreurs.nomcontact && <span className="erreur-champ">{erreurs.nomcontact}</span>}
        </div>

        <div className="form-actions">
          <button type="button" onClick={() => navigate("/clients")}>
            Annuler
          </button>
          <button type="submit" disabled={envoi}>
            {envoi ? "Enregistrement..." : "Enregistrer"}
          </button>
        </div>
      </form>
    </div>
  );
}
