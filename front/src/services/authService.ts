import { LoggedUser } from "@/hooks/useAuth";
import { api } from "./axios.config";

export interface IAuthService {
  register(data: {
    name: string;
    email: string;
    password: string;
    course?: string;
  }): Promise<unknown>;
  login(data: { email: string; password: string }): Promise<LoggedUser>;
  verifyCode(token: string): Promise<LoggedUser>;
  logout(): Promise<unknown>;
  checkCookie(email: string): Promise<unknown>;
}

interface RegisterRequest {
  email: string;
  password: string;
  name: string;
}

interface LoginRequest {
  email: string;
  password: string;
}

const register = async (data: RegisterRequest) => {
  try {
    const response = await api.post("/auth/send-email-code", data);
    return response.data;
  } catch (error) {
    console.error("Erro ao cadastrar", error);
    throw error;
  }
};

const verifyEmailCode = async (token: string) => {
  try {
    const response = await api.get(`/auth/verify-email?token=${token}`);
    return response.data;
  } catch (error) {
    console.log("Erro ao verificar token: " + error);
    throw error;
  }
};

const login = async (data: LoginRequest) => {
  try {
    const response = await api.post("/auth/login", data);
    return response.data;
  } catch (error) {
    console.log("Erro ao fazer login." + error);
    throw error;
  }
};

const logout = async () => {
  try {
    await api.post("/auth/logout");
  } catch (error) {
    console.log("Erro ao fazer logout." + error);
    throw error;
  }
};

const checkCookie = async (email: string) => {
  try {
    const response = await api.get(`/employee/get-professor?email=${email}`);
    return response.data.id;
  } catch (error) {
    console.error("Erro ao verificar cookie");
    throw error;
  }
};

export const authService: IAuthService = {
  register: async (data) => {
    return await register(data);
  },
  login: async (data) => {
    return await login(data);
  },
  verifyCode: async (token: string) => {
    return await verifyEmailCode(token);
  },
  logout: async () => {
    return await logout();
  },
  checkCookie: async (emailRequest: string) => {
    return await checkCookie(emailRequest);
  },
};
