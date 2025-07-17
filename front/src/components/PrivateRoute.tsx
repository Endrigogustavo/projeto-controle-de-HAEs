import { JSX, useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import api from "@services/index";

interface PrivateRouteProps {
	children: JSX.Element;
}

export function PrivateRoute({ children }: PrivateRouteProps) {
	const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
	
	useEffect(() => {
		api
			.get("/employee/get-my-user")
			.then((data) => {
				if(data.status === 401) {
					setIsAuthenticated(false);
				}
				else {
					setIsAuthenticated(true);
				}
			})
			.catch(() => {
				setIsAuthenticated(false);
			});
	}, []);

	if (isAuthenticated === null) {
		return <div>Loading...</div>;
	}

	if (!isAuthenticated) {
		return <Navigate to="/login" replace />;
	}

	return children;
}
