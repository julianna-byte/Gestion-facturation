import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";

export default function Layout({ children }) {
  const { role, logoutUser } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logoutUser();
    navigate("/login");
  };

  return (
    <div className="layout-with-sidebar">
      <aside className="sidebar">
        <div className="sidebar-logo">
          <img 
          src="/Logotype_Ossan_Assur.png"
           alt="OSSAN ASUR"
            className="sidebar-logo-img" 
            style={{width:"140px", height:"auto"}} />
           {/*<span>OSSAN ASUR</span>*/}
        </div>

        <nav className="sidebar-links">
          <NavLink to="/dashboard" className="sidebar-link">
            Tableau de bord
          </NavLink>
          <NavLink to="/clients" className="sidebar-link">
            Clients
          </NavLink>
          <NavLink to="/catalogue" className="sidebar-link">
            Catalogue
          </NavLink>
          <NavLink to="/bons-commande" className="sidebar-link">
            Bons de commande
          </NavLink>
          <NavLink to="/factures" className="sidebar-link">
            Factures
          </NavLink>
        </nav>

        <div className="sidebar-user">
          <span>{role}</span>
          <button onClick={handleLogout}>Déconnexion</button>
        </div>
      </aside>

      <main className="layout-content">{children}</main>
    </div>
  );
}