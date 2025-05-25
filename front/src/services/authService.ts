import { register, verifyEmailCode, login } from './auth'; 
import { IAuthService } from '../hooks/useAuthForms'; 

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
};

export default authService;