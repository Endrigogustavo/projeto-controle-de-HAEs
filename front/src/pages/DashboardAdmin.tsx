import { Header } from "@components/Header";
import { MobileHeader } from "@components/MobileHeader";
import { useEffect, useState } from "react";
import Drawer from "@mui/material/Drawer";
import api from "@/services";
import { Hae } from "@/types/hae";
import { Sidebar } from "@/components/Sidebar";
import { CardHaeCoordenador } from "@/components/CardHaeCoordenador";

export default function DashboardAdmin() {
	const [isDrawerOpen, setDrawerOpen] = useState(false);
	const [haes, setHaes] = useState<Hae[]>([]);

	const toggleDrawer = (open: boolean) => () => {
		setDrawerOpen(open);
	};

	useEffect(() => {
		const fetchHaes = async () => {
			try {
				const haeResponse = await api.get<Hae[]>("/hae/getAll");
				setHaes(haeResponse.data);
			} catch (err: any) {
				console.error("Erro ao buscar HAEs:", err);
			}
		};

		fetchHaes();
	}, []);

	return (
		<div className="layout-container">
			<div className="sidebar">
				<Sidebar />
			</div>

			<div className="mobile-header">
				<MobileHeader onMenuClick={toggleDrawer(true)} />
			</div>

			<Drawer open={isDrawerOpen} onClose={toggleDrawer(false)}>
				<div className="drawer-sidebar">
					<Sidebar />
				</div>
			</Drawer>

			<div className="header">
				<Header />
			</div>

			<main className="col-start-2 row-start-2 p-4 overflow-auto bg-background pt-20 md:pt-4">
				<h2 className="subtitle">Visão Geral das HAEs (Admin)</h2>
				<p>
					Abaixo estão listadas todas as HAEs submetidas pelos professores. Você
					pode acompanhá-las e realizar aprovações ou revisões conforme
					necessário.
				</p>

				{haes.length === 0 ? (
					<p className="mt-4 text-gray-500">Nenhuma HAE encontrada.</p>
				) : (
					<div className="grid gap-4 mt-4">
						{haes.map((hae) => (
							<CardHaeCoordenador
								key={hae.id}
								id={hae.id}
								titulo={hae.projectTitle}
								curso={hae.course}
								descricao={hae.projectDescription}
								status={hae.status}
								professor={hae.employee.name}
							/>
						))}
					</div>
				)}
			</main>
		</div>
	);
}
