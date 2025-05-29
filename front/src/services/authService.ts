import { register, verifyEmailCode, login, logout, checkCookie } from "./auth";
import { IAuthService } from "@/hooks/useAuthForms";

const authService: IAuthService = {
	register: async (data) => {
		return await register(data);
	},
	login: async (data) => {
		return await login(data);
	},
	verifyCode: async (data) => {
		return await verifyEmailCode(data);
	},
	logout: async () => {
		return await logout();
	},
	checkCookie: async () => {
		return await checkCookie();
	},
};

export default authService;
