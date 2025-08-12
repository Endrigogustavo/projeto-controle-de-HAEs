import { BrowserRouter, Routes, Route, Outlet } from "react-router-dom";

import {
  ActivateAccount,
  Configuracoes,
  Coordenadores,
  Dashboard,
  DashboardAdmin,
  DashboardCoordenador,
  DashboardDiretor,
  ForgotPassword,
  GerenciarUsuarios,
  Login,
  MyRequests,
  Professores,
  Register,
  RequestHae,
  ResetPassword,
  ContactUs,
  TodasHaes,
  ViewHae,
} from "./pages";
import { PrivateRoute } from "./components/PrivateRoute";

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
          <Route path="/support" element={<ContactUs />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
