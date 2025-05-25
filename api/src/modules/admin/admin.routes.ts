import { Router } from "express";

import * as adminController from "./admin.controller";
import { isAuth } from "../../shared/middleware";

const adminRoutes = Router();

adminRoutes.put(
  "/change-role/:id",
    isAuth.isAuthenticated,
    adminController.changeEmployeeRoleToCoordenador
);

export { adminRoutes };