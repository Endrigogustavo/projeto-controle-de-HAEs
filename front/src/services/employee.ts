import { Employee } from "@/types/employee";
import api from "./index";

export const getMyUser = async (emailRequest: string): Promise<Employee | null> => {
	try {
		const response = await api.get(`/employee/get-professor?email=${emailRequest}`);
		const { id, name, email, role } = response.data;
		return { id, name, email, role };
	} catch (error) {
		console.error("Erro ao buscar o usuário logado:", error);
		return null;
	}
};
