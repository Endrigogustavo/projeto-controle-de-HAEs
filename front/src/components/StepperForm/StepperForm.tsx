import React, { useState, useEffect, useCallback } from "react";
import StepOne from "./StepOne";
import StepTwo from "./StepTwo";
import StepThree from "./StepThree";
import { HaeDataType, StepProps, FormErrors } from "./types/haeFormTypes";
import { useAuth } from "@/hooks/useAuth";
import { haeFormSchema } from "@/validation/haeFormSchema";
import { useNavigate, useLocation } from "react-router-dom";
import { useHaeService } from "@/hooks/useHaeService";
import Snackbar from "@mui/material/Snackbar";
import MuiAlert, { AlertProps } from "@mui/material/Alert";
import * as yup from "yup";
import { api, haeService } from "@/services";
import { CircularProgress } from "@mui/material";

const Alert = React.forwardRef<HTMLDivElement, AlertProps>(function Alert(
  props,
  ref
) {
  return <MuiAlert elevation={6} ref={ref} variant="standard" {...props} />;
});

interface HaeApiResponse
  extends Omit<HaeDataType, "employeeId" | "studentRAs" | "institutionId"> {
  employee: {
    id: string;
    institution?: {
      id: string;
    };
  };
  students: string[];
}

const StepperForm: React.FC = () => {
  const [step, setStep] = useState(1);
  const {
    user: employee,
    loading: isLoadingEmployee,
    error: employeeError,
  } = useAuth();

  const location = useLocation();
  const haeIdToEdit = location.state?.haeId;
  const isEditMode = !!haeIdToEdit;
  const [isLoadingHae, setIsLoadingHae] = useState(isEditMode);

  const [formData, setFormData] = useState<HaeDataType>({
    employeeId: "",
    projectTitle: "",
    weeklyHours: 0,
    course: "",
    projectType: "",
    modality: "",
    startDate: "",
    endDate: "",
    observations: "",
    dayOfWeek: [],
    timeRange: "",
    projectDescription: "",
    weeklySchedule: {},
    studentRAs: [],
    institutionId: "",
  });

  const [errors, setErrors] = useState<FormErrors>({});

  const {
    openSnackbar,
    snackbarMessage,
    snackbarSeverity,
    handleCloseSnackbar: hideSnackbar,
    handleCreateHae,
    handleUpdateHae,
  } = useHaeService(haeService);

  useEffect(() => {
    if (isEditMode && haeIdToEdit) {
      const fetchHaeForEdit = async () => {
        setIsLoadingHae(true);
        try {
          const response = await api.get<HaeApiResponse>(
            `/hae/getHaeById/${haeIdToEdit}`
          );
          const haeData = response.data;

          setFormData({
            ...haeData,
            employeeId: haeData.employee.id,
            studentRAs: haeData.students,
            institutionId: haeData.employee.institution?.id || "",
          });
        } catch (error) {
          console.error("Erro ao buscar HAE para edição:", error);
        } finally {
          setIsLoadingHae(false);
        }
      };
      fetchHaeForEdit();
    }
  }, [isEditMode, haeIdToEdit]);

  useEffect(() => {
    if (employee && !isLoadingEmployee && !isEditMode) {
      setFormData((prevData) => ({
        ...prevData,
        employeeId: employee.id || "",
        institutionId: employee?.institution.id || "",
      }));
    }
    if (employeeError) {
      console.error("Erro ao carregar funcionário:", employeeError.message);
    }
  }, [employee, isLoadingEmployee, employeeError, isEditMode]);

  const updateFormData = useCallback(
    <K extends keyof HaeDataType>(field: K, value: HaeDataType[K]) => {
      setFormData((prevData) => ({
        ...prevData,
        [field]: value,
      }));
      if (errors[field]) {
        setErrors((prevErrors) => ({ ...prevErrors, [field]: undefined }));
      }
    },
    [errors]
  );

  const handleNextStep = () => setStep((prev) => prev + 1);
  const handleBackStep = () => setStep((prev) => prev - 1);
  const navigate = useNavigate();

  const handleFormSubmit = useCallback(async () => {
    try {
      setErrors({});
      await haeFormSchema.validate(formData, { abortEarly: false });

      let success = false;
      if (isEditMode) {
        success = await handleUpdateHae(haeIdToEdit, formData);
      } else {
        success = await handleCreateHae(formData);
      }

      if (success) {
        const redirectPath = "/myrequests";
        setTimeout(() => navigate(redirectPath), 2000);
      }
    } catch (error: unknown) {
      if (error instanceof yup.ValidationError) {
        const newErrors: FormErrors = {};
        error.inner.forEach((err) => {
          if (err.path) {
            newErrors[err.path] = err.message;
          }
        });
        setErrors(newErrors);
      } else {
        console.error("Erro inesperado no formulário:", error);
      }
    }
  }, [
    formData,
    handleCreateHae,
    handleUpdateHae,
    navigate,
    isEditMode,
    haeIdToEdit,
  ]);

  const renderCurrentStep = () => {
    const commonStepProps: StepProps = {
      formData,
      setFormData: updateFormData,
      errors,
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
            isEditMode={isEditMode}
          />
        );
      default:
        return null;
    }
  };

  if (isLoadingEmployee || isLoadingHae) {
    return (
      <div className="h-screen flex justify-center items-center">
        <CircularProgress
          size={70}
          sx={{
            "& .MuiCircularProgress-circle": {
              stroke: "#c10007",
            },
          }}
        />
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
      <div className="flex flex-col items-center justify-center h-screen">
        <p className="text-xl">
          Você precisa estar logado para acessar este formulário.
        </p>
        <p className="text-md mt-2">Por favor, faça login para continuar.</p>
      </div>
    );
  }

  return (
    <div className="flex items-center justify-center my-6">
      <div className="p-8 rounded-md w-full shadow-lg bg-white">
        <h2 className="text-3xl font-bold text-center mb-6">
          {isEditMode ? "Editar Solicitação de HAE" : "Criar Nova HAE"}
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
            <p className="text-green-800 font-semibold">{snackbarMessage}</p>
          </Alert>
        </Snackbar>
      </div>
    </div>
  );
};

export default StepperForm;
