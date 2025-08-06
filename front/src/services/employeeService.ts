import { getMyUser } from "./employee";
interface IAuthService {
	getProfessor: (id: string) => Promise<unknown>;
}

const employeeService: IAuthService = {
	getProfessor: async (email: string) => {
		return await getMyUser(email);
	},
};

export default employeeService;
