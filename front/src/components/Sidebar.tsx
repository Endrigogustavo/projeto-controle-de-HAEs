import ArticleOutlinedIcon from "@mui/icons-material/ArticleOutlined";
import { useNavigate } from "react-router-dom";
import { useAuthForms } from "@/hooks/useAuthForms";
import { logout } from "@/services/auth";

export const Sidebar = () => {
	const navigate = useNavigate();
	const { handleLogout } = useAuthForms({ logout: logout });

	const onLogout = async () => {
		const result = await handleLogout();
		if (result) {
			navigate("/login");
		}
	};

	return (
		<>
			<aside>
				<div className="h-screen bg-gray-fatec flex flex-col items-center p-2">
					<img
						src="/fatec_zona_leste_icon_branco.png"
						alt="Logo da Fatec da Zona Leste"
						className="w-50 p-4"
					/>
					<nav className="w-full mx-2">
						<ul className="flex flex-col gap-2">
							<li className="text-white flex items-center gap-2">
								<ArticleOutlinedIcon sx={{ fill: "#ffffff" }} />
								<a href="/">Visão Geral</a>
							</li>
							<li className="text-white flex items-center gap-2">
								<ArticleOutlinedIcon sx={{ fill: "#ffffff" }} />
								<a href="/requesthae">Solicitar HAEs</a>
							</li>
							<li className="text-white flex items-center gap-2">
								<ArticleOutlinedIcon sx={{ fill: "#ffffff" }} />
								<a href="/myrequests">Minhas Solicitações</a>
							</li>
						</ul>
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
		</>
	);
};
