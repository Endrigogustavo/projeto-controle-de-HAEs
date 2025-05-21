import { Request, Response } from "express";
import { StatusCodes } from "http-status-codes";
import * as employeeService from "./employee.service";

export const update = async (req: Request, res: Response) => {
  try {
    const { name, email, password } = req.body;
    const { id } = req.params;
    await employeeService.updateEmployee(id, name, email, password);
    res
      .status(StatusCodes.OK)
      .json({ message: "Professor updated successfully" });
  } catch (error: any) {
    res
      .status(StatusCodes.INTERNAL_SERVER_ERROR)
      .json({ error: error.message });
  }
};

export const remove = async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    await employeeService.deleteEmployeeById(id);
    res
      .status(StatusCodes.OK)
      .json({ message: "Professor deleted successfully" });
  } catch (error: any) {
    res
      .status(StatusCodes.INTERNAL_SERVER_ERROR)
      .json({ error: error.message });
  }
};

export const findById = async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const record = await employeeService.findEmployeeById(id);

    if (!record) {
      res.status(StatusCodes.NOT_FOUND).json({ error: "Professor not found" });
      return;
    }
    res.status(StatusCodes.OK).json({ message: "Professor found", record });
  } catch (error: any) {
    res
      .status(StatusCodes.INTERNAL_SERVER_ERROR)
      .json({ error: error.message });
  }
};
