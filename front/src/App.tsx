import { BrowserRouter, Routes, Route } from "react-router-dom";
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

function App() {
	return (
		<BrowserRouter>
			<Routes>
				{/* Rotas públicas */}
				<Route path="/login" element={<Login />} />
				<Route path="/register" element={<Register />} />
				<Route path="/activate-account" element={<ActivateAccount />} />
				<Route path="/forgot-password" element={<ForgotPassword />} />
				<Route path="/reset-password" element={<ResetPassword />} />

				{/* Rotas privadas */}
				<Route
					path="/"
					element={
						<PrivateRoute>
							<>
								<div /> {/* Redireciona baseado na role */}
							</>
						</PrivateRoute>
					}
				/>

				{/* Professor */}
				<Route
					path="/dashboard"
					element={
						<PrivateRoute>
							<Dashboard />
						</PrivateRoute>
					}
				/>
				<Route
					path="/requestHae"
					element={
						<PrivateRoute>
							<RequestHae />
						</PrivateRoute>
					}
				/>
				<Route
					path="/myrequests"
					element={
						<PrivateRoute>
							<MyRequests />
						</PrivateRoute>
					}
				/>

				{/* Coordenador */}
				<Route
					path="/dashboard-coordenador"
					element={
						<PrivateRoute>
							<DashboardCoordenador />
						</PrivateRoute>
					}
				/>

				{/* Rota para o Admin */}
				<Route
					path="/dashboard-admin"
					element={
						<PrivateRoute>
							<DashboardAdmin />
						</PrivateRoute>
					}
				/>

				<Route
					path="/gerenciar-usuarios"
					element={
						<PrivateRoute>
							<GerenciarUsuarios />
						</PrivateRoute>
					}
				/>

				{/* Rota para o Diretor */}
				<Route
					path="/dashboard-diretor"
					element={
						<PrivateRoute>
							<DashboardDiretor />
						</PrivateRoute>
					}
				/>
				<Route
					path="/professores"
					element={
						<PrivateRoute>
							<Professores />
						</PrivateRoute>
					}
				/>
				<Route
					path="/coordenadores"
					element={
						<PrivateRoute>
							<Coordenadores />
						</PrivateRoute>
					}
				/>
				<Route
					path="/haes"
					element={
						<PrivateRoute>
							<TodasHaes />
						</PrivateRoute>
					}
				/>
				<Route
					path="/configuracoes"
					element={
						<PrivateRoute>
							<Configuracoes />
						</PrivateRoute>
					}
				/>

				{/* Visualizar HAE (acessível por todos os perfis logados) */}
				<Route
					path="/hae/:id"
					element={
						<PrivateRoute>
							<ViewHae />
						</PrivateRoute>
					}
				/>
			</Routes>
		</BrowserRouter>
	);
}

export default App;
