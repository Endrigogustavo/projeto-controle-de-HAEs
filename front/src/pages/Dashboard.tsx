import { Sidebar } from "@components/Sidebar";
import { Header } from "@components/Header";
import { CardHae } from "@components/CardHae";
import { MobileHeader } from "@components/MobileHeader";
import { useEffect, useState } from "react";
import Drawer from "@mui/material/Drawer";
import api from "@/services";
import { Employee } from "@/types/employee";
import { Hae } from "@/types/hae";

export default function Dashboard() {
	const [isDrawerOpen, setDrawerOpen] = useState(false);
	const [haes, setHaes] = useState<Hae[]>([]);
	const [loading, setLoading] = useState(true);

	const toggleDrawer = (open: boolean) => () => {
		setDrawerOpen(open);
	};

	useEffect(() => {
		const fetchHaes = async () => {
			setLoading(true);
			try {
				const email = localStorage.getItem("email");
				if (!email) {
					throw new Error("E-mail não encontrado no localStorage");
				}
				const userResponse = await api.get<Employee>(
					`/employee/get-professor?email=${email}`
				);
				const professorId = userResponse.data.id;
				const haeResponse = await api.get<Hae[]>(
					`/hae/getHaesByProfessor/${professorId}`
				);
				setHaes(haeResponse.data);
			} catch (err: unknown) {
				console.error("Erro ao carregar HAEs do professor:", err);
			} finally {
				setLoading(false);
			}
		};

		fetchHaes();
	}, []);

	return (
		<div className="h-screen flex flex-col md:grid md:grid-cols-[20%_80%] md:grid-rows-[auto_1fr]">
			<div className="sidebar hidden md:block row-span-2">
				<Sidebar />
			</div>

			<div className="mobile-header md:hidden">
				<MobileHeader onMenuClick={toggleDrawer(true)} />
			</div>

			<Drawer open={isDrawerOpen} onClose={toggleDrawer(false)}>
				<div className="drawer-sidebar w-64 h-full bg-gray-fatec">
					<Sidebar />
				</div>
			</Drawer>

			<div className="header hidden md:block col-start-2 row-start-1">
				<Header />
			</div>

			<main className="main-content col-start-2 row-start-2 p-4 md:p-8 overflow-auto bg-gray-50 pt-20 md:pt-4">
				<h2 className="subtitle">Visão Geral das Minhas HAEs</h2>
				<p className="text-gray-600 mb-6">
					Aqui você encontra a lista das suas HAEs solicitadas e o status de
					cada uma.
				</p>

				{loading && <p>Carregando suas HAEs...</p>}
				{!loading && haes.length === 0 && (
					<p className="mt-4 text-gray-500">
						Você ainda não solicitou nenhuma HAE.
					</p>
				)}

				<div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
					{haes.map((hae) => (
						<CardHae
							key={hae.id}
							id={hae.id}
							titulo={hae.projectTitle}
							curso={hae.course}
							descricao={hae.projectDescription}
							status={hae.status}
						/>
					))}
				</div>
			</main>
		</div>
	);
}
