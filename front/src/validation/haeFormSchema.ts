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
	dayOfWeek: yup
		.array()
		.of(yup.string())
		.min(1, "Selecione pelo menos um dia da semana")
		.required("Dia da semana é obrigatória"),

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
	studentRAs: yup.array().when("projectType", {
		is: (type: string) => type === "TCC" || type === "Estagio",
		then: (schema) =>
			schema
				.of(
					yup
						.string()
						.matches(/^\d{13}$/, "O RA deve conter exatamente 13 números")
						.required("RA é obrigatório")
				)
				.min(1, "Informe pelo menos um RA")
				.required("É necessário informar os RAs"),
		otherwise: (schema) => schema.strip(), // ignora o campo na validação
	}),

	projectDescription: yup
		.string()
		.required("Descrição do projeto é obrigatória"),
	observation: yup.string(),
	startDate: yup // startDate será preenchida automaticamente, apenas garante que é uma string
		.string()
		.required("Data de início (calculada) é obrigatória"),
	endDate: yup // endDate será preenchida automaticamente, apenas garante que é uma string
		.string()
		.required("Data de fim (calculada) é obrigatória"),
});

export const stepOneSchema = yup.object().shape({
	projectTitle: yup.string().required("Título do projeto é obrigatório"),
	projectType: yup.string().required("Tipo de projeto é obrigatório"),
	course: yup.string().required("Curso é obrigatório"),
	projectDescription: yup
		.string()
		.required("Descrição do projeto é obrigatória"),
	modality: yup.string().required("Modalidade é obrigatória"),
	studentRAs: yup.array().when("projectType", {
		is: (type: string) => type === "TCC" || type === "Estagio",
		then: (schema) =>
			schema
				.of(
					yup
						.string()
						.matches(/^\d{13}$/, "O RA deve conter exatamente 13 números")
						.required("RA é obrigatório")
				)
				.min(1, "Informe pelo menos um RA")
				.required("É necessário informar os RAs"),
		otherwise: (schema) => schema.strip(),
	}),
});

export const stepTwoSchema = yup.object().shape({
	weeklySchedule: yup
		.object()
		.test(
			"at-least-one-day",
			"Selecione pelo menos um dia da semana com horário",
			(value) => {
				if (!value || typeof value !== "object") return false;
				const days = Object.keys(value);
				return days.length > 0 && days.every((day) => !!value[day]?.timeRange);
			}
		)
		.test(
			"valid-time-format",
			"Um ou mais horários estão em formato inválido. Use HH:MM - HH:MM",
			(value) => {
				if (!value) return false;
				return Object.values(value).every((entry: any) =>
					timeRangeRegex.test(entry.timeRange)
				);
			}
		)
		.test(
			"start-before-end",
			"A hora de início deve ser anterior à de fim em todos os dias",
			(value) => {
				if (!value) return false;
				return Object.values(value).every((entry: any) => {
					if (!timeRangeRegex.test(entry.timeRange)) return true;
					const [start, end] = entry.timeRange.split(" - ");
					return parseTime(start) < parseTime(end);
				});
			}
		)
		.test(
			"duration-check",
			"A duração das atividades deve ser entre 1 e 8 horas por dia",
			(value) => {
				if (!value) return false;
				return Object.values(value).every((entry: any) => {
					if (!timeRangeRegex.test(entry.timeRange)) return true;
					const [start, end] = entry.timeRange.split(" - ");
					const duration = (parseTime(end) - parseTime(start)) / 60;
					return duration >= 1 && duration <= 8;
				});
			}
		),
});
