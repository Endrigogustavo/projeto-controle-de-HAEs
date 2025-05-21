import { Router } from "express";
import { swaggerRoutes } from "../config/swagger/swagger.routes";
import { authRoutes } from "../modules/auth/auth.routes";
import { employeeRoutes } from "../modules/employee/employee.routes";

const routes = Router();

routes.use(swaggerRoutes);

routes.use("/auth", authRoutes);

routes.use("/employee", employeeRoutes);

export { routes };
