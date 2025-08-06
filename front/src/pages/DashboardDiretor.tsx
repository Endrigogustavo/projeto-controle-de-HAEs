import { useEffect, useState } from "react";
import { Sidebar } from "@/components/Sidebar";
import { Header } from "@/components/Header";
import { MobileHeader } from "@/components/MobileHeader";
import Drawer from "@mui/material/Drawer";
import api from "@/services";
import {
	Chart as ChartJS,
	ArcElement,
	Tooltip,
	Legend,
	CategoryScale,
	LinearScale,
	BarElement,
	Title,
} from "chart.js";
import { Pie, Bar } from "react-chartjs-2";
import { Hae } from "@/types/hae";

ChartJS.register(
	ArcElement,
	Tooltip,
	Legend,
	CategoryScale,
	LinearScale,
	BarElement,
	Title
);

export default function DashboardDiretor() {
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

	const statusData = {
		labels: ["Pendentes", "Aprovadas", "Reprovadas", "Completas"],
		datasets: [
			{
				label: "# de HAEs",
				data: [
					haes.filter((h) => h.status === "PENDENTE").length,
					haes.filter((h) => h.status === "APROVADO").length,
					haes.filter((h) => h.status === "REPROVADO").length,
					haes.filter((h) => h.status === "COMPLETO").length,
				],
				backgroundColor: ["#f59e0b", "#10b981", "#ef4444", "#3b82f6"],
			},
		],
	};

	const haesPorCurso = haes.reduce((acc: Record<string, number>, hae) => {
		acc[hae.course] = (acc[hae.course] || 0) + 1;
		return acc;
	}, {});

	const courseData = {
		labels: Object.keys(haesPorCurso),
		datasets: [
			{
				label: "Total de HAEs por Curso",
				data: Object.values(haesPorCurso),
				backgroundColor: "#8b5cf6",
			},
		],
	};

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
				<h2 className="subtitle">Dashboard Geral</h2>
				<p className="text-gray-600 mb-6">
					Análise de todas as Horas de Atividades Específicas do sistema.
				</p>

				{loading ? (
					<p>Carregando dados...</p>
				) : (
					<div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
						<div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
							<h3 className="font-semibold text-lg text-gray-700 mb-4">
								Distribuição por Status
							</h3>
							<Pie data={statusData} />
						</div>
						<div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
							<h3 className="font-semibold text-lg text-gray-700 mb-4">
								Volume por Curso
							</h3>
							<Bar
								data={courseData}
								options={{ indexAxis: "y", responsive: true }}
							/>
						</div>
					</div>
				)}
			</main>
		</div>
	);
}
