import { HaeDataType } from "@components/StepperForm/types/haeFormTypes";
import api from "./index";

export const createHae = async (data: HaeDataType) => {
	try {
		const result = await api.post("/hae", data);
		return result.data;
	} catch (error) {
		console.log("Erro ao cadastrar HAE: " + error);
		throw error;
	}
};
