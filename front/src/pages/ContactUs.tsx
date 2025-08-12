import { useState } from "react";
import { TextField } from "@mui/material";
import * as yup from "yup";
import { ErrorFields, supportSchema } from "@/validation/supportSchema";
import { AppLayout } from "@/layouts";

export const ContactUs = () => {
  const [formData, setFormData] = useState({
    email: "",
    subject: "",
    description: "",
  });
  const [errors, setErrors] = useState({
    email: "",
    subject: "",
    description: "",
  });

  const handleChange = (field: string, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    setErrors((prev) => ({ ...prev, [field]: "" }));
  };

  // Schema Yup

  const handleSubmit = async () => {
    try {
      await supportSchema.validate(formData, { abortEarly: false });

      alert("Erro relatado com sucesso!");
      setFormData({ email: "", subject: "", description: "" });
      setErrors({ email: "", subject: "", description: "" });
    } catch (validationErrors) {
      if (validationErrors instanceof yup.ValidationError) {
        const newErrors: ErrorFields = {
          email: "",
          subject: "",
          description: "",
        };
        validationErrors.inner.forEach((error) => {
          if (error.path) {
            newErrors[error.path as keyof ErrorFields] = error.message;
          }
        });
        setErrors(newErrors);
      }
    }
  };

  return (
    <AppLayout>
      <main className="p-6 overflow-auto w-full h-200 justify-center flex flex-col pt-20 md:pt-6">
        <h2 className="subtitle font-semibold mb-2">Entre em Contato</h2>
        <p className="mb-8 text-gray-600">
          Preencha o formulário abaixo para entrar em contato com nossa equipe
          de suporte. Descreva o problema ou dúvida para que possamos ajudá-lo
          da melhor forma.
        </p>

        <div className="bg-white flex flex-col gap-10 p-10 br-2 flex-grow rounded-xl shadow-md justify-center">
          <div className="flex justify-center">
            <h2 className="subtitle font-bold mb-2">Formulário de Contato</h2>
          </div>

          <TextField
            label="Seu e-mail"
            variant="outlined"
            fullWidth
            value={formData.email}
            onChange={(e) => handleChange("email", e.target.value)}
            error={!!errors.email}
            helperText={errors.email}
            className="mb-6"
          />

          <TextField
            label="Assunto"
            variant="outlined"
            fullWidth
            value={formData.subject}
            onChange={(e) => handleChange("subject", e.target.value)}
            error={!!errors.subject}
            helperText={errors.subject}
            className="mb-6"
          />

          <TextField
            label="Mensagem"
            variant="outlined"
            fullWidth
            multiline
            minRows={4}
            maxRows={8}
            value={formData.description}
            onChange={(e) => handleChange("description", e.target.value)}
            error={!!errors.description}
            helperText={errors.description}
            className="mb-8"
          />

          <div className="flex justify-end">
            <button
              type="button"
              onClick={handleSubmit}
              className="w-full py-2 btnFatec"
            >
              <p>Enviar Mensagem</p>
            </button>
          </div>
        </div>
      </main>
    </AppLayout>
  );
};
