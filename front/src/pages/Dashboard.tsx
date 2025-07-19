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
				const userResponse = await api.get<Employee>("/employee/get-my-user");
				const professorId = userResponse.data.id;

				const haeResponse = await api.get<Hae[]>(`/hae/getHaesByProfessor/${professorId}`);

				setHaes(haeResponse.data);
			} catch (err: any) {
				console.error(err);
			}
		};

		fetchHaes();
	}, []);

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
