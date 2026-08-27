import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getArticleById, createArticle, updateArticle } from "../services/articleService";

const CHAMPS_VIDES = {
  code: "",
  libelle: "",
  description: "",
  unite: "",
  tauxTva: 18, // valeur par defaut selon RG-02 (18%), modifiable par article
  prixunitaireHT: "",
};

export default function ArticleForm() {
  const { id } = useParams();
  const estModification = !!id;

  const [formData, setFormData] = useState(CHAMPS_VIDES);
  const [erreurs, setErreurs] = useState({});
  const [erreurGenerale, setErreurGenerale] = useState("");
  const [chargement, setChargement] = useState(estModification);
  const [envoi, setEnvoi] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    if (estModification) {
      getArticleById(id)
        .then((data) => setFormData(data))
        .catch(() => setErreurGenerale("Impossible de charger cet article."))
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

    const payload = {
      ...formData,
      unite: Number(formData.unite),
      tauxTva: Number(formData.tauxTva),
      prixunitaireHT: Number(formData.prixunitaireHT),
    };

    try {
      if (estModification) {
        await updateArticle(id, { ...payload, idArticles: Number(id) });
      } else {
        await createArticle(payload);
      }
      navigate("/catalogue");
    } catch (err) {
      if (err.response?.status === 403) {
        setErreurGenerale("Action réservée aux administrateurs.");
      } else if (err.response?.status === 400 && err.response?.data) {
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
    <div className="article-form">
      <h1>{estModification ? "Modifier l'article" : "Nouvel article"}</h1>

      {erreurGenerale && <p className="erreur">{erreurGenerale}</p>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="code">Code *</label>
          <input id="code" name="code" value={formData.code} onChange={handleChange} required />
          {erreurs.code && <span className="erreur-champ">{erreurs.code}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="libelle">Libellé *</label>
          <input id="libelle" name="libelle" value={formData.libelle} onChange={handleChange} required />
          {erreurs.libelle && <span className="erreur-champ">{erreurs.libelle}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={formData.description || ""}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label htmlFor="unite">Unité *</label>
          <input
            id="unite"
            name="unite"
            type="number"
            value={formData.unite}
            onChange={handleChange}
            required
          />
          {erreurs.unite && <span className="erreur-champ">{erreurs.unite}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="prixunitaireHT">Prix unitaire HT *</label>
          <input
            id="prixunitaireHT"
            name="prixunitaireHT"
            type="number"
            step="0.01"
            value={formData.prixunitaireHT}
            onChange={handleChange}
            required
          />
          {erreurs.prixunitaireHT && <span className="erreur-champ">{erreurs.prixunitaireHT}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="tauxTva">Taux de TVA (%) *</label>
          <input
            id="tauxTva"
            name="tauxTva"
            type="number"
            step="0.01"
            value={formData.tauxTva}
            onChange={handleChange}
            required
          />
          {erreurs.tauxTva && <span className="erreur-champ">{erreurs.tauxTva}</span>}
        </div>

        <div className="form-actions">
          <button type="button" onClick={() => navigate("/catalogue")}>
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
