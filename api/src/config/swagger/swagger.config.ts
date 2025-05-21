import swaggerJSDoc from "swagger-jsdoc";
import path from "path";

const options: swaggerJSDoc.Options = {
  definition: {
    openapi: "3.0.0",
    info: {
      title: "API Controle de HAE's",
      version: "1.0.0",
      description: "API",
    },
  },
  apis: [path.join(__dirname, "swagger.yaml")],
};

export const swaggerSpec = swaggerJSDoc(options);
