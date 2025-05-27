import { Router } from "express";

import * as adminController from "./admin.controller";
import { isAuth } from "../../shared/middleware";

export const adminRoutes = Router();

adminRoutes.put(
  "/change-role/:id",
    isAuth.isAuthenticated,
    adminController.changeEmployeeRoleToCoordenador
);
