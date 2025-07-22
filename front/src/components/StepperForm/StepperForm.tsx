import React, { useState, useEffect, useCallback } from "react";
import StepOne from "./StepOne";
import StepTwo from "./StepTwo";
import StepThree from "./StepThree";
import { HaeDataType, StepProps, FormErrors } from "./types/haeFormTypes";
import { useLoggedEmployee } from "@/hooks/useLoggedEmployee";
import { haeFormSchema } from "@/validation/haeFormSchema";
import { useSnackbar } from "@/hooks/useSnackbar";

import Snackbar from "@mui/material/Snackbar";
import MuiAlert, { AlertProps } from "@mui/material/Alert";

const Alert = React.forwardRef<HTMLDivElement, AlertProps>(function Alert(
	props,
	ref
) {
	return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

import { createHae } from "@/services/hae";
import { useNavigate } from "react-router-dom";
import { ValidationError } from "yup";

const StepperForm: React.FC = () => {
	const [step, setStep] = useState(1);
	const {
		employee,
		isLoadingEmployee,
		error: employeeError,
	} = useLoggedEmployee();

	const [formData, setFormData] = useState<HaeDataType>({
		employeeId: "",
		course: "",
		projectTitle: "",
		modality: "",
		weeklyHours: 0,
		projectType: "",
		dayOfWeek: "",
		timeRange: "",
		cronograma: [],
		projectDescription: "",
		observations: "",
		startDate: "",
		endDate: "",
		studentRAs: [],
	});

	const [errors, setErrors] = useState<FormErrors>({});

	const {
		open: openSnackbar,
		message: snackbarMessage,
		severity: snackbarSeverity,
		showSnackbar,
		hideSnackbar,
	} = useSnackbar();

	useEffect(() => {
		if (employee && !isLoadingEmployee) {
			setFormData((prevData) => ({
				...prevData,
				employeeId: employee.id || "",
			}));
		}
		if (employeeError) {
			console.error(
				"Erro ao carregar dados do funcionário para o formulário:",
				employeeError.message
			);
			showSnackbar(
				"Não foi possível carregar seus dados de funcionário. Tente recarregar a página.",
				"error"
			);
		}
	}, [employee, isLoadingEmployee, employeeError, showSnackbar]);

	const updateFormData = useCallback(
		<K extends keyof HaeDataType>(field: K, value: HaeDataType[K]) => {
			setFormData((prevData) => ({
				...prevData,
				[field]: value,
			}));

			setErrors((prevErrors) => ({
				...prevErrors,
				[field]: undefined, // limpa o erro do campo modificado
			}));
		},
		[]
	);

	const handleNextStep = useCallback(() => {
		setStep((prev) => prev + 1);
	}, []);

	const handleBackStep = useCallback(() => {
		setStep((prev) => prev - 1);
	}, []);

	const navigate = useNavigate();

	const handleFormSubmit = useCallback(async () => {
		try {
			await haeFormSchema.validate(formData, { abortEarly: false });
			await createHae(formData);

			showSnackbar("Formulário HAE enviado com sucesso!", "success");
			setTimeout(() => {
				navigate("/");
			}, 4000);
		} catch (validationErrors: unknown) {
			if (validationErrors instanceof ValidationError) {
				console.error(
					"Erros de validação final no StepperForm:",
					validationErrors
				);

				if (validationErrors.inner) {
					const formErrors: FormErrors = {};
					validationErrors.inner.forEach((err) => {
						if (err.path) formErrors[err.path] = err.message;
					});
					setErrors(formErrors);
				}

				showSnackbar(
					"Por favor, corrija os erros no formulário antes de enviar.",
					"error"
				);
			} else {
				console.error(
					"Erro inesperado no envio do formulário:",
					validationErrors
				);
			}
		}
	}, [formData, showSnackbar, navigate]);

	const renderCurrentStep = () => {
		const commonStepProps: StepProps = {
			formData: formData,
			setFormData: updateFormData,
			errors: errors,
		};

		switch (step) {
			case 1:
				return <StepOne {...commonStepProps} onNext={handleNextStep} />;
			case 2:
				return (
					<StepTwo
						{...commonStepProps}
						onNext={handleNextStep}
						onBack={handleBackStep}
					/>
				);
			case 3:
				return (
					<StepThree
						{...commonStepProps}
						onBack={handleBackStep}
						onSubmit={handleFormSubmit}
					/>
				);
			default:
				return null;
		}
	};

	if (isLoadingEmployee) {
		return (
			<div className="flex items-center justify-center h-screen">
				<p className="text-xl text-gray-700">
					Carregando dados do funcionário...
				</p>
			</div>
		);
	}

	if (employeeError) {
		return (
			<div className="flex flex-col items-center justify-center h-screen text-red-600">
				<p className="text-xl">
					Ocorreu um erro inesperado ao carregar seus dados.
				</p>
				<p className="text-md">{employeeError.message}</p>
				<p className="text-sm text-gray-500 mt-2">
					Por favor, tente recarregar a página.
				</p>
			</div>
		);
	}

	if (!employee) {
		return (
			<div className="flex flex-col items-center justify-center h-screen text-gray-700">
				<p className="text-xl">
					Você precisa estar logado para acessar este formulário.
				</p>
				<p className="text-md mt-2">Por favor, faça login para continuar.</p>
			</div>
		);
	}

	return (
		<div className="flex items-center justify-center my-6">
			<div className="p-8 rounded-xl w-full shadow-md bg-white">
				<h2 className="text-3xl font-bold text-center mb-6 text-gray-800">
					Criar HAE
				</h2>
				{renderCurrentStep()}

				<Snackbar
					open={openSnackbar}
					autoHideDuration={6000}
					onClose={hideSnackbar}
					anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
				>
					<Alert
						onClose={hideSnackbar}
						severity={snackbarSeverity}
						sx={{ width: "100%" }}
					>
						{snackbarMessage}
					</Alert>
				</Snackbar>
			</div>
		</div>
	);
};

export default StepperForm;
