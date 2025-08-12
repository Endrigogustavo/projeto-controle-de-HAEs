import { useEffect, useState } from "react";
import api from "@/services";
import { Hae } from "@/types/hae";
import { CardHaeCoordenador } from "@/components/CardHaeCoordenador";
import { AppLayout } from "@/layouts";

export const DashboardAdmin = () => {
  const [haes, setHaes] = useState<Hae[]>([]);

  useEffect(() => {
    const fetchHaes = async () => {
      try {
        const haeResponse = await api.get<Hae[]>("/hae/getAll");
        setHaes(haeResponse.data);
      } catch (err: any) {
        console.error("Erro ao buscar HAEs:", err);
      }
    };

    fetchHaes();
  }, []);

  return (
    <AppLayout>
      <main className="col-start-2 row-start-2 p-4 overflow-auto bg-background pt-20 md:pt-4">
        <h2 className="subtitle">Visão Geral das HAEs (Admin)</h2>
        <p>
          Abaixo estão listadas todas as HAEs submetidas pelos professores. Você
          pode acompanhá-las e realizar aprovações ou revisões conforme
          necessário.
        </p>

        {haes.length === 0 ? (
          <p className="mt-4 text-gray-500">Nenhuma HAE encontrada.</p>
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
