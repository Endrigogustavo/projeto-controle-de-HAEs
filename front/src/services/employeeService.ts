import { Employee } from "@/types/employee";
import { api } from "./axios.config";

interface IEmployeeService {
  getProfessor: (id: string) => Promise<Employee | null>;
}

const getMyUser = async (emailRequest: string): Promise<Employee | null> => {
  try {
    const response = await api.get(
      `/employee/get-professor?email=${emailRequest}`
    );
    const { id, name, email, role } = response.data;
    return { id, name, email, role };
  } catch (error) {
    console.error("Erro ao buscar o usuário logado:", error);
    return null;
  }
};

export const employeeService: IEmployeeService = {
  getProfessor: async (email: string): Promise<Employee | null> => {
    return await getMyUser(email);
  },
};
