import { Router } from "express";
import { changeHAEStatus } from "./hae.controller";
import { validateHAEStatus } from "../../shared/middleware/validateHAEStatus";

const haeRoutes = Router();

haeRoutes.put(
  "/change-status/:id",
  validateHAEStatus, 
  changeHAEStatus    
);

export { haeRoutes };