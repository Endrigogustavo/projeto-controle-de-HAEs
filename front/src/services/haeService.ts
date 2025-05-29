import { HaeDataType } from "@components/StepperForm/types/haeFormTypes";
import { createHae } from "./hae";
import { IAuthService } from "@/hooks/useHaeService";

const haeService: IAuthService = {
	createHae: async (data: HaeDataType) => {
		return await createHae(data);
	},
};

export default haeService;
