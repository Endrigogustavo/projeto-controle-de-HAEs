import { Sidebar } from "@components/Sidebar";
import { Header } from "@components/Header";
import { CardHae } from "@components/CardHae";
import { MobileHeader } from "@components/MobileHeader";
import { useEffect, useState } from "react";
import Drawer from "@mui/material/Drawer";
import api from "@/services";
import { Hae } from "@/types/hae";
import { Employee } from "@/types/employee";

export default function Dashboard() {
	const [isDrawerOpen, setDrawerOpen] = useState(false);
	const [haes, setHaes] = useState<Hae[]>([]);

	const toggleDrawer = (open: boolean) => () => {
		setDrawerOpen(open);
	};

	useEffect(() => {
		const fetchHaes = async () => {
			try {
				const email = localStorage.getItem("email");

				const userResponse = await api.get<Employee>(
					`/employee/get-professor?email=${email}`
				);

				const professorId = userResponse.data.id;

				const haeResponse = await api.get<Hae[]>(
					`/hae/getHaesByProfessor/${professorId}`
				);

				setHaes(haeResponse.data);
			} catch (err: unknown) {
				console.error(err);
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

			<main className="main-content">
				<h2 className="subtitle">Visão Geral HAEs</h2>
				<p>
					Aqui você encontra a lista das suas HAEs solicitadas e o status de
					cada uma. Acompanhe se sua atividade já foi aprovada pela coordenação
					ou se aguarda avaliação.
				</p>

				{haes.map((hae) => (
					<CardHae
						key={hae.id}
						titulo={hae.projectTitle}
						curso={hae.course}
						descricao={hae.projectDescription}
						status={hae.status}
					/>
				))}
			</main>
		</div>
	);
}
