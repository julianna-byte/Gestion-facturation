import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../services/authService";
import { useAuth } from "../context/useAuth";

export default function Login() {
  const [identifiant, setIdentifiant] = useState("");
  const [motdepasse, setMotdepasse] = useState("");
  const [erreur, setErreur] = useState("");
  const [chargement, setChargement] = useState(false);
  const { loginUser } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErreur("");
    setChargement(true);
    try {
      const data = await login(identifiant, motdepasse);
      loginUser(data);
      navigate("/dashboard");
    } catch (err) {
      const backendMessage = err.response?.data?.message;
      setErreur(backendMessage || "Identifiant ou mot de passe incorrect");
    } finally {
      setChargement(false);
    }
  };

  return (
    <div className="login-container">
      
        <div className="login-logo">
         <img src="/Logotype_Ossan Asur Noir.png" alt="OSSAN ASUR" />
         {/*<span>OSSAN ASUR</span>*/}
         <p>Gestion de la facturation</p>
        </div>
      

       <form onSubmit={handleSubmit}>
        {erreur && <p className="erreur">{erreur}</p>}

        <div className="form-group">
          <label htmlFor="identifiant">Identifiant</label>
          <input
            id="identifiant"
            type="text"
            value={identifiant}
            onChange={(e) => setIdentifiant(e.target.value)}
            required
            autoFocus
          />
        </div>

        <div className="form-group">
          <label htmlFor="motdepasse">Mot de passe</label>
          <input
            id="motdepasse"
            type="password"
            value={motdepasse}
            onChange={(e) => setMotdepasse(e.target.value)}
            required
          />
        </div>

        <button type="submit" disabled={chargement}>
          {chargement ? "Connexion..." : "Se connecter"}
        </button>
      </form>
      
    </div>
    
  );
}
