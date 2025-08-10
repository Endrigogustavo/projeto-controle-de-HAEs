import { useEffect, useState } from "react";
import { Sidebar } from "@/components/Sidebar";
import { Header } from "@/components/Header";
import { MobileHeader } from "@/components/MobileHeader";
import Drawer from "@mui/material/Drawer";
import api from "@/services";
import { useAuth } from "@/hooks/useAuth";
import {
	CircularProgress,
	Box,
	TextField,
	Button,
	Snackbar,
	Alert,
	Typography,
} from "@mui/material";

export default function Configuracoes() {
	const [isDrawerOpen, setDrawerOpen] = useState(false);

	const [currentLimit, setCurrentLimit] = useState<number | null>(null);
	const [newLimit, setNewLimit] = useState<string>("");

	const [loading, setLoading] = useState(true);
	const [isSubmitting, setIsSubmitting] = useState(false);
	const [snackbar, setSnackbar] = useState<{
		open: boolean;
		message: string;
		severity: "success" | "error";
	} | null>(null);

	const { user } = useAuth();

	useEffect(() => {
		const fetchCurrentLimit = async () => {
			try {
				const response = await api.get<number>("/hae/getAvailableHaesCount");
				setCurrentLimit(response.data);
				setNewLimit(response.data.toString());
			} catch (err) {
				console.error("Erro ao buscar o limite de HAEs:", err);
				setSnackbar({
					open: true,
					message: "Falha ao carregar a configuração atual.",
					severity: "error",
				});
			} finally {
				setLoading(false);
			}
		};
		fetchCurrentLimit();
	}, []);

	const handleSave = async (event: React.FormEvent) => {
		event.preventDefault();
		if (!user || !user.id) {
			setSnackbar({
				open: true,
				message: "Sua identidade não pôde ser verificada. Tente novamente.",
				severity: "error",
			});
			return;
		}

		const count = parseInt(newLimit, 10);
		if (isNaN(count) || count < 0) {
			setSnackbar({
				open: true,
				message: "Por favor, insira um número válido.",
				severity: "error",
			});
			return;
		}

		setIsSubmitting(true);
		try {
			await api.post(
				`/hae/setAvailableHaesCount?count=${count}&usuarioId=${user.id}`
			);

			setCurrentLimit(count);
			setSnackbar({
				open: true,
				message: "Limite de HAEs atualizado com sucesso!",
				severity: "success",
			});
		} catch (error: any) {
			console.error("Erro ao definir o limite de HAEs:", error);
			const errorMessage =
				error.response?.data?.message || "Falha ao salvar a configuração.";
			setSnackbar({ open: true, message: errorMessage, severity: "error" });
		} finally {
			setIsSubmitting(false);
		}
	};

	if (loading) {
		return (
			<Box
				display="flex"
				alignItems="center"
				justifyContent="center"
				height="100vh"
			>
				<CircularProgress />
			</Box>
		);
	}

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
				<h2 className="subtitle">Configurações do Sistema</h2>
				<p className="text-gray-600 mb-6">
					Ajuste os parâmetros globais do sistema de HAEs.
				</p>

				<div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 max-w-md">
					<form onSubmit={handleSave} className="flex flex-col gap-4">
						<Typography variant="h6" className="font-semibold text-gray-800">
							Limite de HAEs por Semestre
						</Typography>
						<Typography variant="body2" className="text-gray-500 mb-2">
							Defina o número máximo de HAEs que um professor pode solicitar por
							semestre. O limite atual é:{" "}
							<span className="font-bold text-blue-600">
								{currentLimit ?? "..."}
							</span>
						</Typography>

						<TextField
							fullWidth
							label="Novo Limite de HAEs"
							type="number"
							variant="outlined"
							value={newLimit}
							onChange={(e) => setNewLimit(e.target.value)}
							InputProps={{ inputProps: { min: 0 } }}
							required
						/>

						<Button
							type="submit"
							variant="contained"
							disabled={isSubmitting}
							sx={{ mt: 2 }}
						>
							{isSubmitting ? "Salvando..." : "Salvar Alterações"}
						</Button>
					</form>
				</div>
			</main>

			<Snackbar
				open={snackbar?.open}
				autoHideDuration={6000}
				onClose={() => setSnackbar(null)}
				anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
			>
				<Alert
					onClose={() => setSnackbar(null)}
					severity={snackbar?.severity}
					sx={{ width: "100%" }}
				>
					{snackbar?.message}
				</Alert>
			</Snackbar>
		</div>
	);
}
