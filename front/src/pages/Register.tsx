import { useForm, SubmitHandler } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { TextField } from "@mui/material";
import { PasswordField } from "@components/PasswordField";
import { ToastNotification } from "@components/ToastNotification";
import { registerSchema } from "@/validation/registerSchema";
import { register as registerApi } from "@/services/auth";
import { useAuthForms } from "@/hooks/useAuthForms";
import { useNavigate } from "react-router-dom";

type FormData = {
	name: string;
	email: string;
	password: string;
	course: string;
};

export default function Register() {
	const {
		openSnackbar,
		snackbarMessage,
		snackbarSeverity,
		handleCloseSnackbar,
		handleRegister,
	} = useAuthForms({ register: registerApi });
	const navigate = useNavigate();

	const {
		register,
		handleSubmit,
		formState: { errors, isSubmitting },
	} = useForm<FormData>({
		resolver: yupResolver(registerSchema),
	});

	const onSubmit: SubmitHandler<FormData> = async (data) => {
		await handleRegister(data);
		setTimeout(() => {
			navigate("/verificationCode", { state: { userEmail: data.email } });
		}, 4000);
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

						<TextField
							label="Curso"
							placeholder="Análise e Desenvolvimento de Sistemas AMS"
							{...register("course")}
							error={!!errors.course}
							helperText={errors.course?.message}
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

			<ToastNotification
				open={openSnackbar}
				message={snackbarMessage}
				severity={snackbarSeverity}
				onClose={handleCloseSnackbar}
			/>
		</>
	);
}
