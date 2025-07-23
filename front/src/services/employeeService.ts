import { getMyUser } from "./employee";
interface IAuthService {
	getProfessor: (id: string) => Promise<any>;
}

const employeeService: IAuthService = {
	getProfessor: async () => {
		return await getMyUser();
	},
};

export default employeeService;