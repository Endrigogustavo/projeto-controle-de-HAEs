import { Router } from "express";
import * as HaeController from "./hae.controller";
import { validateHAEStatus } from "../../shared/middleware/validateHAEStatus";

export const haeRoutes = Router();

haeRoutes.put(
  "/change-status/:id",
  validateHAEStatus, 
  HaeController.changeHAEStatus    
);

haeRoutes.get("/:id", 
  HaeController.findHaeById
);

haeRoutes.get("/employee/:id", 
  HaeController.findAllHaesByProfessorId
);

haeRoutes.post("/", 
  HaeController.createHae
);

haeRoutes.delete("/:id", 
  HaeController.deleteHae
);
