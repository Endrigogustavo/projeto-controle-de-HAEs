import { server } from "./server";

server.listen(process.env.PORT, () =>
  console.log(`Server is running! on Port: ${process.env.PORT}!`)
);
