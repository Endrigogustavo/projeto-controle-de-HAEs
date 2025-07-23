import React, { useState, useCallback } from "react";
import {
	TextField,
	MenuItem,
	FormControl,
	InputLabel,
	FormHelperText,
	Select,
} from "@mui/material";
import { StepTwoProps, FormErrors } from "./types/haeFormTypes";
import { stepTwoSchema } from "@/validation/haeFormSchema";
import { useHaeHoursCalculation } from "@/hooks/useHaeHoursCalculation";

const DAYS_OF_WEEK = [
	"SegundaFeira",
	"TerçaFeira",
	"QuartaFeira",
	"QuintaFeira",
	"SextaFeira",
	"Sábado",
];

const StepTwo: React.FC<StepTwoProps> = ({
	onNext,
	onBack,
	formData,
	setFormData,
}) => {
	const [errors, setErrors] = useState<FormErrors>({});

	useHaeHoursCalculation(formData.timeRange, setFormData, errors, setErrors);

	const handleNextStep = useCallback(async () => {
		try {
			setErrors({});

			await stepTwoSchema.validate(
				{
					weeklySchedule: formData.weeklySchedule,
				},
				{ abortEarly: false }
			);

			onNext();
		} catch (validationErrors: any) {
			const newErrors: FormErrors = {};

			if (validationErrors.inner) {
				validationErrors.inner.forEach((error: any) => {
					newErrors[error.path] = error.message;
				});
			} else {
				newErrors.weeklySchedule = validationErrors.message;
			}

			setErrors(newErrors);
			console.error("Erros de validação no Step Two:", newErrors);
		}
	}, [formData, onNext]);

	/**
	 * Função auxiliar para lidar com a mudança de campos de texto/seleção.
	 * Limpa o erro associado ao campo assim que o usuário o modifica.
	 * @param field O nome do campo a ser atualizado.
	 * @param value O novo valor do campo.
	 */
	const handleFieldChange = useCallback(
		<K extends keyof typeof formData>(
			field: K,
			value: (typeof formData)[K]
		) => {
			setFormData(field, value);

			if (field === "dayOfWeek" && Array.isArray(value)) {
				// Ao alterar os dias, atualiza weeklySchedule
				const updatedSchedule = { ...formData.weeklySchedule };
				value.forEach((day) => {
					updatedSchedule[day] = {
						timeRange: formData.timeRange || "", // usa o horário atual
					};
				});
				setFormData("weeklySchedule", updatedSchedule);
			}

			if (field === "timeRange") {
				// Ao alterar o horário, atualiza todos os dias selecionados
				const updatedSchedule = { ...formData.weeklySchedule };
				formData.dayOfWeek.forEach((day) => {
					updatedSchedule[day] = {
						timeRange: value as string,
					};
				});
				setFormData("weeklySchedule", updatedSchedule);
			}

			if (errors[field]) {
				setErrors((prevErrors) => ({ ...prevErrors, [field]: undefined }));
			}
		},
		[formData, errors, setFormData]
	);

	return (
		<div className="flex flex-col gap-10">
			<div>
				<h2 className="font-semibold subtitle">Cronograma Semanal</h2>
				<p className="text-gray-600">
					Defina o dia da semana, o horário e o cronograma.
				</p>
			</div>

			<FormControl fullWidth error={!!errors.dayOfWeek}>
				<InputLabel id="dias-da-semana-label">Dias da Semana</InputLabel>
				<Select
					labelId="dias-da-semana-label"
					multiple
					value={formData.dayOfWeek || []}
					onChange={(e) =>
						handleFieldChange("dayOfWeek", e.target.value as string[])
					}
					label="Dias da Semana"
				>
					{DAYS_OF_WEEK.map((day) => (
						<MenuItem key={day} value={day}>
							{day}
						</MenuItem>
					))}
				</Select>
				{!!errors.dayOfWeek && (
					<FormHelperText>{errors.dayOfWeek}</FormHelperText>
				)}
			</FormControl>

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

			<div className="flex gap-4">
				<TextField
					fullWidth
					type="date"
					label="Data de Início"
					variant="outlined"
					InputLabelProps={{ shrink: true }}
					value={formData.startDate || ""}
					onChange={(e) => handleFieldChange("startDate", e.target.value)}
					error={!!errors.startDate}
					helperText={errors.startDate}
				/>

				<TextField
					fullWidth
					type="date"
					label="Data Final"
					variant="outlined"
					InputLabelProps={{ shrink: true }}
					value={formData.endDate || ""}
					onChange={(e) => handleFieldChange("endDate", e.target.value)}
					error={!!errors.endDate}
					helperText={errors.endDate}
				/>
			</div>

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
