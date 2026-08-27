import { Navigate } from "react-router-dom";
import { useAuth } from "../context/useAuth";

// Usage simple : <ProtectedRoute><Dashboard /></ProtectedRoute>
// Usage avec restriction de role (ex: page Catalogue reservee ADMIN,
// coherent avec SecurityConfig.java : POST/PUT/DELETE articles = ADMIN) :
// <ProtectedRoute requiredRole="ADMIN"><ArticlesAdmin /></ProtectedRoute>
function ProtectedRoute({ children, requiredRole }) {
  const { isAuthenticated, role } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (requiredRole && role !== requiredRole) {
    // Connecte mais role insuffisant -> equivalent du 403 backend
    return <Navigate to="/acces-refuse" replace />;
  }

  return children;
}

export default ProtectedRoute;
