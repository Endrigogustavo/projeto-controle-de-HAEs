import { useEffect, useState } from "react";
import { api } from "@/services";
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
} from "chart.js";
import { Pie, Doughnut } from "react-chartjs-2";
import { Hae } from "@/types/hae";
import { AppLayout } from "@/layouts";
import { CircularProgress } from "@mui/material";
import { useAuth } from "@/hooks/useAuth";

ChartJS.register(
  ArcElement,
  Tooltip,
  Legend,
  CategoryScale,
  LinearScale,
  BarElement,
  Title
);

const getCurrentSemestre = () => {
  const date = new Date();
  const year = date.getFullYear();
  const month = date.getMonth();
  const semestre = month < 6 ? 1 : 2;
  return `${year}/${semestre}`;
};

const getSemestreFromDate = (dateString: string) => {
  const date = new Date(dateString);
  const year = date.getUTCFullYear();
  const month = date.getUTCMonth();
  const semestre = month < 6 ? 1 : 2;
  return `${year}/${semestre}`;
};

export const DashboardDiretor = () => {
  const { user, loading: authLoading } = useAuth();
  const [haes, setHaes] = useState<Hae[]>([]);
  const [loading, setLoading] = useState(true);
  const [haeLimit, setHaeLimit] = useState<number>(0);

  useEffect(() => {
    if (!user) {
      if (!authLoading) {
        setLoading(false);
      }
      return;
    }

    const fetchDashboardData = async () => {
      setLoading(true);
      try {
        const institutionId = user.institution.id;

        const [haesResponse, limitResponse] = await Promise.all([
          api.get<Hae[]>("/institution/getHaesByInstitutionId", {
            params: { institutionId },
          }),

          api.get<number>("/institution/getAvailableHaesCount", {
            params: { institutionId },
          }),
        ]);

        setHaes(haesResponse.data);
        setHaeLimit(limitResponse.data);
      } catch (err) {
        console.error("Erro ao buscar dados do dashboard:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, [user, authLoading]);

  const currentSemestre = getCurrentSemestre();
  const haesNoSemestreAtual = haes.filter(
    (h) => getSemestreFromDate(h.startDate) === currentSemestre
  ).length;
  const haesRestantes = Math.max(0, haeLimit - haesNoSemestreAtual);

  const limitChartData = {
    labels: ["HAEs Criadas no Semestre", "Limite Restante"],
    datasets: [
      {
        data: [haesNoSemestreAtual, haesRestantes],
        backgroundColor: ["#3b82f6", "#e5e7eb"],
        borderColor: ["#ffffff"],
        borderWidth: 2,
      },
    ],
  };

  const limitChartOptions = {
    rotation: -90,
    circumference: 180,
    cutout: "70%",
    responsive: true,
    plugins: {
      legend: { display: false },
      tooltip: { enabled: true },
    },
    backgroundColor: ["#f59e0b", "#10b981", "#ef4444", "#3b82f6"],
  };

  const statusData = {
    labels: ["Pendentes", "Aprovadas", "Reprovadas", "Completas"],
    datasets: [
      {
        label: "# de HAEs",
        data: [
          haes.filter((h) => h.status === "PENDENTE").length,
          haes.filter((h) => h.status === "APROVADO").length,
          haes.filter((h) => h.status === "REPROVADO").length,
          haes.filter((h) => h.status === "COMPLETO").length,
        ],
        backgroundColor: ["#f59e0b", "#10b981", "#ef4444", "#3b82f6"],
      },
    ],
  };

  const haesPorCurso = haes.reduce((acc: Record<string, number>, hae) => {
    acc[hae.course] = (acc[hae.course] || 0) + 1;
    return acc;
  }, {});

  const courseData = {
    labels: [
      "ADS AMS",
      "Análise e Desenvolvimento de Sistemas",
      "Comercio Exterior",
      "Desenvolvimento de Produtos Plásticos",
      "DSM",
      "Gestão de Recursos Humanos",
      "Gestão Empresarial",
      "Logística",
      "Polímeros",
    ],
    datasets: [
      {
        label: "Total de HAEs por Curso",
        data: [
          haesPorCurso["Análise e Desenvolvimento de Sistemas AMS"] || 0,
          haesPorCurso["Análise e Desenvolvimento de Sistemas"] || 0,
          haesPorCurso["Comercio Exterior"] || 0,
          haesPorCurso["Desenvolvimento de Produtos Plásticos"] || 0,
          haesPorCurso["Desenvolvimento de Software Multiplataforma"] || 0,
          haesPorCurso["Gestão de Recursos Humanos"] || 0,
          haesPorCurso["Gestão Empresarial"] || 0,
          haesPorCurso["Logística"] || 0,
          haesPorCurso["Polímeros"] || 0,
        ],
        backgroundColor: [
          "#8b5cf6", // roxo
          "#1366f1", // azul índigo
          "#f97316", // laranja (Comercio Exterior)
          "#f43f5e", // rosa/vermelho
          "#06b6d4", // ciano
          "#10b981", // verde esmeralda
          "#f59e0b", // âmbar
          "#6ea5e9", // azul claro (sky)
          "#af4444", // vermelho escuro
        ],
      },
    ],
  };
  const haesPorSemestre = haes.reduce((acc: Record<string, number>, hae) => {
    const semestre = getSemestreFromDate(hae.startDate);
    acc[semestre] = (acc[semestre] || 0) + 1;
    return acc;
  }, {});

  const semestreData = {
    labels: Object.keys(haesPorSemestre),
    datasets: [
      {
        label: "HAEs por Semestre",
        data: Object.values(haesPorSemestre),
        backgroundColor: [
          "#3b82f6",
          "#10b981",
          "#f59e0b",
          "#ef4444",
          "#8b5cf6",
        ],
      },
    ],
  };

  if (authLoading || loading) {
    return (
      <AppLayout>
        <main className="col-start-2 row-start-2 p-4 md:p-8 overflow-auto bg-gray-50 pt-20 md:pt-4 h-full flex justify-center items-center">
          <CircularProgress
            size={70}
            sx={{
              "& .MuiCircularProgress-circle": {
                stroke: "#c10007",
              },
            }}
          />
        </main>
      </AppLayout>
    );
  }

  return (
    <AppLayout>
      <main className="col-start-2 row-start-2 p-4 md:p-8 overflow-auto bg-gray-50 pt-20 md:pt-4 h-full">
        <h2 className="subtitle font-semibold">
          Dashboard Geral - {user?.institution.name}
        </h2>
        <p className="text-gray-600 mb-6">
          Análise das Horas de Atividades Específicas da sua instituição.
        </p>

        <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-8 mb-7">
          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 flex flex-col">
            <h3 className="font-semibold text-lg text-gray-700 mb-4 text-center">
              Distribuição de HAEs por Status
            </h3>
            <div className="p-5">
              <Doughnut
                data={statusData}
                options={{
                  plugins: {
                    legend: { display: false },
                  },
                }}
              />
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 relative ">
            <h3 className="font-semibold text-lg text-gray-700 text-center">
              Uso de HAEs no Semestre Atual
            </h3>
            <Doughnut
              data={limitChartData}
              options={limitChartOptions}
              className="mb-10"
            />
            <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 text-center pointer-events-none mt-12 md:mt-8">
              <span className="text-3xl font-bold text-gray-800 mt-4">
                {haesNoSemestreAtual}
              </span>
              <span className="text-sm text-gray-500 block">de {haeLimit}</span>
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 flex flex-col">
            <h3 className="font-semibold text-lg text-gray-700 mb-4 text-center">
              HAEs por Semestre - Histórico
            </h3>
            <div className="p-5">
              <Pie
                data={semestreData}
                options={{
                  plugins: {
                    legend: { display: false },
                  },
                }}
              />
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 flex flex-col sm:col-span-1 xl:col-span-3">
            <h3 className="font-semibold text-lg text-gray-700 mb-6 text-center">
              Volume por Curso
            </h3>
            <div className="grid grid-cols-3 gap-2 items-start">
              <div className="flex justify-center items-center col-span-3 p-5 xl:col-span-1  ">
                <Pie
                  data={courseData}
                  options={{
                    plugins: {
                      legend: { display: false },
                    },
                  }}
                />
              </div>

              {/* Legenda */}
              <div className="hidden xl:flex col-span-2 items-center justify-center h-full">
                <ul className="text-sm text-gray-700 grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-3">
                  {courseData.labels.map((label, index) => (
                    <li
                      key={label}
                      className="flex items-center whitespace-nowrap"
                    >
                      <span
                        className="w-3 h-3 rounded-full mr-2"
                        style={{
                          backgroundColor:
                            courseData.datasets[0].backgroundColor[index],
                        }}
                      />
                      {label}
                    </li>
                  ))}
                </ul>
              </div>
            </div>
          </div>
        </div>
      </main>
    </AppLayout>
  );
};
