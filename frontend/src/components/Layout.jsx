import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";

export default function Layout({ children }) {
  const { role, logoutUser } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logoutUser();
    navigate("/login");
  };

  return (
    <div className="layout">
      <nav className="navbar">
        <div className="navbar-links">
          <Link to="/dashboard">Tableau de bord</Link>
          <Link to="/clients">Clients</Link>
          <Link to="/catalogue">Catalogue</Link>
          <Link to="/bons-commande">Bons de commande</Link>
          <Link to="/factures">Factures</Link>
        </div>
        <div className="navbar-user">
          <span>Rôle : {role}</span>
          <button onClick={handleLogout}>Déconnexion</button>
        </div>
      </nav>

      <main className="layout-content">{children}</main>
    </div>
  );
}
