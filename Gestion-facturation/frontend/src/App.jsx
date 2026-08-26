import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/authContext";
import ProtectedRoute from "./components/ProtectedRoute";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
//  ClientList, ArticlesAdmin, etc. au fur et a mesure

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />

          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            }
          />

          {/* Exemple de route reservee ADMIN, a decommenter/adapter
              quand l'ecran catalogue existera :
          <Route
            path="/catalogue"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <ArticlesAdmin />
              </ProtectedRoute>
            }
          />
          */}

          <Route path="/acces-refuse" element={<div>Accès refusé</div>} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
