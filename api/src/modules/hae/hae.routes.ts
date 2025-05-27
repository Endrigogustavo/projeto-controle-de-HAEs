import { Router } from "express";
import * as HaeController from "./hae.controller";
import { validateHAEStatus } from "../../shared/middleware/validateHAEStatus";
import { isAuth } from "../../shared/middleware";

export const haeRoutes = Router();

haeRoutes.put(
  "/change-status/:id",
  validateHAEStatus, 
  isAuth.isAuthenticated,
  HaeController.changeHAEStatus    
);

haeRoutes.get("getHaeById/:id", 
  isAuth.isAuthenticated,
  HaeController.findHaeById
);

haeRoutes.get("/employee/:id",
  isAuth.isAuthenticated, 
  HaeController.findAllHaesByProfessorId
);

haeRoutes.post("/",
  isAuth.isAuthenticated, 
  HaeController.createHae
);

haeRoutes.delete("/delete/:id", 
  isAuth.isAuthenticated,
  HaeController.deleteHae
);

haeRoutes.get("/getAll",
  isAuth.isAuthenticated, 
  HaeController.findAllHaes
);
