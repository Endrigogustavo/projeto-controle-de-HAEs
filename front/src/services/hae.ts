import { HaeDataType } from "@components/StepperForm/types/haeFormTypes";
import api from "./index";

export const createHae = async (data: HaeDataType) => {
	try {
		const result = await api.post("/hae/create", data);
		return result.data;
	} catch (error) {
		console.log("Erro ao cadastrar HAE: " + error);
		throw error;
	}
};

export const getHaesByProfessorId = async (professorId: string) => {
	try {
		const result = await api.get(`hae/getHaesByProfessor/${professorId}`);
		return result.data;
	} catch (error) {
		console.log("Erro recuperar HAE por professor: " + error);
		throw error;
	}
};

export const updateHae = async (id: string, data: HaeDataType) => {
	const response = await api.put(`/hae/update/${id}`, data);
	return response.data;
};
