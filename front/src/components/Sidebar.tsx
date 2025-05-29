import ArticleOutlinedIcon from "@mui/icons-material/ArticleOutlined";

export const Sidebar = () => {
	return (
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
				<button className="btnFatec mb-4 px-3 py-2">
					<a>Sair da Conta</a>
				</button>
			</div>
		</aside>
	);
};
