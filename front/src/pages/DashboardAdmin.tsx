import { useEffect, useState } from "react";
import { api } from "@/services";
import { Hae } from "@/types/hae";
import { CardHaeAdmin } from "@/components/CardHaeAdmin";
import { AppLayout } from "@/layouts";
import { CircularProgress } from "@mui/material";

export const DashboardAdmin = () => {
  const [haes, setHaes] = useState<Hae[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchHaes = async () => {
      try {
        setIsLoading(true);
        const haeResponse = await api.get<Hae[]>("/hae/getAll");
        const sortedHaes = haeResponse.data.sort(
          (a, b) => Number(a.viewed) - Number(b.viewed)
        );
        setHaes(sortedHaes);
      } catch (err: any) {
        console.error("Erro ao buscar HAEs:", err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchHaes();
  }, []);

  const handleToggleViewed = async (haeId: string) => {
    const originalHaes = [...haes];

    const updatedHaes = haes
      .map((h) => (h.id === haeId ? { ...h, viewed: !h.viewed } : h))
      .sort((a, b) => Number(a.viewed) - Number(b.viewed));
    setHaes(updatedHaes);

    try {
      await api.put(`/hae/viewed/toggle/${haeId}`);
    } catch (error) {
      console.error("Falha ao atualizar o status de visualização:", error);
      setHaes(originalHaes);
      alert("Não foi possível atualizar o status da HAE. Tente novamente.");
    }
  };

  if (isLoading) {
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
      <main className="col-start-2 row-start-2 p-4 md:p-6 lg:p-8 overflow-auto pt-20 md:pt-4 h-full">
        <h2 className="subtitle font-semibold">Visão Geral das HAEs (Admin)</h2>
        <p>
          Abaixo estão listadas todas as HAEs submetidas pelos professores. Você
          pode acompanhá-las e realizar aprovações ou revisões conforme
          necessário.
        </p>

        {haes.length === 0 ? (
          <p className="mt-6 text-gray-500">Nenhuma HAE encontrada.</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6 mt-6">
            {haes.map((hae) => (
              <CardHaeAdmin
                key={hae.id}
                id={hae.id}
                titulo={hae.projectTitle}
                curso={hae.course}
                descricao={hae.projectDescription}
                status={hae.status}
                professor={hae.employee.name}
                viewed={hae.viewed}
                onViewedChange={() => handleToggleViewed(hae.id)}
              />
            ))}
          </div>
        )}
      </main>
    </AppLayout>
  );
};
