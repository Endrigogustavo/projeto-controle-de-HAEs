import api from "./index";

interface RegisterData {
	email: string;
	password: string;
	name: string;
}

export const register = async (data: RegisterData) => {
	try {
		const response = await api.post("/auth/send-email-code", data);
		return response.data;
	} catch (error) {
		console.error("Erro ao cadastrar", error);
		throw error;
	}
};

interface VerifyEmailCode {
	email: string;
	code: string;
}

export const verifyEmailCode = async (data: VerifyEmailCode) => {
	try {
		const response = await api.post("/auth/verify-email-code", data);
		return response.data;
	} catch (error) {
		console.log("Erro ao verificar codigo. " + error);
		throw error;
	}
};

interface Login {
	email: string;
	password: string;
}

export const login = async (data: Login) => {
	try {
		const response = await api.post("/auth/login", data);
		return response.data;
	} catch (error) {
		console.log("Erro ao fazer login." + error);
		throw error;
	}
};

export const logout = async () => {
	try {
		await api.post("/auth/logout");
	} catch (error) {
		console.log("Erro ao fazer logout." + error);
		throw error;
	}
};

export const checkCookie = async () => {
	try {
		const response = await api.get("/auth/check-cookie");
		return response.data.userId;
	} catch (error) {
		console.error("Erro ao verificar cookie");
		throw error;
	}
};
