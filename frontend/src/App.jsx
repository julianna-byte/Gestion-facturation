import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/authContext";
import ProtectedRoute from "./components/ProtectedRoute";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import ClientList from "./pages/ClientList";
import ClientForm from "./pages/clientForm";
import ArticleList from "./pages/ArticleList";
import ArticleForm from "./pages/ArticleForm";
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

          <Route
           path="/clients"
           element={
           <ProtectedRoute>
             <ClientList />
           </ProtectedRoute>
            }
          />

         <Route
           path="/clients/nouveau"
           element={
           <ProtectedRoute>
              <ClientForm />
            </ProtectedRoute>
            }
         />

          <Route
            path="/clients/:id"
            element={
            <ProtectedRoute>
              <ClientForm />
            </ProtectedRoute>
           }
         />

          <Route
            path="/catalogue"
            element={
            <ProtectedRoute>
              <ArticleList />
            </ProtectedRoute>
            }
          />

          <Route
            path="/catalogue/nouveau"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <ArticleForm />
              </ProtectedRoute>
            }
          />


          <Route
            path="/catalogue/:id"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <ArticleForm />
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
