import { BrowserRouter, Routes, Route, Outlet } from "react-router-dom";
import Login from "@pages/Login";
import Register from "@pages/Register";
import RequestHae from "@pages/RequestHae";
import Dashboard from "@pages/Dashboard";
import DashboardCoordenador from "@pages/DashboardCoordenador";
import DashboardAdmin from "@pages/DashboardAdmin";
import MyRequests from "@pages/MyRequests";
import ViewHae from "@pages/ViewHae";
import { PrivateRoute } from "@components/PrivateRoute";
import DashboardDiretor from "./pages/DashboardDiretor";
import Professores from "./pages/Professores";
import TodasHaes from "./pages/TodasHaes";
import Coordenadores from "./pages/Coordenadores";
import GerenciarUsuarios from "./pages/GerenciarUsuarios";
import ForgotPassword from "./pages/ForgotPassword";
import ResetPassword from "./pages/ResetPassword";
import ActivateAccount from "./pages/ActivateAccount";
import Configuracoes from "./pages/Configuracoes";
import SupportError from "./pages/SupportError";

function PrivateWrapper() {
  return (
    <PrivateRoute>
      <Outlet />
    </PrivateRoute>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Rotas públicas */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/activate-account" element={<ActivateAccount />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password" element={<ResetPassword />} />

        {/* Rotas privadas agrupadas */}
        <Route element={<PrivateWrapper />}>
          <Route path="/" element={<></>} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/requestHae" element={<RequestHae />} />
          <Route path="/myrequests" element={<MyRequests />} />
          <Route
            path="/dashboard-coordenador"
            element={<DashboardCoordenador />}
          />
          <Route path="/dashboard-admin" element={<DashboardAdmin />} />
          <Route path="/gerenciar-usuarios" element={<GerenciarUsuarios />} />
          <Route path="/dashboard-diretor" element={<DashboardDiretor />} />
          <Route path="/professores" element={<Professores />} />
          <Route path="/coordenadores" element={<Coordenadores />} />
          <Route path="/haes" element={<TodasHaes />} />
          <Route path="/configuracoes" element={<Configuracoes />} />
          <Route path="/hae/:id" element={<ViewHae />} />
          <Route path="/support" element={<SupportError />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
