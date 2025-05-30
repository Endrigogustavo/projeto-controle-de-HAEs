import { server } from "./server";
import dotenv from 'dotenv';
dotenv.config();


server.listen(process.env.PORT, () =>
  console.log(`Server is running! on Port: ${process.env.PORT}!`)
);
