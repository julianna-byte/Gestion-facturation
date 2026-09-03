import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";
import { LayoutDashboard, Users, Package, FileText, Receipt } from "lucide-react";

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
              {/*import des icones des modules*/}
        <nav className="sidebar-links">
           {/*tableau de bord*/}
          <NavLink to="/dashboard" className="sidebar-link"><LayoutDashboard size={16}/> Tableau de bord</NavLink>

           {/*clients*/}
          <NavLink to="/clients" className="sidebar-link"><Users size={16} />Clients</NavLink>

           {/*catalogue*/}
          <NavLink to="/catalogue" className="sidebar-link"><Package size={16} />Catalogue</NavLink>

           {/*Bons de commande*/}
          <NavLink to="/bons-commande" className="sidebar-link"><FileText size={16} />Bons de commande</NavLink>

           {/*factures*/}
          <NavLink to="/factures" className="sidebar-link"><Receipt size={16} />Factures</NavLink>

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