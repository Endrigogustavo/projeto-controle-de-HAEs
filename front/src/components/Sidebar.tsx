import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";
import { useAuthForms } from "@/hooks/useAuthForms";
import { logout } from "@/services/auth";

import {
	ArticleOutlined,
	DashboardOutlined,
	PeopleOutline,
	BarChartOutlined,
	ListAltOutlined,
	AdminPanelSettingsOutlined,
} from "@mui/icons-material";
import React from "react";

const SidebarItem = ({
	to,
	icon,
	text,
}: {
	to: string;
	icon: React.ReactElement;
	text: string;
}) => (
	<li className="text-white flex items-center gap-2 px-2 py-1.5 rounded-md hover:bg-white/10 transition-colors">
		<span className="text-white">{icon}</span>
		<Link to={to} className="w-full">
			{text}
		</Link>
	</li>
);

export const Sidebar = () => {
	const navigate = useNavigate();
	const { user } = useAuth();
	const { handleLogout } = useAuthForms({ logout });

	const onLogout = async () => {
		if (await handleLogout()) {
			navigate("/login");
		}
	};

	const renderNavLinks = () => {
		if (!user) return null;

		switch (user.role) {
			case "PROFESSOR":
				return (
					<>
						<SidebarItem
							to="/dashboard"
							icon={<DashboardOutlined sx={{ fill: "white" }} />}
							text="Minha Visão Geral"
						/>
						<SidebarItem
							to="/requestHae"
							icon={<ArticleOutlined sx={{ fill: "white" }} />}
							text="Solicitar HAE"
						/>
						<SidebarItem
							to="/myrequests"
							icon={<ListAltOutlined sx={{ fill: "white" }} />}
							text="Minhas Solicitações"
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
						/>
						<SidebarItem
							to="/requestHae"
							icon={<ArticleOutlined sx={{ fill: "white" }} />}
							text="Solicitar HAE"
						/>
						<SidebarItem
							to="/myrequests"
							icon={<ListAltOutlined sx={{ fill: "white" }} />}
							text="Minhas Solicitações"
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
						/>
						<SidebarItem
							to="/gerenciar-usuarios"
							icon={<AdminPanelSettingsOutlined sx={{ fill: "white" }} />}
							text="Gerenciar Usuários"
						/>
						<SidebarItem
							to="/requestHae"
							icon={<ArticleOutlined sx={{ fill: "white" }} />}
							text="Solicitar HAE"
						/>
						<SidebarItem
							to="/myrequests"
							icon={<ListAltOutlined sx={{ fill: "white" }} />}
							text="Minhas Solicitações"
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
						/>
						<SidebarItem
							to="/haes"
							icon={<ListAltOutlined sx={{ fill: "white" }} />}
							text="Visualizar HAEs"
						/>
						<SidebarItem
							to="/professores"
							icon={<PeopleOutline sx={{ fill: "white" }} />}
							text="Visualizar Professores"
						/>
						<SidebarItem
							to="/coordenadores"
							icon={<PeopleOutline sx={{ fill: "white" }} />}
							text="Visualizar Coordenadores"
						/>
					</>
				);
			default:
				return null;
		}
	};

	return (
		<aside>
			<div className="h-screen bg-gray-fatec flex flex-col items-center p-2">
				<img
					src="/fatec_zona_leste_icon_branco.png"
					alt="Logo da Fatec da Zona Leste"
					className="w-50 p-4"
				/>
				<nav className="w-full mx-2">
					<ul className="flex flex-col gap-2">{renderNavLinks()}</ul>
				</nav>
				<div className="flex-1" />
				<button
					onClick={onLogout}
					className="btnFatec mb-4 px-3 py-2 text-white"
				>
					Sair da Conta
				</button>
			</div>
		</aside>
	);
};
