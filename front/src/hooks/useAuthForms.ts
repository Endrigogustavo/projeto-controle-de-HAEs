import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { AxiosError } from "axios";
import { verifyEmailCode } from "@api/auth";

interface IAuthService {
	register(data: {
		name: string;
		email: string;
		password: string;
	}): Promise<any>;
	verifyCode?(data: { email: string; code: string }): Promise<any>;
}

export const useAuthForms = (authService: IAuthService) => {
	const navigate = useNavigate();

	const [openSnackbar, setOpenSnackbar] = useState(false);
	const [snackbarMessage, setSnackbarMessage] = useState("");
	const [snackbarSeverity, setSnackbarSeverity] = useState<
		"error" | "success" | "info" | "warning"
	>("error");

	const handleCloseSnackbar = () => {
		setOpenSnackbar(false);
	};

	const handleRegister = async (data: {
		name: string;
		email: string;
		password: string;
	}) => {
		try {
			await authService.register(data);
			setSnackbarMessage(
				"Cadastro realizado com sucesso! Verifique seu e-mail."
			);
			setSnackbarSeverity("success");
			setOpenSnackbar(true);
		} catch (error) {
			const axiosError = error as AxiosError;
			console.error("Erro ao cadastrar:", error);
			const errorMessage =
				(axiosError.response?.data as { message?: string })?.message ||
				axiosError.message ||
				"Erro desconhecido ao cadastrar.";

			setSnackbarMessage(errorMessage);
			setSnackbarSeverity("error");
			setOpenSnackbar(true);
		}
	};

	const handleVerifyCode = async (data: { email: string; code: string }) => {
		try {
			await verifyEmailCode(data);
			setSnackbarMessage(
				"Código verificado com sucesso! Sua conta está ativa."
			);
			setSnackbarSeverity("success");
			setOpenSnackbar(true);
			navigate("/dashboard"); // Rota posterior ao registro
		} catch (error) {
			const axiosError = error as AxiosError;
			console.error("Erro ao verificar código:", error);
			const errorMessage =
				(axiosError.response?.data as { message?: string })?.message ||
				axiosError.message ||
				"Erro desconhecido ao verificar o código. Por favor, tente novamente.";

			setSnackbarMessage(errorMessage);
			setSnackbarSeverity("error");
			setOpenSnackbar(true);
		}
	};

	return {
		openSnackbar,
		snackbarMessage,
		snackbarSeverity,
		handleCloseSnackbar,
		handleRegister,
		handleVerifyCode,
	};
};
