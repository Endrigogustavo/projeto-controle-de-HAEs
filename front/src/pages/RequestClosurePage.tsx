import React, { useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { api } from "@/services";
import { AppLayout } from "@/layouts";
import {
  CircularProgress,
  TextField,
  Typography,
  Alert,
} from "@mui/material";

export const RequestClosurePage = () => {
  const { id } = useParams<{ id: string }>();
  const location = useLocation();
  const navigate = useNavigate();
  const { projectTitle } = location.state as { projectTitle: string };

  const [file, setFile] = useState<File | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      if (event.target.files[0].type !== "application/pdf") {
        setError("Apenas arquivos PDF são permitidos.");
        setFile(null);
        return;
      }
      setFile(event.target.files[0]);
      setError("");
    }
  };

  const handleSubmit = async () => {
    if (!file || !id) {
      setError("Por favor, selecione um arquivo PDF válido.");
      return;
    }

    setIsSubmitting(true);
    setError("");

    const formData = new FormData();
    formData.append("file", file);

    try {
      await api.post(`/api/files/upload?haeId=${id}`, formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      alert("Solicitação de fechamento enviada com sucesso!");
      navigate("/myrequests");
    } catch (err: any) {
      console.error("Erro ao solicitar fechamento:", err);
      const errorMessage =
        err.response?.data?.message ||
        "Falha ao enviar a solicitação. Tente novamente.";
      setError(errorMessage);
    } finally {
      setIsSubmitting(false);
    }
  };

  const baseButtonStyles =
    "px-4 py-2 uppercase font-semibold rounded-md transition-colors duration-300 focus:outline-none focus:ring-2 focus:ring-offset-2";

  const cancelButtonStyles = `${baseButtonStyles} bg-transparent border border-gray-400 text-gray-700 hover:bg-gray-100 focus:ring-gray-500 disabled:opacity-50 disabled:cursor-not-allowed`;

  const submitButtonStyles = `${baseButtonStyles} bg-red-800 text-white hover:bg-red-900 focus:ring-red-700 disabled:bg-gray-400 disabled:cursor-not-allowed`;

  return (
    <AppLayout>
      <main className="col-start-2 row-start-2 p-4 md:p-8">
        <Typography variant="h4" className="subtitle font-semibold mb-2">
          Solicitar Fechamento de HAE
        </Typography>
        <Typography variant="h6" className="text-gray-700 mb-6">
          {projectTitle}
        </Typography>

        <div className="bg-white p-6 rounded-lg shadow-md max-w-lg space-y-4">
          <Typography>
            Para concluir sua HAE, por favor, anexe o documento comprobatório
            final em formato PDF.
          </Typography>

          <TextField
            type="file"
            fullWidth
            onChange={handleFileChange}
            inputProps={{ accept: "application/pdf" }}
            error={!!error}
            helperText={error}
            variant="outlined"
          />

          {error && <Alert severity="error">{error}</Alert>}

          <div className="flex justify-end pt-4 gap-4">
            <button
              type="button"
              onClick={() => navigate(-1)}
              disabled={isSubmitting}
              className={cancelButtonStyles}
            >
              Cancelar
            </button>

            <button
              type="button"
              onClick={handleSubmit}
              disabled={isSubmitting || !file}
              className={submitButtonStyles}
            >
              {isSubmitting ? (
                <div className="flex items-center gap-2">
                  <CircularProgress size={20} color="inherit" />
                  <span>Enviando...</span>
                </div>
              ) : (
                "Enviar Solicitação"
              )}
            </button>
          </div>
        </div>
      </main>
    </AppLayout>
  );
};
