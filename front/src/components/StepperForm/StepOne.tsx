import React, { useState } from "react";
import { TextField, MenuItem } from "@mui/material";
import { StepOneProps, FormErrors } from "./types/haeFormTypes";
import { stepOneSchema } from "@/validation/haeFormSchema";
import { ValidationError } from "yup";

const StepOne: React.FC<StepOneProps> = ({ onNext, formData, setFormData }) => {
	const [errors, setErrors] = useState<FormErrors>({});
	const [studentRAs, setStudentRAs] = useState<string[]>([""]);

	const handleNext = async () => {
		try {
			setErrors({});

			await stepOneSchema.validate(
				{
					projectTitle: formData.projectTitle,
					projectType: formData.projectType,
					course: formData.course,
					projectDescription: formData.projectDescription,
					studentRAs: studentRAs,
					modality: formData.modality,
				},
				{ abortEarly: false }
			);

			setFormData("studentRAs", studentRAs);
			onNext();
		} catch (validationErrors: unknown) {
			const newErrors: FormErrors = {};

			if (validationErrors instanceof ValidationError) {
				validationErrors.inner.forEach((error: ValidationError) => {
					if (error.path?.startsWith("studentRAs[")) {
						const match = error.path.match(/studentRAs\[(\d+)\]/);
						if (match) {
							const index = Number(match[1]);
							newErrors.studentRAs = {
								...(newErrors.studentRAs || {}),
								[index]: error.message,
							};
						}
					} else {
						if (error.path) {
							newErrors[error.path] = error.message;
						}
					}
				});
			}

			setErrors(newErrors);
			console.error("Erros de validação no Step One:", newErrors);
		}
	};

	const handleRAChange = (index: number, value: string) => {
		const updatedRAs = [...studentRAs];
		updatedRAs[index] = value;
		setStudentRAs(updatedRAs);
		setFormData("studentRAs", updatedRAs);

		if (errors.studentRAs?.[index]) {
			setErrors((prev) => ({
				...prev,
				studentRAs: {
					...prev.studentRAs,
					[index]: "RM inválido",
				},
			}));
		}
	};

	const addRMField = () => {
		setStudentRAs([...studentRAs, ""]);
	};

	const removeRMField = (index: number) => {
		const updatedRMs = studentRAs.filter((_, i) => i !== index);
		setStudentRAs(updatedRMs);

		if (errors.studentRAs) {
			const updatedErrors = { ...errors.studentRAs };
			delete updatedErrors[index];

			// Reorganiza os índices dos erros após remoção
			const reorderedErrors: { [key: number]: string } = {};
			Object.entries(updatedErrors).forEach(([i, msg]) => {
				const newIndex = Number(i) > index ? Number(i) - 1 : Number(i);
				reorderedErrors[newIndex] = msg;
			});

			setErrors((prevErrors) => ({
				...prevErrors,
				studentRAs: reorderedErrors,
			}));
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
				select
				variant="outlined"
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
			>
				<MenuItem value="ApoioDirecao">Apoio a Direção</MenuItem>
				<MenuItem value="Estagio">Estágio</MenuItem>
				<MenuItem value="TCC">Trabalho de Conclusão de Curso</MenuItem>
			</TextField>

			<TextField
				fullWidth
				label="Modalidade"
				select
				variant="outlined"
				value={formData.modality}
				onChange={(e) => {
					setFormData("modality", e.target.value);
					if (errors.modality) {
						setErrors((prevErrors) => ({
							...prevErrors,
							modality: undefined,
						}));
					}
				}}
				error={!!errors.modality}
				helperText={errors.modality}
			>
				<MenuItem value="PRESENCIAL">Presencial</MenuItem>
				<MenuItem value="ONLINE">Online</MenuItem>
			</TextField>

			<TextField
				fullWidth
				label="Curso"
				select
				value={formData.course}
				onChange={(e) => {
					setFormData("course", e.target.value);
					if (errors.course) {
						setErrors((prevErrors) => ({
							...prevErrors,
							course: undefined,
						}));
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

			{(formData.projectType === "TCC" ||
				formData.projectType === "Estagio") && (
				<div className="flex flex-col gap-4">
					<h3 className="font-medium text-gray-700">RMs dos Alunos</h3>
					{studentRAs.map((ra, index) => (
						<div key={index} className="flex items-center gap-2">
							<TextField
								fullWidth
								label={`RM do Aluno ${index + 1}`}
								variant="outlined"
								value={ra}
								onChange={(e) => handleRAChange(index, e.target.value)}
								error={!!errors.studentRAs?.[index]}
								helperText={errors.studentRAs?.[index]}
							/>
							{index > 0 && (
								<button
									type="button"
									onClick={() => removeRMField(index)}
									className="text-red-500 font-bold"
								>
									Remover
								</button>
							)}
						</div>
					))}
					<button
						type="button"
						onClick={addRMField}
						className="text-blue-500 font-semibold text-sm"
					>
						+ Adicionar outro RM
					</button>
				</div>
			)}

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
