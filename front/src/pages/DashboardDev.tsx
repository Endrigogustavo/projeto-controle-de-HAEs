import { useEffect, useState } from "react";
import { Header } from "@/components/Header";
import { MobileHeader } from "@/components/MobileHeader";
import Drawer from "@mui/material/Drawer";
import { Paper, Typography, Box } from "@mui/material";
import { dashboardService, DashboardStats } from "@/services/dashboardService";
import { Sidebar } from "@/components";

const StatCard = ({
  title,
  value,
  loading,
}: {
  title: string;
  value: number;
  loading: boolean;
}) => (
  <Paper elevation={2} sx={{ p: 3, textAlign: "center" }}>
    <Typography variant="h6" color="text.secondary">
      {title}
    </Typography>
    <Typography variant="h3" fontWeight="bold" color="primary">
      {loading ? "..." : value}
    </Typography>
  </Paper>
);

export const DashboardDev = () => {
  const [isDrawerOpen, setDrawerOpen] = useState(false);
  const [stats, setStats] = useState<DashboardStats>({
    haeCount: 0,
    userCount: 0,
    institutionCount: 0,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const fetchedStats = await dashboardService.getDevDashboardStats();
        setStats(fetchedStats);
      } catch (err) {
        console.error("Erro ao buscar estatísticas:", err);
        setError("Não foi possível carregar os dados do painel.");
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  return (
    <div className="h-screen flex flex-col md:grid md:grid-cols-[20%_80%] md:grid-rows-[auto_1fr]">
      <div className="hidden md:block row-span-2">
        <Sidebar />
      </div>
      <div className="md:hidden">
        <MobileHeader onMenuClick={() => setDrawerOpen(true)} />
      </div>
      <Drawer open={isDrawerOpen} onClose={() => setDrawerOpen(false)}>
        <div className="w-64 h-full bg-gray-fatec">
          <Sidebar />
        </div>
      </Drawer>
      <div className="hidden md:block col-start-2 row-start-1">
        <Header />
      </div>

      <main className="col-start-2 row-start-2 p-4 md:p-8 overflow-auto bg-gray-50 pt-20 md:pt-4">
        <h2 className="subtitle">Painel do Desenvolvedor</h2>
        <p className="text-gray-600 mb-6">
          Visão geral e monitoramento do sistema.
        </p>

        {error && <p className="text-red-500">{error}</p>}

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <StatCard
            title="Total de HAEs"
            value={stats.haeCount}
            loading={loading}
          />
          <StatCard
            title="Total de Usuários"
            value={stats.userCount}
            loading={loading}
          />
          <StatCard
            title="Instituições"
            value={stats.institutionCount}
            loading={loading}
          />
        </div>
      </main>
    </div>
  );
};
