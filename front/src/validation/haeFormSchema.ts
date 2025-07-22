// src/validation/haeFormSchema.ts
import * as yup from "yup";

const timeRangeRegex =
	/^([01]\d|2[0-3]):([0-5]\d)\s*-\s*([01]\d|2[0-3]):([0-5]\d)$/;

const parseTime = (timeStr: string) => {
	const [hours, minutes] = timeStr.split(":").map(Number);
	return hours * 60 + minutes;
};

export const haeFormSchema = yup.object().shape({
	employeeId: yup.string().required("O ID do funcionário é obrigatório"),
	course: yup.string().required("Curso é obrigatório"),
	projectTitle: yup.string().required("Título do projeto é obrigatório"),
	weeklyHours: yup
		.number()
		.min(1, "Mínimo de 1 hora semanal") // A validação ainda pode ser útil para garantir que foi calculado
		.required("Horas semanais são obrigatórias"),
	projectType: yup.string().required("Tipo de projeto é obrigatório"),
	dayOfWeek: yup.string().required("Dia da semana é obrigatório"),
	timeRange: yup
		.string()
		.required("Horário é obrigatório")
		.matches(
			timeRangeRegex,
			"Formato de horário inválido. Use HH:MM - HH:MM (ex: 08:00 - 12:00)"
		)
		.test(
			"start-before-end",
			"A hora de início deve ser anterior à hora de fim",
			(value) => {
				if (!value || !timeRangeRegex.test(value)) return true;
				const [startTimeStr, endTimeStr] = value.split(" - ");
				const startMinutes = parseTime(startTimeStr);
				const endMinutes = parseTime(endTimeStr);
				return startMinutes < endMinutes;
			}
		)
		.test(
			"duration-check",
			"A duração da atividade deve ser entre 1 e 8 horas",
			(value) => {
				if (!value || !timeRangeRegex.test(value)) return true;
				const [startTimeStr, endTimeStr] = value.split(" - ");
				const startMinutes = parseTime(startTimeStr);
				const endMinutes = parseTime(endTimeStr);

				const durationInMinutes = endMinutes - startMinutes;
				const durationInHours = durationInMinutes / 60;

				const minDurationHours = 1;
				const maxDurationHours = 8;

				return (
					durationInHours >= minDurationHours &&
					durationInHours <= maxDurationHours
				);
			}
		),
	cronograma: yup
		.array()
		.of(yup.string())
		.min(1, "Selecione pelo menos uma data no cronograma")
		.required("Cronograma é obrigatório"),
	studentRAs: yup
		.array()
		.of(
			yup
				.string()
				.matches(/^\d{13}$/, "O RA deve conter exatamente 13 números")
				.required("RA é obrigatório")
		)
		.min(1, "Informe pelo menos um RA")
		.required("É necessário informar os RAs"),
	projectDescription: yup
		.string()
		.required("Descrição do projeto é obrigatória"),
	observations: yup.string(),
	startDate: yup // startDate será preenchida automaticamente, apenas garante que é uma string
		.string()
		.required("Data de início (calculada) é obrigatória"),
	endDate: yup // endDate será preenchida automaticamente, apenas garante que é uma string
		.string()
		.required("Data de fim (calculada) é obrigatória"),
	employeeId: yup.string().required("ID do funcionário é obrigatório"),
});

export const stepOneSchema = yup.object().shape({
	projectTitle: yup.string().required("Título do projeto é obrigatório"),
	projectType: yup.string().required("Tipo de projeto é obrigatório"),
	course: yup.string().required("Curso é obrigatório"),
	projectDescription: yup
		.string()
		.required("Descrição do projeto é obrigatória"),
	modality: yup.string().required("Modalidade é obrigatória"),
	studentRAs: yup
		.array()
		.of(
			yup
				.string()
				.matches(/^\d{13}$/, "O RA deve conter exatamente 13 números")
				.required("RA é obrigatório")
		)
		.min(1, "Informe pelo menos um RA")
		.required("É necessário informar os RAs"),
});

export const stepTwoSchema = yup.object().shape({
	dayOfWeek: yup.string().required("Dia da semana é obrigatório"),
	timeRange: yup
		.string()
		.required("Horário é obrigatório")
		.matches(
			timeRangeRegex,
			"Formato de horário inválido. Use HH:MM - HH:MM (ex: 08:00 - 12:00)"
		)
		.test(
			"start-before-end",
			"A hora de início deve ser anterior à hora de fim",
			(value) => {
				if (!value || !timeRangeRegex.test(value)) return true;
				const [startTimeStr, endTimeStr] = value.split(" - ");
				const startMinutes = parseTime(startTimeStr);
				const endMinutes = parseTime(endTimeStr);
				return startMinutes < endMinutes;
			}
		)
		.test(
			"duration-check",
			"A duração da atividade deve ser entre 1 e 8 horas",
			(value) => {
				if (!value || !timeRangeRegex.test(value)) return true;
				const [startTimeStr, endTimeStr] = value.split(" - ");
				const startMinutes = parseTime(startTimeStr);
				const endMinutes = parseTime(endTimeStr);

				const durationInMinutes = endMinutes - startMinutes;
				const durationInHours = durationInMinutes / 60;

				const minDurationHours = 1;
				const maxDurationHours = 8;

				return (
					durationInHours >= minDurationHours &&
					durationInHours <= maxDurationHours
				);
			}
		),
	cronograma: yup
		.array()
		.of(yup.string())
		.min(1, "Selecione pelo menos uma data no cronograma")
		.required("Cronograma é obrigatório"),
	// Removendo startDate e endDate de stepTwoSchema,
	// pois eles serão calculados e não inputs diretos aqui.
});
