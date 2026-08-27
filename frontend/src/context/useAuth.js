import { useContext } from "react";

// 🔴 On récupère le contexte créé dans authContext.jsx
import { AuthContext } from "./authContext";

export function useAuth() {
  return useContext(AuthContext);
}
