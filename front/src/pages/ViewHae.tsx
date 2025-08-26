import { useEffect, useState, useCallback } from "react";
import { useParams } from "react-router-dom";
import { api } from "@/services";
import {
  Snackbar,
  Alert,
  CircularProgress,
  Divider,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
} from "@mui/material";
import {
  SchoolOutlined,
  Event as EventIcon,
  AccessTime as AccessTimeIcon,
  NotesOutlined,
  InfoOutlined,
  CalendarMonthOutlined,
  CategoryOutlined,
  GroupOutlined,
  VerifiedUserOutlined,
  ArrowBack,
  WidgetsOutlined,
} from "@mui/icons-material";
import { useAuth } from "@/hooks/useAuth";
import { HaeDetailDTO } from "@/types/hae";
import { AppLayout } from "@/layouts";
import { StatusBadge } from "@/components";
import { AxiosError } from "axios";
import { DIMENSAO_LABELS, HAE_TYPE_LABELS, STATUS_OPTIONS } from "@/constants/options";

/**
 * Formata uma string de data (YYYY-MM-DD) para o formato local do usuário.
 */
const formatDate = (dateString: string | null | undefined): string => {
  if (!dateString) return "N/A";
  try {
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return "Data inválida";
    return date.toLocaleDateString(undefined, { timeZone: "UTC" });
  } catch (error) {
    console.error("Erro ao formatar data:", error);
    return dateString;
  }
};

interface DetailItemProps {
  icon: React.ReactNode;
  label: string;
  value: React.ReactNode;
  className?: string;
}

const DetailItem: React.FC<DetailItemProps> = ({
  icon,
  label,
  value,
  className = "",
}) => (
  <div className={`flex flex-col gap-1 ${className}`}>
    <div className="flex items-center gap-2 text-sm text-gray-500">
      {icon}
      <span>{label}</span>
    </div>
    <div className="text-gray-800 text-base">{value}</div>
  </div>
);

export const ViewHae = () => {
  const { id } = useParams<{ id: string }>();
  const { user, loading: isLoadingUser } = useAuth();
  const [hae, setHae] = useState<HaeDetailDTO | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [snackbar, setSnackbar] = useState<{
    open: boolean;
    message: string;
    severity: "success" | "error";
  } | null>(null);

  const fetchHae = useCallback(async () => {
    if (!id) return;
    try {
      const response = await api.get<HaeDetailDTO>(`/hae/getHaeById/${id}`);
      setHae(response.data);
    } catch (err) {
      console.error("Erro ao buscar HAE:", err);
      if (err instanceof AxiosError) {
        const message = err.response?.data?.message || "Falha ao carregar os dados da HAE.";
        setSnackbar({ open: true, message, severity: "error" });
      } else {
        setSnackbar({ open: true, message: "Falha ao carregar os dados da HAE.", severity: "error" });
      }
    }
  }, [id]);

  useEffect(() => {
    fetchHae();
  }, [fetchHae]);

  const handleStatusChange = async (newStatus: HaeDetailDTO["status"]) => {
    if (!user?.id) {
      setSnackbar({
        open: true,
        message: "Não foi possível verificar sua identidade.",
        severity: "error",
      });
      return;
    }
    setIsSubmitting(true);
    try {
      const payload = { newStatus, coordenadorId: user.id };
      await api.put(`/hae/change-status/${id}`, payload);
      setSnackbar({
        open: true,
        message: `Status da HAE atualizado para ${newStatus} com sucesso!`,
        severity: "success",
      });
      fetchHae();
    } catch (err) {
      let errorMessage = "Ocorreu um erro na operação.";
      if (err instanceof AxiosError) {
        errorMessage = err.response?.data?.message || "Ocorreu um erro na operação.";
      }
      setSnackbar({ open: true, message: errorMessage, severity: "error" });
    } finally {
      setIsSubmitting(false);
    }
  };

  const isCoordinator = user?.role === "COORDENADOR" || user?.role === "DEV";

  if (isLoadingUser || !hae) {
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

  return (
    <AppLayout>
      <main className="col-start-2 row-start-2 p-4 md:p-8 overflow-auto bg-gray-50 pt-20 md:pt-4 h-full">
        <div>
          <p className="subtitle font-semibold mb-6 ml-1">Informações da HAE</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
          <div className="flex flex-col md:flex-row justify-between md:items-center gap-4 pb-4 border-b border-gray-200">
            <div>
              <h1 className="text-2xl font-bold text-gray-800">
                {hae.projectTitle}
              </h1>
              <p className="text-gray-500">
                Solicitado por: {hae.professorName ?? "N/A"}
              </p>
            </div>
            <StatusBadge status={hae.status} isFullView />
          </div>

          <div className="pt-6 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-x-8 gap-y-6">
            {hae.coordenatorName &&
              hae.coordenatorName !== "Sem coordenador definido" && (
                <DetailItem
                  icon={<VerifiedUserOutlined />}
                  label="Avaliado por"
                  value={hae.coordenatorName}
                />
              )}
            <DetailItem
              icon={<SchoolOutlined />}
              label="Curso"
              value={hae.course}
            />
            <DetailItem
              icon={<CategoryOutlined />}
              label="Tipo de Projeto"
              value={HAE_TYPE_LABELS[hae.projectType] || hae.projectType}
            />
            <DetailItem
              icon={<WidgetsOutlined />}
              label="Dimensão"
              value={DIMENSAO_LABELS[hae.dimensao] || hae.dimensao}
            />
            <DetailItem
              icon={<AccessTimeIcon />}
              label="Horas Semanais"
              value={`${hae.weeklyHours} horas`}
            />
            <DetailItem
              icon={<InfoOutlined />}
              label="Modalidade"
              value={hae.modality}
            />
            <DetailItem
              icon={<EventIcon />}
              label="Data de Início"
              value={formatDate(hae.startDate)}
            />
            <DetailItem
              icon={<EventIcon />}
              label="Data de Término"
              value={formatDate(hae.endDate)}
            />
          </div>

          {hae.weeklySchedule && Object.keys(hae.weeklySchedule).length > 0 && (
            <div className="pt-6">
              <Divider />
              <div className="pt-6">
                <DetailItem
                  icon={<CalendarMonthOutlined />}
                  label="Cronograma Semanal"
                  value={
                    <div className="mt-2 grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-x-8 gap-y-4">
                      {Object.entries(hae.weeklySchedule).map(
                        ([day, timeRange]) => (
                          <div key={day} className="flex flex-col">
                            <span className="font-semibold text-gray-700">
                              {day}
                            </span>
                            <span className="text-gray-600">
                              {typeof timeRange === "string"
                                ? timeRange
                                : "Horário inválido"}
                            </span>
                          </div>
                        )
                      )}
                    </div>
                  }
                />
              </div>
            </div>
          )}

          {hae.students && hae.students.length > 0 && (
            <div className="pt-6">
              <Divider />
              <div className="pt-6">
                <DetailItem
                  icon={<GroupOutlined />}
                  label="Alunos Envolvidos (RAs)"
                  value={
                    <div className="mt-2 flex flex-wrap gap-2">
                      {hae.students.map((ra) => (
                        <span
                          key={ra}
                          className="bg-gray-100 text-gray-800 text-sm font-mono px-3 py-1 rounded-full"
                        >
                          {ra}
                        </span>
                      ))}
                    </div>
                  }
                />
              </div>
            </div>
          )}

          <div className="pt-6 space-y-6">
            <Divider />
            {hae.projectDescription && (
              <DetailItem
                icon={<NotesOutlined />}
                label="Descrição do Projeto"
                value={
                  <p className="whitespace-pre-wrap">
                    {hae.projectDescription}
                  </p>
                }
                className="col-span-full pt-6"
              />
            )}
            {hae.observations && (
              <DetailItem
                icon={<InfoOutlined />}
                label="Observações"
                value={
                  <p className="whitespace-pre-wrap">{hae.observations}</p>
                }
                className="col-span-full"
              />
            )}

            {isCoordinator && (
              <>
                <Divider />
                <div className="bg-white mt-6 flex flex-col md:flex-row md:items-center md:justify-end gap-4 rounded-lg">
                  <p className="font-semibold text-gray-700">
                    Ações do Coordenador:
                  </p>
                  <FormControl size="small" sx={{ minWidth: 200 }}>
                    <InputLabel id="status-select-label">
                      Alterar Status
                    </InputLabel>
                    <Select
                      labelId="status-select-label"
                      label="Alterar Status"
                      value={hae.status}
                      onChange={(e) =>
                        handleStatusChange(
                          e.target.value as HaeDetailDTO["status"]
                        )
                      }
                      disabled={isSubmitting}
                    >
                      {STATUS_OPTIONS.map((option) => (
                        <MenuItem key={option.value} value={option.value}>
                          {option.label}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </div>
              </>
            )}
            <div className="flex justify-end">
              <ArrowBack sx={{ fill: "white", width: 19, mb: 0.3 }} />
              <a
                href="/haes"
                className="ml-1 font-medium btnFatec  text-white uppercase hover:bg-red-900"
              >
                Voltar
              </a>
            </div>
          </div>
        </div>
      </main>

      <Snackbar
        open={snackbar?.open}
        autoHideDuration={6000}
        onClose={() => setSnackbar(null)}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
      >
        <Alert
          onClose={() => setSnackbar(null)}
          severity={snackbar?.severity}
          sx={{ width: "100%" }}
        >
          {snackbar?.message}
        </Alert>
      </Snackbar>
    </AppLayout>
  );
};
