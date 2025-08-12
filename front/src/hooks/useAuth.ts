import { api } from "@/services";
import { useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";

export interface LoggedUser {
  id: string;
  name: string;
  email: string;
  role: "PROFESSOR" | "COORDENADOR" | "ADMIN" | "DIRETOR";
  course: string;
}

/**
 * Este hook é a fonte única de verdade sobre o estado da autenticação na aplicação.
 * Ele verifica quem está logado ao carregar a página e fornece esses dados para
 * qualquer componente que precisar.
 */
export const useAuth = () => {
  const [user, setUser] = useState<LoggedUser | null>(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const fetchUser = useCallback(async () => {
    const email = localStorage.getItem("email");
    const token = localStorage.getItem("token");

    if (!email || !token) {
      setLoading(false);
      setUser(null);
      return;
    }

    try {
      const response = await api.get<LoggedUser>(
        `/employee/get-professor?email=${email}`
      );
      setUser(response.data);
    } catch (error) {
      console.error("Sessão inválida ou erro ao buscar usuário:", error);
      localStorage.removeItem("token");
      localStorage.removeItem("email");
      setUser(null);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchUser();
  }, [fetchUser]);

  const logout = useCallback(() => {
    localStorage.removeItem("token");
    localStorage.removeItem("email");
    setUser(null);
    navigate("/login");
  }, [navigate]);

  return { user, loading, logout };
};
