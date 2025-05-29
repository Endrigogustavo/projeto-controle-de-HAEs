import api from "./index";

export const getProfessor = async (id: string) => {
	try {
		const response = await api.get(`/employee/get-professor/${id}`);
		return response.data;
	} catch (error) {
		console.log("Erro ao pegar professor");
		throw error;
	}
};