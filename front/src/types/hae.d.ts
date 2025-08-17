import { EmployeeDetails } from "./employee";

export interface Hae {
	id: string;
	nameEmployee: string;
	course: string;
	projectTitle: string;
	weeklyHours: number;
	projectType: string;
	dayOfWeek: string[];
	timeRange: string;
	projectDescription: string;
	observations: string;
	status: "PENDENTE" | "APROVADO" | "REPROVADO" | "COMPLETO";
	coordenatorId: string;
	coordenatorName?: string;
	startDate: string;
	endDate: string;
	comprovanteDoc: any[];
	modality: "ONLINE" | "HIBRIDO" | "PRESENCIAL";
	employee: EmployeeDetails;
	students: string[];
	weeklySchedule: Record<string, string>;
	createdAt: string;
	updatedAt: string;
	viewed: boolean;
}
