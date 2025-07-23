import React, { useState } from "react";
import { TextField } from "@mui/material";
import { StepThreeProps, FormErrors } from "./types/haeFormTypes";
import { object } from "yup";
import { haeFormSchema } from "@/validation/haeFormSchema";

const StepThree: React.FC<StepThreeProps> = ({
	onBack,
	formData,
	setFormData,
	onSubmit,
}) => {
	const [errors, setErrors] = useState<FormErrors>({});

	const handleSendClick = async () => {
		try {
			setErrors({});
			await object()
				.shape({
					observation: haeFormSchema.fields.observation,
				})
				.validate(
					{ observation: formData.observation },
					{ abortEarly: false }
				);

			console.log(formData);
			onSubmit();
		} catch (validationErrors: any) {
			const newErrors: FormErrors = {};
			validationErrors.inner.forEach((error: any) => {
				newErrors[error.path] = error.message;
			});
			setErrors(newErrors);
		}
	};

	return (
		<div className="space-y-4">
			<h2 className="font-semibold subtitle">Informações Adicionais</h2>
			<p className="text-gray-600">
				Adicione quaisquer observações ou detalhes importantes sobre a sua
				atividade HAE.
			</p>
			<div className="my-2">
				<TextField
					fullWidth
					label="Observações"
					multiline
					minRows={3}
					maxRows={10}
					placeholder="Ex.: Necessidade de acesso a laboratórios específicos..."
					value={formData.observation}
					onChange={(e) => {
						setFormData("observation", e.target.value);
						if (errors.observation) {
							setErrors((prevErrors) => ({
								...prevErrors,
								observation: undefined,
							}));
						}
					}}
					error={!!errors.observation}
					helperText={errors.observation}
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
				<button
					type="button"
					onClick={handleSendClick}
					className="btnFatec bg-green-600 text-white"
				>
					Enviar
				</button>
			</div>
		</div>
	);
};

export default StepThree;
