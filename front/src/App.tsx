// src/App.tsx
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "@pages/Login";
import Register from "@pages/Register";
import VerificationCode from "@pages/VerificationCode";
import RequestHae from "@pages/RequestHae";
import Dashboard from "@pages/Dashboard";
import MyRequests from "@pages/MyRequests";
import { PrivateRoute } from "@components/PrivateRoute";

function App() {
	return (
		<BrowserRouter>
			<Routes>
				{/* Rotas públicas */}
				<Route path="/login" element={<Login />} />
				<Route path="/register" element={<Register />} />
				<Route path="/verificationCode" element={<VerificationCode />} />
				
				{/* Rotas privadas */}
				<Route
					path="/"
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
			</Routes>
		</BrowserRouter>
	);
}

export default App;
