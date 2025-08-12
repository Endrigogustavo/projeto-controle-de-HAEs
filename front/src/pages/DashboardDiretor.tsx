import { useEffect, useState } from "react";
import api from "@/services/axios.config";
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
import { Pie, Bar, Doughnut } from "react-chartjs-2";
import { Hae } from "@/types/hae";
import { AppLayout } from "@/layouts";

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
  const [haes, setHaes] = useState<Hae[]>([]);
  const [loading, setLoading] = useState(true);
  const [haeLimit, setHaeLimit] = useState<number>(0);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [haesResponse, limitResponse] = await Promise.all([
          api.get<Hae[]>("/hae/getAll"),
          api.get<number>("/hae/getAvailableHaesCount"),
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
  }, []);

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
    labels: Object.keys(haesPorCurso),
    datasets: [
      {
        label: "Total de HAEs por Curso",
        data: Object.values(haesPorCurso),
        backgroundColor: "#8b5cf6",
      },
    ],
  };

  return (
    <AppLayout>
      <main className="col-start-2 row-start-2 p-4 md:p-8 overflow-auto bg-gray-50 pt-20 md:pt-4">
        <h2 className="subtitle">Dashboard Geral</h2>
        <p className="text-gray-600 mb-6">
          Análise de todas as Horas de Atividades Específicas do sistema.
        </p>

        {loading ? (
          <p>Carregando dados...</p>
        ) : (
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 lg:col-span-1 relative">
              <h3 className="font-semibold text-lg text-gray-700 mb-4 text-center">
                Uso de HAEs no Semestre Atual
              </h3>
              <Doughnut data={limitChartData} options={limitChartOptions} />
              <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 text-center pointer-events-none">
                <span className="text-4xl font-bold text-gray-800">
                  {haesNoSemestreAtual}
                </span>
                <span className="text-sm text-gray-500 block">
                  de {haeLimit}
                </span>
              </div>
            </div>

            {/* Gráfico de Status */}
            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 lg:col-span-1 flex flex-col items-center">
              <h3 className="font-semibold text-lg text-gray-700 mb-4">
                Distribuição por Status
              </h3>
              <div className="w-full max-w-xs flex-grow flex justify-center items-center">
                <Pie data={statusData} />
              </div>
            </div>

            {/* Gráfico de Cursos */}
            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 lg:col-span-3">
              <h3 className="font-semibold text-lg text-gray-700 mb-4">
                Volume por Curso
              </h3>
              <Bar data={courseData} options={{ responsive: true }} />
            </div>
          </div>
        )}
      </main>
    </AppLayout>
  );
};
