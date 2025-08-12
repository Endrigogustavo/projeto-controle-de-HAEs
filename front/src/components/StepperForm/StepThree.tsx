import React, { useState } from "react";
import { TextField } from "@mui/material";
import { StepThreeProps, FormErrors } from "./types/haeFormTypes";

const StepThree: React.FC<StepThreeProps> = ({
  onBack,
  formData,
  setFormData,
  onSubmit,
  isEditMode,
}) => {
  const [errors, setErrors] = useState<FormErrors>({});

  const handleSendClick = async () => {
    try {
      setErrors({});
      await onSubmit();
    } catch (err: unknown) {
      console.log(err);
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
          value={formData.observations}
          onChange={(e) => {
            setFormData("observations", e.target.value);
            if (errors.observations) {
              setErrors((prevErrors) => ({
                ...prevErrors,
                observations: undefined,
              }));
            }
          }}
          error={!!errors.observations}
          helperText={errors.observations}
        />
      </div>

      <div className="flex justify-between mt-10">
        <button
          type="button"
          onClick={onBack}
          className="btnFatec bg-red-600 text-white uppercase"
        >
          Voltar
        </button>
        <button
          type="button"
          onClick={handleSendClick}
          className={`btnFatec text-white uppercase ${
            isEditMode ? "bg-blue-600" : "bg-green-600"
          }`}
        >
          {isEditMode ? "Atualizar" : "Enviar"}
        </button>
      </div>
    </div>
  );
};

export default StepThree;
