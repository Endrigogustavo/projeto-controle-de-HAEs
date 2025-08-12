import { useEffect, useState } from "react";
import api from "@/services";
import { CardHaeCoordenador } from "../components/CardHaeCoordenador";
import { useAuth } from "@/hooks/useAuth";
import { CircularProgress, Box } from "@mui/material";
import { Hae } from "@/types/hae";
import { AppLayout } from "@/layouts";

export const DashboardCoordenador = () => {
  const [haes, setHaes] = useState<Hae[]>([]);
  const [isLoadingHaes, setIsLoadingHaes] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const { user, loading: isLoadingUser } = useAuth();

  useEffect(() => {
    if (user && user.course) {
      const fetchHaesByCourse = async () => {
        setIsLoadingHaes(true);
        setError(null);
        try {
          const courseName = encodeURIComponent(user.course);
          const haeResponse = await api.get<Hae[]>(
            `/hae/getHaesByCourse/${courseName}`
          );

          setHaes(haeResponse.data);
        } catch (err: any) {
          console.error("Erro ao buscar HAEs por curso:", err);
          setError("Não foi possível carregar as HAEs do seu curso.");
        } finally {
          setIsLoadingHaes(false);
        }
      };

      fetchHaesByCourse();
    } else if (!isLoadingUser) {
      setIsLoadingHaes(false);
    }
  }, [user, isLoadingUser]);

  if (isLoadingUser || isLoadingHaes) {
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
      <main className="col-start-2 row-start-2 p-4 overflow-auto bg-background pt-20 md:pt-4">
        <h2 className="subtitle">Visão Geral das HAEs (Coordenador)</h2>
        <p>
          Abaixo estão listadas todas as HAEs submetidas pelos professores do
          seu curso. Você pode acompanhá-las e realizar aprovações ou revisões
          conforme necessário.
        </p>

        {error && <p className="mt-4 text-red-500">{error}</p>}

        {haes.length === 0 ? (
          <p className="mt-4 text-gray-500">
            Nenhuma HAE encontrada para o seu curso.
          </p>
        ) : (
          <div className="grid gap-4 mt-4">
            {haes.map((hae) => (
              <CardHaeCoordenador
                key={hae.id}
                id={hae.id}
                titulo={hae.projectTitle}
                curso={hae.course}
                descricao={hae.projectDescription}
                status={hae.status}
                professor={hae.employee.name}
              />
            ))}
          </div>
        )}
      </main>
    </AppLayout>
  );
};
