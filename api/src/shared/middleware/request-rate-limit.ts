import rateLimit from "express-rate-limit";
import RedisStore from "rate-limit-redis";
import { createClient } from "redis";

// Cria o cliente Redis local
const redisClient = createClient({
  socket: {
    host: process.env.REDIS_HOST,
    port: Number(process.env.REDIS_PORT) || 6379,
  },
});

redisClient.connect().catch(console.error);

// Middleware: Envio de código (evita spam)
export const sendCodeRateLimit = rateLimit({
  windowMs: 60 * 60 * 1000, // 1 hora
  max: 50,
  standardHeaders: true,
  legacyHeaders: false,
  message: {
    message: "Too many code requests. Try again in 1 hour.",
  },
  store: new RedisStore({
    sendCommand: (...args: string[]) => redisClient.sendCommand(args),
    prefix: "rl:send-code:",
  }),
});

// Middleware: Verificação de código (evita brute force)
export const verifyCodeRateLimit = rateLimit({
  windowMs: 15 * 60 * 1000, // 15 minutos
  max: 110,
  standardHeaders: true,
  legacyHeaders: false,
  message: {
    message: "Too many verification attempts. Try again later.",
  },
  store: new RedisStore({
    sendCommand: (...args: string[]) => redisClient.sendCommand(args),
    prefix: "rl:verify-code:",
  }),
});

// Middleware: Login (evita brute force)
export const loginRateLimit = rateLimit({
  windowMs: 15 * 60 * 1000, // 15 minutos
  max: 11,
  standardHeaders: true,
  legacyHeaders: false,
  message: {
    message: "Too many login attempts. Please try again later.",
  },
  store: new RedisStore({
    sendCommand: (...args: string[]) => redisClient.sendCommand(args),
    prefix: "rl:login:",
  }),
});
