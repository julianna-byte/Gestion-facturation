import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/authContext";
import ProtectedRoute from "./components/ProtectedRoute";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import ClientList from "./pages/ClientList";
import ClientForm from "./pages/clientForm";
import ArticleList from "./pages/ArticleList";
import ArticleForm from "./pages/ArticleForm";
import BonCommandeList from "./pages/BonCommandeList";
import BonCommandeForm from "./pages/BonCommandeForm";
import BonCommandeDetail from "./pages/BonCommandeDetail";
import FactureList from "./pages/FactureList";
import FactureDetail from "./pages/FactureDetail";
import Layout from "./components/Layout";

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
               <Layout><Dashboard /></Layout> 
              </ProtectedRoute>
            }
          />

          <Route
           path="/clients"
           element={
           <ProtectedRoute>
             <Layout><ClientList /></Layout>
           </ProtectedRoute>
            }
          />

         <Route
           path="/clients/nouveau"
           element={
           <ProtectedRoute>
             <Layout><ClientForm /></Layout> 
            </ProtectedRoute>
            }
         />

          <Route
            path="/clients/:id"
            element={
            <ProtectedRoute>
              <Layout><ClientForm /></Layout>
            </ProtectedRoute>
           }
         />

          <Route
            path="/catalogue"
            element={
            <ProtectedRoute>
              <Layout><ArticleList /></Layout>
            </ProtectedRoute>
            }
          />

          <Route
            path="/catalogue/nouveau"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <Layout><ArticleForm /></Layout>
              </ProtectedRoute>
            }
          />


          <Route
            path="/catalogue/:id"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <Layout><ArticleForm /></Layout>
              </ProtectedRoute>
            }
          />

            <Route
            path="/catalogue/:id"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <Layout><ArticleForm /></Layout>
              </ProtectedRoute>
            }
          />

          <Route
            path="/bons-commande"
            element={
              <ProtectedRoute>
                <Layout><BonCommandeList /></Layout>
              </ProtectedRoute>
            }
          />

          <Route
            path="/bons-commande/nouveau"
            element={
              <ProtectedRoute>
                <Layout><BonCommandeForm /></Layout>
              </ProtectedRoute>
            }
          />

          <Route
            path="/bons-commande/:id"
            element={
              <ProtectedRoute>
                <Layout><BonCommandeDetail /></Layout>
              </ProtectedRoute>
            }
          />

          <Route
            path="/factures"
            element={
              <ProtectedRoute>
                <Layout><FactureList /></Layout>
              </ProtectedRoute>
            }
          />

           <Route
            path="/factures/:id"
            element={
              <ProtectedRoute>
                <Layout><FactureDetail /></Layout>
              </ProtectedRoute>
            }
          />
          
          <Route path="/acces-refuse" element={<div>Accès refusé</div>} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
