import { useEffect } from "react";
import { format, parseISO, min, max } from "date-fns";
import { HaeDataType } from "@/components/StepperForm/types/haeFormTypes";

/**
 * Hook para calcular e atualizar as datas de início e fim (startDate, endDate)
 * com base no array de datas do cronograma.
 *
 * @param cronograma Array de strings de datas no formato 'YYYY-MM-DD'.
 * @param setFormData Função para atualizar o formData no componente pai.
 */
export const useHaeDateCalculation = (
	cronograma: string[],
	setFormData: <K extends keyof HaeDataType>(
		field: K,
		value: HaeDataType[K]
	) => void
) => {
	useEffect(() => {
		if (cronograma && cronograma.length > 0) {
			const parsedDates = cronograma.map((dateStr) => parseISO(dateStr));
			const minDate = min(parsedDates);
			const maxDate = max(parsedDates);

			setFormData("startDate", format(minDate, "yyyy-MM-dd"));
			setFormData("endDate", format(maxDate, "yyyy-MM-dd"));
		} else {
			setFormData("startDate", "");
			setFormData("endDate", "");
		}
	}, [cronograma, setFormData]);
};
