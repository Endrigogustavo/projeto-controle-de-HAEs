import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";
import { useAuthForms } from "@/hooks/useAuthForms";
import { authService } from "@/services";
import {
  ArticleOutlined,
  DashboardOutlined,
  PeopleOutline,
  BarChartOutlined,
  ListAltOutlined,
  AdminPanelSettingsOutlined,
  SettingsOutlined,
  AlternateEmail,
} from "@mui/icons-material";
import { SidebarItem } from "./SidebarItem";
import { useState, useEffect } from "react";
import { CircularProgress } from "@mui/material";

export const Sidebar = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const { user } = useAuth();
  const { handleLogout } = useAuthForms(authService);

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setLoading(false);
    }, 1000);

    return () => clearTimeout(timer);
  }, []);

  const onLogout = async () => {
    if (await handleLogout()) {
      navigate("/login");
    }
  };

  const renderNavLinks = () => {
    if (!user) return null;

    const currentPath = location.pathname;
    const isActive = (path: string) => currentPath === path;

    switch (user.role) {
      case "PROFESSOR":
        return (
          <>
            <SidebarItem
              to="/dashboard"
              icon={<DashboardOutlined sx={{ fill: "white" }} />}
              text="Minha Visão Geral"
              active={isActive("/dashboard")}
            />
            <SidebarItem
              to="/requestHae"
              icon={<ArticleOutlined sx={{ fill: "white" }} />}
              text="Solicitar HAE"
              active={isActive("/requestHae")}
            />
            <SidebarItem
              to="/myrequests"
              icon={<ListAltOutlined sx={{ fill: "white" }} />}
              text="Minhas Solicitações"
              active={isActive("/myrequests")}
            />
            <SidebarItem
              to="/support"
              icon={<AlternateEmail sx={{ fill: "white" }} />}
              text="Entre Em Contato"
              active={isActive("/support")}
            />
          </>
        );
      case "COORDENADOR":
        return (
          <>
            <SidebarItem
              to="/dashboard-coordenador"
              icon={<DashboardOutlined sx={{ fill: "white" }} />}
              text="Visão Geral (Curso)"
              active={isActive("/dashboard-coordenador")}
            />
            <SidebarItem
              to="/requestHae"
              icon={<ArticleOutlined sx={{ fill: "white" }} />}
              text="Solicitar HAE"
              active={isActive("/requestHae")}
            />
            <SidebarItem
              to="/myrequests"
              icon={<ListAltOutlined sx={{ fill: "white" }} />}
              text="Minhas Solicitações"
              active={isActive("/myrequests")}
            />
            <SidebarItem
              to="/support"
              icon={<AlternateEmail sx={{ fill: "white" }} />}
              text="Entre Em Contato"
              active={isActive("/support")}
            />
          </>
        );
      case "ADMIN":
        return (
          <>
            <SidebarItem
              to="/dashboard-admin"
              icon={<DashboardOutlined sx={{ fill: "white" }} />}
              text="Visão Geral"
              active={isActive("/dashboard-admin")}
            />
            <SidebarItem
              to="/gerenciar-usuarios"
              icon={<AdminPanelSettingsOutlined sx={{ fill: "white" }} />}
              text="Gerenciar Usuários"
              active={isActive("/gerenciar-usuarios")}
            />
            <SidebarItem
              to="/requestHae"
              icon={<ArticleOutlined sx={{ fill: "white" }} />}
              text="Solicitar HAE"
              active={isActive("/requestHae")}
            />
            <SidebarItem
              to="/myrequests"
              icon={<ListAltOutlined sx={{ fill: "white" }} />}
              text="Minhas Solicitações"
              active={isActive("/myrequests")}
            />
            <SidebarItem
              to="/support"
              icon={<AlternateEmail sx={{ fill: "white" }} />}
              text="Entre Em Contato"
              active={isActive("/support")}
            />
          </>
        );
      case "DIRETOR":
        return (
          <>
            <SidebarItem
              to="/dashboard-diretor"
              icon={<BarChartOutlined sx={{ fill: "white" }} />}
              text="Dashboard Geral"
              active={isActive("/dashboard-diretor")}
            />
            <SidebarItem
              to="/haes"
              icon={<ListAltOutlined sx={{ fill: "white" }} />}
              text="Visualizar HAEs"
              active={isActive("/haes")}
            />
            <SidebarItem
              to="/professores"
              icon={<PeopleOutline sx={{ fill: "white" }} />}
              text="Visualizar Professores"
              active={isActive("/professores")}
            />
            <SidebarItem
              to="/coordenadores"
              icon={<PeopleOutline sx={{ fill: "white" }} />}
              text="Visualizar Coordenadores"
              active={isActive("/coordenadores")}
            />
            <SidebarItem
              to="/configuracoes"
              icon={<SettingsOutlined sx={{ fill: "white" }} />}
              text="Configurações"
              active={isActive("/configuracoes")}
            />
            <SidebarItem
              to="/support"
              icon={<AlternateEmail sx={{ fill: "white" }} />}
              text="Entre Em Contato"
              active={isActive("/support")}
            />
          </>
        );
      default:
        return null;
    }
  };

  if (loading) {
    return (
      <div className="h-screen flex justify-center items-center bg-gray-fatec">
        <CircularProgress
          size={50}
          sx={{
            "& .MuiCircularProgress-circle": {
              stroke: "#fff", // Branco
            },
          }}
        />
      </div>
    );
  }

  return (
    <aside>
      <div className="h-screen bg-gray-fatec flex flex-col items-center p-2">
        <img
          src="/fatec_zona_leste_icon_branco.png"
          alt="Logo da Fatec da Zona Leste"
          className="w-50 p-4"
        />
        <nav className="w-full h-full items-center">
          <ul className="flex flex-col gap-2">{renderNavLinks()}</ul>
        </nav>

        <div className="flex-1" />
        <button
          onClick={onLogout}
          className="btnFatec mb-4 px-3 py-2 text-white uppercase bg-red-800 hover:bg-red-900"
        >
          Sair da Conta
        </button>
      </div>
    </aside>
  );
};
