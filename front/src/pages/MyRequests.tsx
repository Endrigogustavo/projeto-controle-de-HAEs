import { useEffect, useState } from "react";
import { Sidebar } from "@components/Sidebar";
import { Header } from "@components/Header";
import { CardRequestHae } from "@components/CardRequestHae";
import { MobileHeader } from "@components/MobileHeader";
import Drawer from "@mui/material/Drawer";
import { Employee } from "@/types/employee";
import api from "@/services";
import { Hae } from "@/types/hae";

export default function MyRequests() {
	const [isDrawerOpen, setDrawerOpen] = useState(false);
	const [haes, setHaes] = useState<Hae[]>([]);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState("");

	const toggleDrawer = (open: boolean) => () => {
		setDrawerOpen(open);
	};

	useEffect(() => {
		const fetchHaes = async () => {
			try {
				setLoading(true);
				const email = localStorage.getItem("email");

				const userResponse = await api.get<Employee>(
					`/employee/get-professor?email=${email}`
				);
				const professorId = userResponse.data.id;

				const haeResponse = await api.get<Hae[]>(
					`/hae/getHaesByProfessor/${professorId}`
				);

				const filteredHaes = haeResponse.data.filter(
					(hae) => hae.status === "PENDENTE"
				);
				setHaes(filteredHaes);
			} catch (err: unknown) {
				console.error(err);
				setError("Erro ao carregar as HAEs");
			} finally {
				setLoading(false);
			}
		};

		fetchHaes();
	}, []);

	const handleDelete = async (id: string) => {
		try {
			await api.delete(`/hae/delete/${id}`);
			setHaes((prev) => prev.filter((hae) => hae.id !== id));
		} catch (error) {
			console.error("Erro ao deletar HAE:", error);
			alert("Não foi possível deletar a solicitação.");
		}
	};

	return (
		<div className="h-screen flex flex-col md:grid md:grid-cols-[20%_80%] md:grid-rows-[auto_1fr]">
			<div className="sidebar">
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
			<div className="header">
				<Header />
			</div>

			<main className="col-start-2 row-start-2 p-4 overflow-auto bg-background pt-20 md:pt-4">
				<h2 className="subtitle">Minhas Solicitações</h2>
				<p>
					Nesta seção, você pode gerenciar suas solicitações de HAEs com status
					"Pendente". Tenha a opção de editar ou excluir atividades, conforme
					sua necessidade.
				</p>

				{loading && <p>Carregando HAEs...</p>}
				{error && <p className="text-red-500">{error}</p>}
				{!loading && haes.length === 0 && (
					<p className="mt-4">Você não possui solicitações pendentes.</p>
				)}

				{haes.map((hae) => (
					<CardRequestHae
						key={hae.id}
						id={hae.id}
						titulo={hae.projectTitle}
						curso={hae.course}
						descricao={hae.projectDescription}
						onDelete={() => handleDelete(hae.id)}
					/>
				))}
			</main>
		</div>
	);
}
