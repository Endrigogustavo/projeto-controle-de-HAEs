import { useEffect, useState } from "react";
import { CardRequestHae } from "@components/CardRequestHae";
import api from "@/services";
import { Employee } from "@/types/employee";
import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Button,
} from "@mui/material";
import { Hae } from "@/types/hae";
import { AppLayout } from "@/layouts";

export const MyRequests = () => {
  const [haes, setHaes] = useState<Hae[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [haeToDelete, setHaeToDelete] = useState<string | null>(null);

  useEffect(() => {
    const fetchHaes = async () => {
      try {
        setLoading(true);
        const email = localStorage.getItem("email");
        const userResponse = await api.get<Employee>(
          `/employee/get-professor?email=${email}`
        );
        const professorId = userResponse.data.id;
        const haeResponse = await api.get<Hae[]>(
          `/hae/getHaesByProfessor/${professorId}`
        );
        const filteredHaes = haeResponse.data.filter(
          (hae) => hae.status === "PENDENTE"
        );
        setHaes(filteredHaes);
      } catch (err: unknown) {
        console.error(err);
        setError("Erro ao carregar as HAEs");
      } finally {
        setLoading(false);
      }
    };
    fetchHaes();
  }, []);

  const openDeleteDialog = (id: string) => {
    setHaeToDelete(id);
    setIsDeleteDialogOpen(true);
  };

  const closeDeleteDialog = () => {
    setHaeToDelete(null);
    setIsDeleteDialogOpen(false);
  };

  const handleDeleteConfirm = async () => {
    if (!haeToDelete) return;

    try {
      await api.delete(`/hae/delete/${haeToDelete}`);
      setHaes((prev) => prev.filter((hae) => hae.id !== haeToDelete));
    } catch (error) {
      console.error("Erro ao deletar HAE:", error);
      alert("Não foi possível deletar a solicitação.");
    } finally {
      closeDeleteDialog();
    }
  };

  return (
    <AppLayout>
      <main className="col-start-2 row-start-2 p-4 overflow-auto bg-background pt-20 md:pt-4">
        <h2 className="subtitle">Minhas Solicitações</h2>
        <p>
          Nesta seção, você pode gerenciar suas solicitações de HAEs com status
          "Pendente". Tenha a opção de editar ou excluir atividades, conforme
          sua necessidade.
        </p>

        {loading && <p>Carregando HAEs...</p>}
        {error && <p className="text-red-500">{error}</p>}
        {!loading && haes.length === 0 && (
          <p className="mt-4">Você não possui solicitações pendentes.</p>
        )}

        {haes.map((hae) => (
          <CardRequestHae
            key={hae.id}
            id={hae.id}
            titulo={hae.projectTitle}
            curso={hae.course}
            descricao={hae.projectDescription}
            onDelete={() => openDeleteDialog(hae.id)}
          />
        ))}
      </main>

      <Dialog
        open={isDeleteDialogOpen}
        onClose={closeDeleteDialog}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">
          {"Confirmar Exclusão"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            Você tem certeza que deseja excluir esta solicitação de HAE? Esta
            ação não pode ser desfeita.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={closeDeleteDialog}>Cancelar</Button>
          <Button onClick={handleDeleteConfirm} color="error" autoFocus>
            Confirmar Exclusão
          </Button>
        </DialogActions>
      </Dialog>
    </AppLayout>
  );
};
