import { useEffect, useState } from "react";
import { api } from "@/services";
import { CardHaeCoordenador } from "@/components/CardHaeCoordenador";
import { Hae } from "@/types/hae";
import { AppLayout } from "@/layouts";
import { CircularProgress } from "@mui/material";

export const TodasHaes = () => {
  const [haes, setHaes] = useState<Hae[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchHaes = async () => {
      try {
        const response = await api.get<Hae[]>("/hae/getAll");
        setHaes(response.data);
      } catch (err) {
        console.error("Erro ao buscar HAEs:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchHaes();
  }, []);

  return (
    <AppLayout>
      <main className="col-start-2 row-start-2 p-4 md:p-8 overflow-auto bg-gray-50 pt-20 md:pt-4 h-screen">
        <h2 className="subtitle font-semibold">Todas as HAEs do Sistema</h2>
        <p className="text-gray-600 mb-6">
          Lista completa de todas as HAEs cadastradas.
        </p>

        {loading ? (
          <div className="h-screen flex justify-center items-center ">
            <CircularProgress
              size={50}
              sx={{
                "& .MuiCircularProgress-circle": {
                  stroke: "#c10007", // Branco
                },
              }}
            />
          </div>
        ) : haes.length === 0 ? (
          <p className="text-center text-gray-500 py-10">
            Nenhuma HAE cadastrada no sistema.
          </p>
        ) : (
          <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
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
