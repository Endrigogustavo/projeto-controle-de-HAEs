export interface WeeklyScheduleEntry {
	timeRange: string;
}

export type WeeklySchedule = {
	[day: string]: WeeklyScheduleEntry;
};

export interface HaeDataType {
	employeeId: string;
	course: string;
	projectTitle: string;
	modality: string;
	weeklyHours: number;
	projectType: string; 
	dayOfWeek: string[];
	weeklySchedule: WeeklySchedule;
	timeRange: string;
	projectDescription: string;
	observation: string;
	startDate: string;
	endDate: string;
	studentRAs: string[];
}

export interface FormErrors {
	[key: string]: string | { [index: number]: string } | undefined;

	nameEmployee?: string;
	course?: string;
	projectTitle?: string;
	modality?: string;
	weeklyHours?: string;
	projectType?: string;
	dayOfWeek?: string;
	timeRange?: string;
	projectDescription?: string;
	observation?: string;
	startDate?: string;
	endDate?: string;
	employeeId?: string;

	studentRAs?: {
		[index: number]: string;
	};
}

export interface StepProps {
	formData: HaeDataType;

	setFormData: <K extends keyof HaeDataType>(
		field: K,
		value: HaeDataType[K]
	) => void;
	errors: FormErrors;
}

export interface StepOneProps extends StepProps {
	onNext: () => void;
}

export interface StepTwoProps extends StepProps {
	onNext: () => void;
	onBack: () => void;
}

export interface StepThreeProps extends StepProps {
	onBack: () => void;
	onSubmit: () => Promise<void>;
}
