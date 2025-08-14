import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  TextField,
  CircularProgress,
  Box,
  Typography,
} from "@mui/material";
import * as yup from "yup";
import { AppLayout } from "@/layouts";
import { useSnackbar } from "@/hooks/useSnackbar";
import { ToastNotification } from "@/components/ToastNotification";
import { api } from "@/services";

interface InstitutionApiResponse {
  id: string;
  name: string;
  institutionCode: number;
  address: string;
  haeQtd: number;
}

const editSchema = yup.object({
  institutionCode: yup
    .number()
    .typeError("O código deve ser um número")
    .positive("O código deve ser positivo")
    .required("O código é obrigatório"),
  name: yup.string().required("O nome é obrigatório"),
  address: yup.string().required("O endereço é obrigatório"),
  haeQtd: yup
    .number()
    .typeError("O limite deve ser um número")
    .min(0, "O limite não pode ser negativo")
    .required("O limite de HAEs é obrigatório"),
});

type FormData = {
  institutionCode: string;
  name: string;
  address: string;
  haeQtd: string;
};

export const EditInstitution = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { open, message, severity, showSnackbar, hideSnackbar } = useSnackbar();

  const [formData, setFormData] = useState<FormData>({
    institutionCode: "",
    name: "",
    address: "",
    haeQtd: "",
  });
  const [loading, setLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errors, setErrors] = useState<Partial<Record<keyof FormData, string>>>(
    {}
  );

  useEffect(() => {
    const fetchInstitution = async () => {
      if (!id) return;
      try {
        const response = await api.get<InstitutionApiResponse>(
          `/institution/getInstitutionById?institutionId=${id}`
        );

        const data = response.data;

        setFormData({
          institutionCode: String(data.institutionCode),
          name: data.name,
          address: data.address,
          haeQtd: String(data.haeQtd),
        });
      } catch (err) {
        console.error("Erro ao carregar dados da instituição:", err);
        showSnackbar("Erro ao carregar dados da instituição.", "error");
      } finally {
        setLoading(false);
      }
    };
    fetchInstitution();
  }, [id, showSnackbar]);

  const handleChange = (field: keyof FormData, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    if (errors[field]) {
      setErrors((prev) => ({ ...prev, [field]: undefined }));
    }
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    try {
      const payloadToValidate = {
        ...formData,
        institutionCode: parseInt(formData.institutionCode, 10),
        name: formData.name,
        haeQtd: parseInt(formData.haeQtd, 10),
      };

      await editSchema.validate(payloadToValidate, { abortEarly: false });

      setIsSubmitting(true);

      await api.put(`/institution/update/${id}`, payloadToValidate);

      showSnackbar("Instituição atualizada com sucesso!", "success");
      setTimeout(() => navigate("/institutions"), 1500);
    } catch (err: any) {
      if (err instanceof yup.ValidationError) {
        const newErrors: Partial<Record<keyof FormData, string>> = {};
        err.inner.forEach((error) => {
          if (error.path) {
            newErrors[error.path as keyof FormData] = error.message;
          }
        });
        setErrors(newErrors);
        showSnackbar("Por favor, corrija os erros no formulário.", "error");
      } else {
        console.error("Erro ao atualizar instituição:", err);
        showSnackbar(
          err.response?.data?.message || "Erro ao atualizar instituição.",
          "error"
        );
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  if (loading) {
    return (
      <Box
        display="flex"
        alignItems="center"
        justifyContent="center"
        height="100vh"
      >
        <CircularProgress />
      </Box>
    );
  }

  return (
    <AppLayout>
      <main className="p-4 md:p-8 overflow-auto w-full h-full flex flex-col pt-20 md:pt-6 bg-gray-50">
        <h2 className="subtitle font-semibold mb-2">Editar Instituição</h2>
        <p className="mb-6 text-gray-600">
          Altere os dados da instituição abaixo e clique em salvar.
        </p>

        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 max-w-lg">
          <form onSubmit={handleSubmit} className="flex flex-col gap-6">
            <Typography variant="h6" className="font-semibold text-gray-800">
              Dados da Instituição
            </Typography>
            <TextField
              label="Código da Instituição"
              type="number"
              value={formData.institutionCode}
              onChange={(e) => handleChange("institutionCode", e.target.value)}
              error={!!errors.institutionCode}
              helperText={errors.institutionCode}
              fullWidth
              required
            />
            <TextField
              label="Nome da Instituição"
              value={formData.name}
              onChange={(e) => handleChange("name", e.target.value)}
              error={!!errors.name}
              helperText={errors.name}
              fullWidth
              required
            />
            <TextField
              label="Endereço"
              value={formData.address}
              onChange={(e) => handleChange("address", e.target.value)}
              error={!!errors.address}
              helperText={errors.address}
              fullWidth
              required
            />
            <TextField
              label="Limite de HAEs por Semestre"
              type="number"
              value={formData.haeQtd}
              onChange={(e) => handleChange("haeQtd", e.target.value)}
              error={!!errors.haeQtd}
              helperText={errors.haeQtd}
              InputProps={{ inputProps: { min: 0 } }}
              fullWidth
              required
            />
            <Box
              sx={{
                display: "flex",
                justifyContent: "flex-end",
                gap: 2,
                mt: 2,
              }}
            >
              <button
                className="btnFatec bg-gray-600  text-white uppercase hover:bg-gray-900"
                onClick={() => navigate("/institutions")}
              >
                Cancelar
              </button>

              <button
                type="submit"
                className="btnFatec  text-white uppercase hover:bg-red-900"
                disabled={isSubmitting}
              >
                {isSubmitting ? (
                  <CircularProgress size={24} />
                ) : (
                  "Salvar Alterações"
                )}
              </button>
            </Box>
          </form>
        </div>
      </main>
      <ToastNotification
        open={open}
        message={message}
        severity={severity}
        onClose={hideSnackbar}
      />
    </AppLayout>
  );
};
