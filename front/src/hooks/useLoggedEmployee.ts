import { useEffect, useState } from "react";
import { checkCookie } from "@services/auth";
import { getMyUser } from "@services/employee";
import { Employee } from "@/types/employee";

interface UseLoggedEmployeeReturn {
	employee: Employee | null;
	isLoadingEmployee: boolean;
	error: Error | null;
}

export const useLoggedEmployee = (): UseLoggedEmployeeReturn => {
	const [employee, setEmployee] = useState<Employee | null>(null);
	const [isLoadingEmployee, setIsLoadingEmployee] = useState(true);
	const [error, setError] = useState<Error | null>(null);

	useEffect(() => {
		const fetchEmployeeData = async (emailRequest: string) => {
			try {
				const userId = await checkCookie(emailRequest);

				if (!userId) {
					setEmployee(null);
					return;
				}

				const employeeData = await getMyUser(emailRequest);
				setEmployee(employeeData);
			} catch (err) {
				console.error("Erro ao carregar dados do funcionário logado:", err);
				setError(
					err instanceof Error
						? err
						: new Error("Erro desconhecido ao carregar o funcionário.")
				);
				setEmployee(null);
			} finally {
				setIsLoadingEmployee(false);
			}
		};

		const email = localStorage.getItem("email");
		if (email) {
			fetchEmployeeData(email);
		} else {
			setIsLoadingEmployee(false);
			setEmployee(null);
		}
	}, []);

	return { employee, isLoadingEmployee, error };
};
