import { Employee } from "@/types/employee";
import api from "./index";

export const getMyUser = async (): Promise<Employee | null> => {
	try {
		const response = await api.get("/employee/get-my-user");
		const { id, name, email, role } = response.data;
		return { id, name, email, role };
	} catch (error) {
		console.error("Erro ao buscar o usuário logado:", error);
		return null;
	}
};
