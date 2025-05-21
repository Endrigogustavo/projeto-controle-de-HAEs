import { RequestHandler } from "express";
import { StatusCodes } from "http-status-codes";
import { jwtUtils } from "../utils";



declare module "express-serve-static-core" {
  interface Request {
    userId?: string;
  }
}

export const isAuthenticated: RequestHandler = (req, res, next) => {
  const token = req.cookies?.token;

  if (!token) {
    res.status(StatusCodes.UNAUTHORIZED).json({
      errors: { default: "Not authenticated" },
    });
    return;
  }

  const jwtData = jwtUtils.verify(token);

  if (jwtData === "JWT_SECRET_NOT_FOUND") {
    res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
      errors: { default: "Error verifying token" },
    });
    return;
  }

  if (jwtData === "INVALID_TOKEN") {
    res.status(StatusCodes.UNAUTHORIZED).json({
      errors: { default: "Invalid token" },
    });
    return;
  }

  req.userId = jwtData.sub.toString();
  return next();
};
