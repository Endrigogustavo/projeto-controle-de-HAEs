import { useEffect, useState } from "react";
import { checkCookie } from "@services/auth";
import { getProfessor } from "@services/employee";
import { Employee, GetProfessorResponse } from "@/types/employee";

interface UseLoggedEmployeeReturn {
	employee: Employee | null;
	isLoadingEmployee: boolean;
	error: Error | null;
}

export const useLoggedEmployee = (): UseLoggedEmployeeReturn => {
	const [employee, setEmployee] = useState<Employee | null>(null);
	const [isLoadingEmployee, setIsLoadingEmployee] = useState<boolean>(true);
	const [error, setError] = useState<Error | null>(null);

	useEffect(() => {
		const fetchEmployeeData = async () => {
			try {
				const userId = await checkCookie();

				if (!userId) {
					setEmployee(null);
					return;
				}

				const professorResponse: GetProfessorResponse = await getProfessor(
					userId
				);

				setEmployee(professorResponse.record);
			} catch (err) {
				console.error("Erro ao carregar dados do funcionário logado:", err);
				setError(
					err instanceof Error
						? err
						: new Error(
								"Ocorreu um erro desconhecido ao carregar o funcionário."
						  )
				);
				setEmployee(null);
			} finally {
				setIsLoadingEmployee(false);
			}
		};

		fetchEmployeeData();
	}, []);

	return { employee, isLoadingEmployee, error };
};
