import { useForm, SubmitHandler } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup"; 
import { TextField } from "@mui/material";
import { PasswordField } from "@components/PasswordField";
import { ToastNotification } from "@components/ToastNotification";
import { loginSchema } from "@/validation/loginSchema";
import { login as loginApi } from "@services/auth"; 
import { useAuthForms } from "@/hooks/useAuthForms"; 
import { useNavigate } from "react-router-dom";

type FormData = {
	email: string;
	password: string;
};

export default function Login() {
	const navigate = useNavigate();

	const {
		openSnackbar,
		snackbarMessage,
		snackbarSeverity,
		handleCloseSnackbar,
		handleLogin, 
	} = useAuthForms({ login: loginApi });

	const {
		register,
		handleSubmit,
		formState: { errors, isSubmitting },
	} = useForm<FormData>({
		resolver: yupResolver(loginSchema), 
	});

	const onSubmit: SubmitHandler<FormData> = async (data) => {
		const success = await handleLogin(data); 
		if (success) {
			setTimeout(() => {
				navigate("/");
			}, 3000);
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
							label="E-mail institucional"
							placeholder="nome@fatec.sp.gov.br"
							{...register("email")}
							error={!!errors.email}
							helperText={errors.email?.message}
							sx={{ margin: "1rem 0" }}
							required
						/>

						<PasswordField
							label="Senha"
							{...register("password")}
							error={!!errors.password}
							helperText={errors.password?.message}
						/>

						<a
							href="/forgot-password"
							className="text-red-fate hover:underline text-sm my-2"
						>
							Esqueceu a senha?
						</a>

						<button
							type="submit"
							disabled={isSubmitting}
							className="bg-red-fatec text-white p-2 rounded mt-6 mb-2 uppercase"
						>
							Entrar
						</button>
					</form>

					<p>
						É seu primeiro acesso?{" "}
						<a href="/register" className="text-red-fate hover:underline">
							Cadastre-se
						</a>
					</p>
				</div>
			</div>

			<ToastNotification
				open={openSnackbar}
				message={snackbarMessage}
				severity={snackbarSeverity}
				onClose={handleCloseSnackbar}
			/>
		</>
	);
}
