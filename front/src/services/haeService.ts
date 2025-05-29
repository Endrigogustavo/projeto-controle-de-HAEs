import { HaeDataType } from "@components/types";
import { createHae } from "./hae";
import { IAuthService } from "@/hooks/useAuthForms";

const haeService: IAuthService = {
	createHae: async (data: HaeDataType) => {
		return await createHae(data);
	},
};

export default haeService;
