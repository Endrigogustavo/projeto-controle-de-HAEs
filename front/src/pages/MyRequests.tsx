import { Sidebar } from "@components/Sidebar";
import { Header } from "@components/Header";
import { CardRequestHae } from "@components/CardRequestHae";
import { MobileHeader } from "@components/MobileHeader";
import { useState } from "react";
import Drawer from "@mui/material/Drawer";

export default function MyRequests() {
	const [isDrawerOpen, setDrawerOpen] = useState(false);

	const toggleDrawer = (open: boolean) => () => {
		setDrawerOpen(open);
	};

	return (
		<div className="h-screen flex flex-col md:grid md:grid-cols-[20%_80%] md:grid-rows-[auto_1fr]">
			<div className="hidden md:block row-span-2">
				<Sidebar />
			</div>

			<div className="md:hidden">
				<MobileHeader onMenuClick={toggleDrawer(true)} />
			</div>

			<Drawer open={isDrawerOpen} onClose={toggleDrawer(false)}>
				<div className="w-64 h-full bg-gray-fatec">
					<Sidebar />
				</div>
			</Drawer>

			<div className="hidden md:block col-start-2 row-start-1">
				<Header />
			</div>

			<main className="col-start-2 row-start-2 p-4 overflow-auto bg-background pt-20 md:pt-4">
				<h2 className="subtitle">Minhas Solicitações</h2>
				<p>
					Nesta seção, você pode gerenciar suas solicitações de HAEs. Tenha a
					opção de editar ou excluir atividades, conforme sua necessidade.
				</p>
				<CardRequestHae
					titulo="Hae Muito legal"
					curso="Desenvolvimento de Sistemas"
					descricao="HAE legal"
				/>
				<CardRequestHae
					titulo="Outra Solicitação lá"
					curso="Logistica"
					descricao="HAE Não tao legal assim"
				/>
			</main>
		</div>
	);
}
