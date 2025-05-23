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