import { Request, Response, NextFunction } from "express";
import { StatusCodes } from "http-status-codes";
import * as haeService from "./hae.service";

export const changeHAEStatus = async (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  try {
    const { id } = req.params;
    const { haeStatus } = req.body;

    await haeService.changeHAEStatus(id, haeStatus);
    res.status(StatusCodes.OK).json({ message: "HAE status updated" });
  } catch (error) {
    next(error);
  }
};