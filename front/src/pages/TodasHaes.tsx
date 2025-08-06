import { useEffect, useState } from "react";
import { Sidebar } from "@/components/Sidebar";
import { Header } from "@/components/Header";
import { MobileHeader } from "@/components/MobileHeader";
import Drawer from "@mui/material/Drawer";
import api from "@/services";
import { CardHaeCoordenador } from "@/components/CardHaeCoordenador";
import { Hae } from "@/types/hae";

export default function TodasHaes() {
	const [isDrawerOpen, setDrawerOpen] = useState(false);
	const [haes, setHaes] = useState<Hae[]>([]);
	const [loading, setLoading] = useState(true);

	useEffect(() => {
		const fetchHaes = async () => {
			try {
				const response = await api.get<Hae[]>("/hae/getAll");
				setHaes(response.data);
			} catch (err) {
				console.error("Erro ao buscar HAEs:", err);
			} finally {
				setLoading(false);
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
				<MobileHeader onMenuClick={() => setDrawerOpen(true)} />
			</div>
			<Drawer open={isDrawerOpen} onClose={() => setDrawerOpen(false)}>
				<div className="w-64 h-full bg-gray-fatec">
					<Sidebar />
				</div>
			</Drawer>
			<div className="hidden md:block col-start-2 row-start-1">
				<Header />
			</div>

			<main className="col-start-2 row-start-2 p-4 md:p-8 overflow-auto bg-gray-50 pt-20 md:pt-4">
				<h2 className="subtitle">Todas as HAEs do Sistema</h2>
				<p className="text-gray-600 mb-6">
					Lista completa de todas as HAEs cadastradas.
				</p>

				{loading ? (
					<p>Carregando...</p>
				) : (
					<div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
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
