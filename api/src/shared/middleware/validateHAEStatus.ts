import { Request, Response, NextFunction } from "express";
import { StatusCodes } from "http-status-codes";
import { Status } from "@prisma/client";

export const validateHAEStatus = (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  const { haeStatus } = req.body;

  const validStatuses = Object.values(Status);
  if (!validStatuses.includes(haeStatus)) {
    return res.status(StatusCodes.BAD_REQUEST).json({
      error: `Invalid HAE status. Must be one of: ${validStatuses.join(", ")}`,
    }) as unknown as void; 
  }

  next();
};