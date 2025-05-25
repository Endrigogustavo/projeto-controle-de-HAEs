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
		console.log(`DATA: ${data}`);
		console.log(`RESPONSE: ${response.data}`);
	} catch (error) {
		console.log("Erro ao verificar codigo. " + error);
		throw error;
	}
};
