import { useEffect } from "react";
import {
	HaeDataType,
	FormErrors,
} from "@/components/StepperForm/types/haeFormTypes";

// Regex para validar o formato de horário HH:MM - HH:MM
const TIME_RANGE_REGEX =
	/^([0-1]?[0-9]|2[0-3]):[0-5][0-9]\s-\s([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/;

/**
 * Converte uma string de tempo (HH:MM) para minutos totais desde a meia-noite.
 * @param timeStr A string de tempo no formato "HH:MM".
 * @returns O total de minutos.
 */
const parseTime = (timeStr: string): number => {
	const [hours, minutes] = timeStr.split(":").map(Number);
	return hours * 60 + minutes;
};

/**
 * Hook para calcular e atualizar as horas semanais estimadas (weeklyHours)
 * com base na string de faixa de horário (timeRange).
 *
 * @param timeRange String com a faixa de horário (Ex: "08:00 - 12:00").
 * @param setFormData Função para atualizar o formData no componente pai.
 * @param errors Objeto de erros do formulário para limpar erros de timeRange se o formato for corrigido.
 * @param setErrors Função para atualizar o estado de erros no componente pai.
 */
export const useHaeHoursCalculation = (
	timeRange: string,
	setFormData: <K extends keyof HaeDataType>(
		field: K,
		value: HaeDataType[K]
	) => void,
	errors: FormErrors,
	setErrors: React.Dispatch<React.SetStateAction<FormErrors>>
) => {
	useEffect(() => {
		if (timeRange) {
			if (TIME_RANGE_REGEX.test(timeRange)) {
				const [startTimeStr, endTimeStr] = timeRange.split(" - ");
				const startMinutes = parseTime(startTimeStr);
				const endMinutes = parseTime(endTimeStr);

				const durationInMinutes = endMinutes - startMinutes;
				const durationInHours = durationInMinutes / 60;

				setFormData("weeklyHours", durationInHours);
				// Limpa o erro de timeRange se o formato estiver correto agora
				if (errors.timeRange) {
					setErrors((prevErrors) => ({ ...prevErrors, timeRange: undefined }));
				}
			} else {
				setFormData("weeklyHours", 0);
			}
		} else {
			setFormData("weeklyHours", 0);
		}
	}, [timeRange, setFormData, errors.timeRange, setErrors]); // Adicionado setErrors como dependência
};
