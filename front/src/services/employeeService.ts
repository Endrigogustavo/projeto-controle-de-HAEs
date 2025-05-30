import { getProfessor } from "./employee";
interface IAuthService {
	getProfessor: (id: string) => Promise<any>;
}

const employeeService: IAuthService = {
	getProfessor: async (id) => {
		return await getProfessor(id);
	},
};

export default employeeService;