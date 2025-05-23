import { useForm, SubmitHandler } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { TextField, Snackbar, Alert } from "@mui/material";
import { PasswordField } from "../components/PasswordField";
import { registerSchema } from "../validation/registerSchema";
import { register as registerApi } from "../api/auth";
import { useNavigate } from "react-router-dom";
import {useState} from "react";

type FormData = {
	name: string;
	email: string;
	password: string;
};

export default function Register() {
	const {
		register,
		handleSubmit,
		formState: { errors, isSubmitting },
	} = useForm<FormData>({
		resolver: yupResolver(registerSchema),
	});

	const navigate = useNavigate();

	// Estados para controlar o Snackbar
	const [openSnackbar, setOpenSnackbar] = useState(false);
	const [snackbarMessage, setSnackbarMessage] = useState("");
	const [snackbarSeverity, setSnackbarSeverity] = useState<
		"error" | "success" | "info" | "warning"
	>("error");

	const handleCloseSnackbar = (
		event?: React.SyntheticEvent | Event,
		reason?: string
	) => {
		if (reason === "clickaway") {
			return;
		}
		setOpenSnackbar(false);
	};

	const onSubmit: SubmitHandler<FormData> = async (data) => {
		try {
			await registerApi(data);
			setSnackbarMessage(
				"Cadastro realizado com sucesso! Verifique seu e-mail."
			);
			navigate("/verificationCode");
		} catch (error: any) {
			console.error("Erro ao cadastrar:", error);
			setSnackbarMessage(
				error.response?.data?.message ||
					error.message ||
					"Erro desconhecido ao cadastrar."
			);
			setSnackbarSeverity("error");
			setOpenSnackbar(true);
		}
	};

	return (
		<>
			<div className="flex flex-col items-center justify-center h-screen">
				<div className="p-8 xl:w-xl">
					<img
						src="/fatec_zona_leste_icon.png"
						alt="Logo da Fatec da Zona Leste"
						className="mb-4 w-24"
					/>
					<h1 className="text-xl my-4 font-semibold">
						Sistema de Controle de
						<span className="text-red-fatec"> HAEs</span>
					</h1>

					<form
						className="flex flex-col"
						onSubmit={handleSubmit(onSubmit)}
						noValidate
					>
						<TextField
							label="Nome"
							{...register("name")}
							error={!!errors.name}
							helperText={errors.name?.message}
							required
						/>

						<TextField
							label="E-mail institucional"
							placeholder="nome@fatec.sp.gov.br"
							{...register("email")}
							error={!!errors.email}
							helperText={errors.email?.message}
							sx={{ margin: "1rem 0" }}
							required
						/>

						<PasswordField
							{...register("password")}
							error={!!errors.password}
							helperText={errors.password?.message}
						/>

						<button
							type="submit"
							disabled={isSubmitting}
							className="bg-red-fatec text-white p-2 rounded my-2 uppercase"
						>
							Cadastrar
						</button>
					</form>

					<p>
						Já possui uma conta? <a href="/login">Entre</a>
					</p>
				</div>
			</div>

			<Snackbar
				open={openSnackbar}
				autoHideDuration={6000} 
				onClose={handleCloseSnackbar}
				anchorOrigin={{ vertical: "top", horizontal: "center" }}
			>
				<Alert
					onClose={handleCloseSnackbar}
					severity={snackbarSeverity}
					sx={{ width: "100%" }}
				>
					{snackbarMessage}
				</Alert>
			</Snackbar>
		</>
	);
}
