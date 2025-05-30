import express from "express";
import "./shared/utils/yupTranslation.utils";
import cors from "cors";
import bodyParser from "body-parser";
import cookieParser from "cookie-parser";
import dotenv from 'dotenv';
dotenv.config();
import { routes } from "./routes/index";

const server = express();

server.use(
	cors({
		origin: ["http://localhost:5173", "http://localhost", "http://front", "http://front:5173"],
		credentials: true,
		methods: "GET,POST,PUT,DELETE",
	})
);

server.use(bodyParser.json());
server.use(express.urlencoded({ extended: true }));
server.use(cookieParser());

server.use(routes);

export { server };
