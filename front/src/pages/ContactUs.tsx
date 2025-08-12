import { useState } from "react";
import {
  TextField,
  MenuItem,
  Select,
  InputLabel,
  FormControl,
  FormHelperText,
} from "@mui/material";
import * as yup from "yup";
import { AppLayout } from "@/layouts";

// Schema de validação atualizado com category
const supportSchema = yup.object().shape({
  email: yup.string().email("E-mail inválido").required("E-mail obrigatório"),
  subject: yup.string().required("Assunto obrigatório"),
  description: yup.string().required("Mensagem obrigatória"),
  category: yup.string().required("Categoria obrigatória"),
});

type ErrorFields = {
  email: string;
  subject: string;
  description: string;
  category: string;
};

export const ContactUs = () => {
  const [formData, setFormData] = useState({
    email: "",
    subject: "",
    description: "",
    category: "",
  });
  const [errors, setErrors] = useState<ErrorFields>({
    email: "",
    subject: "",
    description: "",
    category: "",
  });

  const handleChange = (field: string, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    setErrors((prev) => ({ ...prev, [field]: "" }));
  };

  const handleSubmit = async () => {
    try {
      await supportSchema.validate(formData, { abortEarly: false });

      alert("Erro relatado com sucesso!");
      setFormData({ email: "", subject: "", description: "", category: "" });
      setErrors({ email: "", subject: "", description: "", category: "" });
    } catch (validationErrors) {
      if (validationErrors instanceof yup.ValidationError) {
        const newErrors: ErrorFields = {
          email: "",
          subject: "",
          description: "",
          category: "",
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
      <main className="p-6 overflow-auto w-full justify-center flex flex-col pt-20 md:pt-6 ">
        <h2 className="subtitle font-semibold mb-2 ">Entre em Contato</h2>
        <p className="mb-8 text-gray-600">
          Preencha o formulário abaixo para entrar em contato com nossa equipe
          de suporte. Descreva o problema ou dúvida para que possamos ajudá-lo
          da melhor forma.
        </p>

        <div className="bg-white flex flex-col gap-13 p-10 flex-grow rounded-md shadow-lg justify-center">
          <div className="flex justify-center">
            <h2 className="subtitle font-bold">Formulário de Contato</h2>
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

          <FormControl fullWidth error={!!errors.category} className="mb-6">
            <InputLabel id="category-label">Categoria</InputLabel>
            <Select
              labelId="category-label"
              value={formData.category}
              label="Categoria"
              onChange={(e) => handleChange("category", e.target.value)}
            >
              <MenuItem value="">Selecione a categoria</MenuItem>
              <MenuItem value="bug">Bug</MenuItem>
              <MenuItem value="question">Dúvida</MenuItem>
              <MenuItem value="feedback">Feedback</MenuItem>
            </Select>
            <FormHelperText>{errors.category}</FormHelperText>
          </FormControl>

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
            className=""
          />

          <div className="flex justify-end ">
            <button
              type="button"
              onClick={handleSubmit}
              className="w-full py-2 btnFatec uppercase"
            >
              <p>Enviar</p>
            </button>
          </div>
        </div>
      </main>
    </AppLayout>
  );
};
