import { TextField, FormControl } from "@mui/material";
import { useForm, SubmitHandler } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { useLocation, useNavigate } from "react-router-dom"; // Importa useLocation para pegar o state
import { ToastNotification } from "@/components/ToastNotification";
import { verificationCodeSchema } from "@/validation/verificationCodeSchema";
import { verifyEmailCode as verifyCodeApi } from "@/services/auth";
import { useAuthForms } from "@/hooks/useAuthForms";

type VerificationFormData = {
	code: string;
};

interface LocationState {
	userEmail?: string;
}

export default function VerificationCode() {
	const location = useLocation();
	const { userEmail } = (location.state as LocationState) || {};
	const navigate = useNavigate();

	if (!userEmail) {
		navigate("/register");
	}

	const {
		openSnackbar,
		snackbarMessage,
		snackbarSeverity,
		handleCloseSnackbar,
		handleVerifyCode,
	} = useAuthForms({
		register: async () => {},
		verifyCode: verifyCodeApi,
	});

	const {
		register,
		handleSubmit,
		formState: { errors, isSubmitting },
	} = useForm<VerificationFormData>({
		resolver: yupResolver(verificationCodeSchema),
	});

	const onSubmit: SubmitHandler<VerificationFormData> = async (formData) => {
		if (!userEmail) {
			console.error(
				"Não foi possível verificar o código: email do usuário ausente."
			);
			return;
		}
		await handleVerifyCode({ email: userEmail, code: formData.code });
		setTimeout(() => {
			navigate("/");
		}, 4000);
	};

	return (
		<>
			<div className="flex flex-col items-center justify-center h-screen">
				<div className="p-8 xl:w-xl">
					<h1 className="text-xl my-4 font-semibold">
						Confirme seu
						<span className="text-red-fatec"> Cadastro</span>
					</h1>
					<p className="my-4">
						Nós enviamos um código de confirmação para o seu{" "}
						<a href="/#">e-mail institucional.</a>
						{userEmail && (
							<span className="font-bold"> ({userEmail})</span>
						)}{" "}
					</p>

					<form
						onSubmit={handleSubmit(onSubmit)}
						className="flex flex-col"
						noValidate
					>
						<FormControl>
							<TextField
								required
								label="Código"
								placeholder="369777"
								{...register("code")}
								error={!!errors.code}
								helperText={errors.code?.message}
								inputProps={{
									maxLength: 6,
									pattern: "[0-9]*",
								}}
							/>
						</FormControl>

						<button
							type="submit"
							disabled={isSubmitting || !userEmail}
							className="bg-red-fatec text-white p-2 rounded my-2 uppercase"
						>
							Confirmar Cadastro
						</button>
					</form>
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
