import { HaeDataType } from "@components/StepperForm/types/haeFormTypes";
import { createHae, getHaesByProfessorId, updateHae } from "./hae";
import { IHaeService } from "@/hooks/useHaeService";

const haeService: IHaeService = {
	createHae: async (data: HaeDataType) => {
		return await createHae(data);
	},
	getHaesByProfessorId: async (professorId: string) => {
		return await getHaesByProfessorId(professorId);
	},
	updateHae: async (id: string, data: HaeDataType) => {
		return await updateHae(id, data);
	},
};

export default haeService;
