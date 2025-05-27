import { Request, Response, NextFunction } from "express";
import { StatusCodes } from "http-status-codes";
import * as haeService from "./hae.service";

export const changeHAEStatus = async (
  req: Request,
  res: Response,
): Promise<void> => {
  try {
    const { id } = req.params;
    const { haeStatus, coordenadorId } = req.body;

    const validStatuses = ["APPROVED", "REJECTED"];
    if (!validStatuses.includes(haeStatus)) {
      res.status(StatusCodes.BAD_REQUEST).json({
        message: "Invalid HAE status. Valid statuses are: APPROVED, REJECTED.",
      });
      return;
    }

    if (!coordenadorId) {
      res.status(StatusCodes.BAD_REQUEST).json({
        message: "Coordenador ID is required.",
      });
      return;
    }

    await haeService.changeHAEStatus(id, haeStatus, coordenadorId);
    res.status(StatusCodes.OK).json({ message: "HAE status updated" });
  } catch (error: any) {
    res
      .status(StatusCodes.BAD_REQUEST)
      .json({ error: error.message });
  }
};


export const deleteHae = async (
  req: Request,
  res: Response,
) => {
  try {
    const { id } = req.params;
    await haeService.deleteHae(id);
    res.status(StatusCodes.OK).json({ message: "HAE deleted successfully" });
  } catch (error: any) {
    res
      .status(StatusCodes.BAD_REQUEST)
      .json({ error: error.message });
  }
};

export const findHaeById = async (
  req: Request,
  res: Response,
) => {
  try {
    const { id } = req.params;
    const hae = await haeService.findHaeWithId(id);
    res.status(StatusCodes.OK).json({ message: "HAE found", hae });
  } catch (error: any) {
    res
      .status(StatusCodes.BAD_REQUEST)
      .json({ error: error.message });
  }
};

export const findAllHaes = async (
  req: Request,
  res: Response,
) => {
  try {
    const haes = await haeService.findAllHaes();
    res.status(StatusCodes.OK).json({ message: "All HAEs found", haes });
  } catch (error: any) {
    res
      .status(StatusCodes.BAD_REQUEST)
      .json({ error: error.message });
  }
};

export const findAllHaesByProfessorId = async (
  req: Request,
  res: Response,
) => {
  try {
    const { id } = req.params;
    const haes = await haeService.findAllHaesByProfessorId(id);
    res.status(StatusCodes.OK).json({ message: "HAEs by professor found", haes });
  } catch (error: any) {
    res
      .status(StatusCodes.BAD_REQUEST)
      .json({ error: error.message });
  }
};

export const createHae = async (
  req: Request,
  res: Response,
) => {
  try {
    const haeData = req.body;
    const newHae = await haeService.createHae(haeData);
    res.status(StatusCodes.CREATED).json({ message: "HAE created successfully", newHae });
  } catch (error: any) {
    res
      .status(StatusCodes.BAD_REQUEST)
      .json({ error: error.message });
  }
};



