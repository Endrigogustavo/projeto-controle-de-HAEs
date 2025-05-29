import { getProfessor } from "./employee";
import { IAuthService } from "@/hooks/useAuthForms";

const employeeService: IAuthService = {
	getProfessor: async (data) => {
		return await getProfessor(data);
	},
};

export default employeeService;
