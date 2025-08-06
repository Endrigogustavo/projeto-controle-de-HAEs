import { useEffect, useState } from "react";
import { Sidebar } from "@/components/Sidebar";
import { Header } from "@/components/Header";
import { MobileHeader } from "@/components/MobileHeader";
import Drawer from "@mui/material/Drawer";
import api from "@/services";
import { EmployeeSummary } from "@/types/employee";

export default function Professores() {
	const [isDrawerOpen, setDrawerOpen] = useState(false);
	const [professores, setProfessores] = useState<EmployeeSummary[]>([]);
	const [loading, setLoading] = useState(true);

	useEffect(() => {
		const fetchProfessores = async () => {
			try {
				const response = await api.get<EmployeeSummary[]>(
					"employee/getAllByRole/PROFESSOR"
				);
				setProfessores(response.data);
			} catch (err) {
				console.error("Erro ao buscar professores:", err);
			} finally {
				setLoading(false);
			}
		};
		fetchProfessores();
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
				<h2 className="subtitle">Visão Geral de Professores</h2>
				<p className="text-gray-600 mb-6">
					Lista de todos os professores cadastrados e suas atividades.
				</p>

				<div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-x-auto">
					<table className="w-full text-sm text-left text-gray-500">
						<thead className="text-xs text-gray-700 uppercase bg-gray-100">
							<tr>
								<th scope="col" className="px-6 py-3">
									Nome
								</th>
								<th scope="col" className="px-6 py-3">
									E-mail
								</th>
								<th scope="col" className="px-6 py-3">
									Curso
								</th>
								<th scope="col" className="px-6 py-3">
									HAEs Ativas
								</th>
							</tr>
						</thead>
						<tbody>
							{loading ? (
								<tr>
									<td colSpan={4} className="text-center p-4">
										Carregando...
									</td>
								</tr>
							) : (
								professores.map((prof) => (
									<tr
										key={prof.id}
										className="bg-white border-b hover:bg-gray-50"
									>
										<th
											scope="row"
											className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap"
										>
											{prof.name}
										</th>
										<td className="px-6 py-4">{prof.email}</td>
										<td className="px-6 py-4">{prof.course}</td>
										<td className="px-6 py-4 text-center">{prof.haeCount}</td>
									</tr>
								))
							)}
						</tbody>
					</table>
				</div>
			</main>
		</div>
	);
}
