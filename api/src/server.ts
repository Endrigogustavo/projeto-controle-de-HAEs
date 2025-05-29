import "dotenv/config";
import express from "express";
import "./shared/utils/yupTranslation.utils";
import cors from "cors";
import bodyParser from "body-parser";
import cookieParser from "cookie-parser";

import { routes } from "./routes/index";

const server = express();

server.use(
  cors({
    origin: process.env.ENABLED_CORS?.split(";") || [],
    credentials: true
  })
);

server.use(bodyParser.json());
server.use(express.urlencoded({ extended: true }));
server.use(cookieParser());

server.use(routes);

export { server };
