import React, { useState } from "react";
import { TextField } from "@mui/material";
import { Sidebar } from "@components/Sidebar";
import { Header } from "@components/Header";
import { MobileHeader } from "@components/MobileHeader";
import Drawer from "@mui/material/Drawer";
import * as yup from "yup";

type ErrorFields = {
  email: string;
  subject: string;
  description: string;
};

export default function SupportError() {
  const [isDrawerOpen, setDrawerOpen] = useState(false);
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

  const toggleDrawer = (open: boolean) => () => {
    setDrawerOpen(open);
  };

  const handleChange = (field: string, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    setErrors((prev) => ({ ...prev, [field]: "" }));
  };

  // Schema Yup
  const schema = yup.object().shape({
    email: yup
      .string()
      .email("Por favor, insira um email válido.")
      .required("O email é obrigatório."),
    subject: yup.string().required("O assunto é obrigatório."),
    description: yup.string().required("Por favor, descreva o erro."),
  });

  const handleSubmit = async () => {
    try {
      await schema.validate(formData, { abortEarly: false });
      // Se passou na validação:
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
            // Aqui dizemos para TS que error.path é chave de ErrorFields
            newErrors[error.path as keyof ErrorFields] = error.message;
          }
        });
        setErrors(newErrors);
      }
    }
  };

  return (
    <div className="h-screen flex flex-col md:grid md:grid-cols-[20%_80%] md:grid-rows-[auto_1fr] bg-background">
      {/* Sidebar Desktop */}
      <div className="hidden md:block row-span-2">
        <Sidebar />
      </div>

      {/* Mobile Header */}
      <div className="md:hidden">
        <MobileHeader onMenuClick={toggleDrawer(true)} />
      </div>

      {/* Drawer para Mobile Sidebar */}
      <Drawer open={isDrawerOpen} onClose={toggleDrawer(false)}>
        <div className="w-64 h-full bg-gray-fatec">
          <Sidebar />
        </div>
      </Drawer>

      {/* Header Desktop */}
      <div className="hidden md:block col-start-2 row-start-1">
        <Header />
      </div>

      <main className="p-6 overflow-auto w-full h-full justify-center flex flex-col pt-20 md:pt-6">
        <h2 className="subtitle font-semibold mb-2">Relatar Erro</h2>
        <p className="mb-8 text-gray-600">
          Por favor, descreva o problema que você está enfrentando para que
          possamos ajudar.
        </p>
        <div className="bg-white flex flex-col gap-5 p-10 br-2 flex-grow">
          <div className="flex justify-center">
            <h2 className="subtitle font-bold mb-2">Relatar Erro</h2>
          </div>

          <TextField
            label="Seu Email"
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
            label="Descrição do Erro"
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
              <p>Enviar</p>
            </button>
          </div>
        </div>
      </main>
    </div>
  );
}
