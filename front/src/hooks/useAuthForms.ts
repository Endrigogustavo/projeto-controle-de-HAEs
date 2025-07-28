import { useSnackbar } from "./useSnackbar";

export interface IAuthService {
	register?(data: {
		name: string;
		email: string;
		password: string;
		course?: string;
	}): Promise<any>;
	login?(data: { email: string; password: string }): Promise<any>;
	verifyCode?(data: { email: string; code: string }): Promise<any>;
	logout?(): Promise<any>;
	checkCookie?(): Promise<any>;
}

export const useAuthForms = (authService: IAuthService) => {
	const { open, message, severity, showSnackbar, hideSnackbar } = useSnackbar();

	const handleRegister = async (data: {
		name: string;
		email: string;
		password: string;
	}) => {
		if (!authService.register) {
			showSnackbar("Funcionalidade de cadastro não disponível.", "error");
			return false;
		}
		try {
			const response = await authService.register(data);

			if (response?.token) {
				localStorage.setItem("token", response.token);
			}
			if (response?.user) {
				localStorage.setItem("user", JSON.stringify(response.user));
			}

			showSnackbar(
				"Cadastro realizado com sucesso! Verifique seu e-mail.",
				"success"
			);
			return true;
		} catch (error) {
			const errorMessage =
				(error as any)?.response?.data?.message ||
				"Erro ao cadastrar. Tente novamente.";
			showSnackbar(errorMessage, "error");
			return false;
		}
	};

	const handleLogin = async (data: { email: string; password: string }) => {
		if (!authService.login) {
			console.error("Auth service does not provide a login function.");
			showSnackbar("Funcionalidade de login não disponível.", "error");
			return false;
		}
		try {
			const response = await authService.login(data);

			if (response?.token) {
				localStorage.setItem("token", response.token);
			}
			if (response?.user) {
				localStorage.setItem("user", JSON.stringify(response.user));
			}

			showSnackbar("Login realizado com sucesso!", "success");
			return true;
		} catch (error) {
			const errorMessage =
				(error as any)?.response?.data?.message ||
				"Credenciais inválidas. Verifique seu e-mail e senha.";
			showSnackbar(errorMessage, "error");
			return false;
		}
	};

	const handleVerifyCode = async (data: { email: string; code: string }) => {
		if (!authService.verifyCode) {
			console.error("Auth service does not provide a verifyCode function.");
			showSnackbar(
				"Funcionalidade de verificação de código não disponível.",
				"error"
			);
			return false;
		}
		try {
			await authService.verifyCode(data);
			showSnackbar(
				"Código verificado com sucesso! Sua conta está ativa.",
				"success"
			);
			return true;
		} catch (error) {
			const errorMessage =
				(error as any)?.response?.data?.message ||
				"Erro ao verificar código. Verifique se o código está correto.";
			showSnackbar(errorMessage, "error");
			return false;
		}
	};

	const handleLogout = async () => {
		if (!authService.logout) {
			showSnackbar("Funcionalidade de logout não disponível.", "error");
			return false;
		}
		try {
			await authService.logout();

			localStorage.removeItem("token");
			localStorage.removeItem("user");

			showSnackbar("Logout realizado com sucesso!", "success");
			return true;
		} catch (error) {
			const errorMessage =
				(error as any)?.response?.data?.message ||
				"Erro ao fazer logout. Tente novamente.";
			showSnackbar(errorMessage, "error");
			return false;
		}
	};

	return {
		openSnackbar: open,
		snackbarMessage: message,
		snackbarSeverity: severity,
		handleCloseSnackbar: hideSnackbar,
		handleRegister,
		handleLogin,
		handleVerifyCode,
		handleLogout,
	};
};
