import React, { useState, useCallback } from "react";
import { TextField, MenuItem } from "@mui/material";
import { StepTwoProps, FormErrors } from "./types/haeFormTypes";
import { stepTwoSchema } from "@/validation/haeFormSchema";
import CronogramaPicker from "@components/CronogramaPicker";
import { useHaeDateCalculation } from "@/hooks/useHaeDateCalculation";
import { useHaeHoursCalculation } from "@/hooks/useHaeHoursCalculation";
import { format, parseISO } from "date-fns";

const DAYS_OF_WEEK = [
	"Segunda-feira",
	"Terça-feira",
	"Quarta-feira",
	"Quinta-feira",
	"Sexta-feira",
	"Sábado",
	"Domingo",
];

const StepTwo: React.FC<StepTwoProps> = ({
	onNext,
	onBack,
	formData,
	setFormData,
}) => {
	const [errors, setErrors] = useState<FormErrors>({});

	useHaeDateCalculation(formData.cronograma, setFormData);

	useHaeHoursCalculation(formData.timeRange, setFormData, errors, setErrors);

	const handleNextStep = useCallback(async () => {
		try {
			setErrors({});

			await stepTwoSchema.validate(
				{
					dayOfWeek: formData.dayOfWeek,
					timeRange: formData.timeRange,
					cronograma: formData.cronograma,
				},
				{ abortEarly: false }
			);

			onNext();
		} catch (validationErrors: any) {
			const newErrors: FormErrors = {};
			validationErrors.inner.forEach((error: any) => {
				newErrors[error.path] = error.message;
			});
			setErrors(newErrors); // Atualiza os erros locais para exibição
			console.error("Erros de validação no Step Two:", newErrors);
		}
	}, [formData, onNext]);

	/**
	 * Lida com a mudança nas datas selecionadas do CronogramaPicker.
	 * @param selectedDates Array de strings de datas selecionadas.
	 */
	const handleCronogramaChange = useCallback(
		(selectedDates: string[]) => {
			setFormData("cronograma", selectedDates);
			if (errors.cronograma) {
				setErrors((prevErrors) => ({ ...prevErrors, cronograma: undefined }));
			}
		},
		[setFormData, errors.cronograma]
	);

	/**
	 * Função auxiliar para lidar com a mudança de campos de texto/seleção.
	 * Limpa o erro associado ao campo assim que o usuário o modifica.
	 * @param field O nome do campo a ser atualizado.
	 * @param value O novo valor do campo.
	 */
	const handleFieldChange = useCallback(
		<K extends keyof FormErrors>(
			field: keyof typeof formData,
			value: (typeof formData)[typeof field]
		) => {
			setFormData(field, value);
			if (errors[field]) {
				setErrors((prevErrors) => ({ ...prevErrors, [field]: undefined }));
			}
		},
		[setFormData, errors]
	);

	return (
		<div className="flex flex-col gap-10">
			<div>
				<h2 className="font-semibold subtitle">Cronograma Semanal</h2>
				<p className="text-gray-600">
					Defina o dia da semana, o horário e o cronograma.
				</p>
			</div>

			<TextField
				fullWidth
				label="Dia da Semana"
				select
				value={formData.dayOfWeek}
				onChange={(e) => handleFieldChange("dayOfWeek", e.target.value)}
				error={!!errors.dayOfWeek}
				helperText={errors.dayOfWeek}
			>
				{DAYS_OF_WEEK.map((day) => (
					<MenuItem key={day} value={day}>
						{day}
					</MenuItem>
				))}
			</TextField>

			<TextField
				fullWidth
				label="Horário (HH:MM - HH:MM)"
				variant="outlined"
				placeholder="Ex.: 08:00 - 12:00"
				value={formData.timeRange}
				onChange={(e) => handleFieldChange("timeRange", e.target.value)}
				error={!!errors.timeRange}
				helperText={errors.timeRange}
			/>

			<div>
				<label className="text-sm font-medium text-gray-700 block mb-2">
					Cronograma (Selecione as Datas)
				</label>
				<CronogramaPicker
					selectedDates={formData.cronograma}
					onSelect={handleCronogramaChange}
					errorMessage={errors.cronograma}
				/>
			</div>

			{/* Se houver datas selecionadas, exibe o resumo */}
			{formData.cronograma.length > 0 && (
				<div className="mt-4 p-4 border rounded-md bg-gray-50">
					<p className="font-semibold text-gray-800">
						Datas Selecionadas no Cronograma:
					</p>
					<ul className="list-disc list-inside text-gray-700">
						{formData.cronograma.map((dateString) => (
							<li key={dateString}>
								{format(parseISO(dateString), "dd/MM/yyyy")}
							</li>
						))}
					</ul>
					<p className="mt-2 text-gray-800">
						<span className="font-bold">Período da Atividade:</span>{" "}
						{formData.startDate
							? `${format(
									parseISO(formData.startDate),
									"dd/MM/yyyy"
							  )} a ${format(parseISO(formData.endDate), "dd/MM/yyyy")}`
							: "N/A"}
					</p>
					<p className="mt-2 text-gray-800">
						<span className="font-bold">
							Total de Horas Semanais Estimadas:
						</span>{" "}
						{formData.weeklyHours > 0 ? `${formData.weeklyHours} horas` : "N/A"}
					</p>
				</div>
			)}

			<div className="flex justify-between mt-10">
				<button
					type="button"
					onClick={onBack}
					className="btnFatec bg-gray-400 text-black"
				>
					Voltar
				</button>
				<button type="button" onClick={handleNextStep} className="btnFatec">
					<p>CONTINUAR</p>
				</button>
			</div>
		</div>
	);
};

export default StepTwo;
