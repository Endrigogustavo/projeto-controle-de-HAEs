import React, { useState } from "react";
import { TextField, MenuItem } from "@mui/material";
import { StepOneProps, FormErrors } from "./types/haeFormTypes";
import { stepOneSchema } from "@/validation/haeFormSchema";

const StepOne: React.FC<StepOneProps> = ({ onNext, formData, setFormData }) => {
	const [errors, setErrors] = useState<FormErrors>({});

	const handleNext = async () => {
		try {
			setErrors({});
			await stepOneSchema.validate(
				{
					projectTitle: formData.projectTitle,
					projectType: formData.projectType,
					course: formData.course,
					projectDescription: formData.projectDescription,
				},
				{ abortEarly: false }
			);
			onNext();
		} catch (validationErrors: any) {
			const newErrors: FormErrors = {};
			validationErrors.inner.forEach((error: any) => {
				newErrors[error.path] = error.message;
			});
			setErrors(newErrors);
			console.error("Erros de validação no Step One:", newErrors);
		}
	};

	return (
		<div className="flex flex-col gap-10">
			<div>
				<h2 className="font-semibold subtitle">Definição de Atividade</h2>
				<p className="text-gray-600">
					Forneça os dados essenciais da sua HAE, como título, curso e descrição
					completa.
				</p>
			</div>

			<TextField
				fullWidth
				label="Título do Projeto"
				variant="outlined"
				placeholder="Ex.: Aulas de Legislação para Concurso Público"
				value={formData.projectTitle}
				onChange={(e) => {
					setFormData("projectTitle", e.target.value);
					if (errors.projectTitle) {
						setErrors((prevErrors) => ({
							...prevErrors,
							projectTitle: undefined,
						}));
					}
				}}
				error={!!errors.projectTitle}
				helperText={errors.projectTitle}
			/>

			<TextField
				fullWidth
				label="Tipo de projeto"
				variant="outlined"
				placeholder="Ex.: Estudo e projeto, Administração Acadêmica"
				value={formData.projectType}
				onChange={(e) => {
					setFormData("projectType", e.target.value);
					if (errors.projectType) {
						setErrors((prevErrors) => ({
							...prevErrors,
							projectType: undefined,
						}));
					}
				}}
				error={!!errors.projectType}
				helperText={errors.projectType}
			/>

			<TextField
				fullWidth
				label="Curso"
				select
				value={formData.course}
				onChange={(e) => {
					setFormData("course", e.target.value);
					if (errors.course) {
						setErrors((prevErrors) => ({ ...prevErrors, course: undefined }));
					}
				}}
				error={!!errors.course}
				helperText={errors.course}
			>
				<MenuItem value="Análise e Desenvolvimento de Sistemas">
					Análise e Desenvolvimento de Sistemas
				</MenuItem>
				{/* Adicione outras opções de curso aqui */}
			</TextField>

			<TextField
				fullWidth
				label="Descrição do Projeto"
				multiline
				minRows={3}
				maxRows={10}
				placeholder="Ex.: O projeto consiste em ministrar aulas preparatórias..."
				value={formData.projectDescription}
				onChange={(e) => {
					setFormData("projectDescription", e.target.value);
					if (errors.projectDescription) {
						setErrors((prevErrors) => ({
							...prevErrors,
							projectDescription: undefined,
						}));
					}
				}}
				error={!!errors.projectDescription}
				helperText={errors.projectDescription}
			/>

			<div className="flex justify-end mt-10">
				<button
					type="button"
					onClick={handleNext}
					className="w-full py-2 btnFatec"
				>
					<p>CONTINUAR</p>
				</button>
			</div>
		</div>
	);
};

export default StepOne;
