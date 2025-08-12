import { useAuth } from "@/hooks/useAuth";
import { JSX } from "react";
import { Navigate, useLocation } from "react-router-dom";

export const PrivateRouteLayout = ({ children }: { children: JSX.Element }) => {
	const { user, loading } = useAuth();
	const location = useLocation();

	if (loading) {
		return <div>Carregando...</div>;
	}

	if (!user) {
		return <Navigate to="/login" state={{ from: location }} replace />;
	}

	if (location.pathname === "/") {
		let redirectPath = "/dashboard";

		switch (user.role) {
			case "COORDENADOR":
				redirectPath = "/dashboard-coordenador";
				break;
			case "ADMIN":
				redirectPath = "/dashboard-admin";
				break;
			case "DIRETOR":
				redirectPath = "/dashboard-diretor";
				break;
			case "PROFESSOR":
			default:
				redirectPath = "/dashboard";
				break;
		}

		return <Navigate to={redirectPath} replace />;
	}

	return children;
};
