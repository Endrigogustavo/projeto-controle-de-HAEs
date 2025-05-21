import { Router } from "express";
import swaggerUi from "swagger-ui-express";
import { swaggerSpec } from "../../config/swagger/swagger.config";

const swaggerRoutes = Router();

// Rota padrão para documentação Swagger
swaggerRoutes.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerSpec));

export { swaggerRoutes };
