export interface HaeDataType {
	nameEmployee: string;
	course: string;
	projectTitle: string;
	weeklyHours: number;
	projectType: string;
	dayOfWeek: string;
	timeRange: string;
	cronograma: string[];
	projectDescription: string;
	observations: string;
	startDate: string;
	endDate: string;
	employeeId: string;
}

export interface FormErrors {
	[key: string]: string | undefined;
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
