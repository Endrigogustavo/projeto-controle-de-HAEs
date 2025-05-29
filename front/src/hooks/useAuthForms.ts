import { useState } from "react";

export interface IAuthService {
	register?(data: {
		name: string;
		email: string;
		password: string;
	}): Promise<any>;
	login?(data: { email: string; password: string }): Promise<any>;
	verifyCode?(data: { email: string; code: string }): Promise<any>;
}

export const useAuthForms = (authService: IAuthService) => {
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
		if (!authService.register) {
			setSnackbarMessage("Funcionalidade de cadastro não disponível.");
			setSnackbarSeverity("error");
			setOpenSnackbar(true);
			return false;
		}
		try {
			await authService.register(data);
			setSnackbarMessage(
				"Cadastro realizado com sucesso! Verifique seu e-mail."
			);
			setSnackbarSeverity("success");
			setOpenSnackbar(true);
			return true;
		} catch (error) {
			setSnackbarMessage("Erro ao cadastrar:");
			setSnackbarSeverity("error");
			setOpenSnackbar(true);
			return false;
		}
	};

	const handleLogin = async (data: { email: string; password: string }) => {
		if (!authService.login) {
			console.error("Auth service does not provide a login function.");
			setSnackbarMessage("Funcionalidade de login não disponível.");
			setSnackbarSeverity("error");
			setOpenSnackbar(true);
			return false;
		}
		try {
			await authService.login(data);
			setSnackbarMessage("Login realizado com sucesso!");
			setSnackbarSeverity("success");
			setOpenSnackbar(true);

			return true;
		} catch (error) {
			setSnackbarMessage("Credenciais inválidas.");
			setSnackbarSeverity("error");
			setOpenSnackbar(true);
			return false;
		}
	};

	const handleVerifyCode = async (data: { email: string; code: string }) => {
		if (!authService.verifyCode) {
			console.error("Auth service does not provide a verifyCode function.");
			setSnackbarMessage(
				"Funcionalidade de verificação de código não disponível."
			);
			setSnackbarSeverity("error");
			setOpenSnackbar(true);
			return false;
		}
		try {
			await authService.verifyCode(data);
			setSnackbarMessage(
				"Código verificado com sucesso! Sua conta está ativa."
			);
			setSnackbarSeverity("success");
			setOpenSnackbar(true);
			return true;
		} catch (error) {
			setSnackbarMessage("Erro ao verificar código");
			setSnackbarSeverity("error");
			setOpenSnackbar(true);
			return false;
		}
	};

	return {
		openSnackbar,
		snackbarMessage,
		snackbarSeverity,
		handleCloseSnackbar,
		handleRegister,
		handleLogin,
		handleVerifyCode,
	};
};
