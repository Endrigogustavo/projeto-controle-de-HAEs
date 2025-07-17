import PersonIcon from "@mui/icons-material/Person";
import { Employee } from "@/types/employee";

interface EmployeeInfoDisplayProps {
	employee: Employee | null; 
	isLoading: boolean;
}

export const EmployeeInfoDisplay = ({
	employee,
	isLoading,
}: EmployeeInfoDisplayProps) => {
	if (isLoading) {
		return (
			<div className="flex items-center text-sm text-gray-700 px-2">
				<p>Carregando...</p>
				<div className="row-span-2 col-start-2 flex justify-center items-center">
					<PersonIcon className="text-gray-500" />
				</div>
				<p>...</p>
			</div>
		);
	}

	if (!employee) {
		return (
			<div className="flex items-center text-sm text-gray-700 px-2">
				<p>Visitante</p>
				<div className="row-span-2 col-start-2 flex justify-center items-center p-4">
					<PersonIcon className="text-gray-500" />
				</div>
				<p>Não logado</p>
			</div>
		);
	}

	return (
		<div className="flex items-center text-sm text-gray-700 px-2">
			<div className="px-2">
				<p className="font-semibold">{employee.name}</p>
				<p className="text-gray-500">{employee.email}</p>
			</div>
			<div className="row-span-2 col-start-2 flex justify-center items-center p-4">
				<PersonIcon className="text-blue-600 text-3xl" />
			</div>
		</div>
	);
};
