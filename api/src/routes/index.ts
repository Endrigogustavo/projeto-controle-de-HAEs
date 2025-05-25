import { Router } from "express";
import { swaggerRoutes } from "../config/swagger/swagger.routes";
import { authRoutes } from "../modules/auth/auth.routes";
import { employeeRoutes } from "../modules/employee/employee.routes";
import { haeRoutes } from "../modules/hae/hae.routes";
import { adminRoutes } from "../modules/admin/admin.routes";

const routes = Router();

routes.use(swaggerRoutes);

routes.use("/auth", authRoutes);

routes.use("/employee", employeeRoutes);

routes.use("/hae", haeRoutes);

routes.use("/admin", adminRoutes);

export { routes };
