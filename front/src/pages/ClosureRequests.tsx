import { useEffect, useState } from "react";
import { api } from "@/services";
import { useAuth } from "@/hooks/useAuth";
import { AppLayout } from "@/layouts";
import { HaeResponseDTO } from "@/types/hae";
import {
  CircularProgress,
  Typography,
  Paper,
  Link as MuiLink,
} from "@mui/material";

export const ClosureRequests = () => {
  const { user, loading: userLoading } = useAuth();
  const [requests, setRequests] = useState<HaeResponseDTO[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const fetchRequests = async () => {
    if (!user || (user.role !== "COORDENADOR" && user.role !== "DEV")) return;
    setIsLoading(true);
    try {
      const response = await api.get<HaeResponseDTO[]>(
        `/hae/getHaeByStatus/FECHAMENTO_SOLICITADO`
      );

      const filteredRequests = response.data.filter(
        (hae) => hae.course === user.course
      );

      setRequests(filteredRequests);
    } catch (error) {
      console.error("Erro ao buscar solicitações de fechamento:", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (!userLoading) {
      fetchRequests();
    }
  }, [user, userLoading]);

  const handleUpdateStatus = async (
    haeId: string,
    newStatus: "COMPLETO" | "APROVADO"
  ) => {
    if (!user) return;
    try {
      const payload = { newStatus, coordenadorId: user.id };
      await api.put(`/hae/change-status/${haeId}`, payload);
      setRequests((prev) => prev.filter((req) => req.id !== haeId));
    } catch (error) {
      console.error("Erro ao atualizar status:", error);
      alert("Não foi possível processar a solicitação.");
    }
  };

  if (userLoading || isLoading) {
    return (
      <div className="h-screen flex justify-center items-center">
        <CircularProgress size={70} sx={{ color: "#c10007" }} />
      </div>
    );
  }

  return (
    <AppLayout>
      <main className="col-start-2 row-start-2 p-4 md:p-8">
        <Typography variant="h4" className="subtitle font-semibold mb-6">
          Solicitações de Fechamento de HAE
        </Typography>

        {requests.length === 0 ? (
          <Paper className="p-6 text-center text-gray-600">
            <Typography>
              Nenhuma solicitação de fechamento pendente para o seu curso.
            </Typography>
          </Paper>
        ) : (
          <div className="space-y-4">
            {requests.map((hae) => (
              <Paper
                key={hae.id}
                elevation={2}
                className="p-4 flex flex-col md:flex-row justify-between items-start md:items-center gap-4"
              >
                <div className="flex-1 min-w-0">
                  <Typography variant="h6" className="truncate">
                    {hae.projectTitle}
                  </Typography>
                  <Typography variant="body2" color="textSecondary">
                    Professor: {hae.professorName}
                  </Typography>
                  {hae.comprovanteDoc && hae.comprovanteDoc[0] ? (
                    <MuiLink
                      href={hae.comprovanteDoc[0]}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="font-semibold break-all"
                    >
                      Visualizar Comprovante
                    </MuiLink>
                  ) : (
                    <Typography variant="body2" color="error">
                      Sem comprovante
                    </Typography>
                  )}
                </div>
                <div className="flex gap-2 self-end md:self-center flex-shrink-0">
                  <button
                    className="btnFatec bg-gray-600 text-white uppercase hover:bg-gray-800"
                    onClick={() => handleUpdateStatus(hae.id, "APROVADO")}
                  >
                    Rejeitar
                  </button>
                  <button
                    className="btnFatec text-white uppercase bg-red-800 hover:bg-red-900"
                    onClick={() => handleUpdateStatus(hae.id, "COMPLETO")}
                  >
                    Aprovar Fechamento
                  </button>
                </div>
              </Paper>
            ))}
          </div>
        )}
      </main>
    </AppLayout>
  );
};
