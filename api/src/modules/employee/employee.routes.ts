import { Router } from "express";

import * as employeeController from "./employee.controller";
import { isAuth } from "../../shared/middleware";

const employeeRoutes = Router();

employeeRoutes.get(
  "/get-professor/:id",
  isAuth.isAuthenticated,
  employeeController.findById
);

employeeRoutes.delete(
  "/delete-account/:id",
  isAuth.isAuthenticated,
  employeeController.remove
);

employeeRoutes.put(
  "/update-account/:id",
  isAuth.isAuthenticated,
  employeeController.update
);

export { employeeRoutes };
