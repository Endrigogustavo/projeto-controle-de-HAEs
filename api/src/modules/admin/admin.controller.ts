import { Request, Response } from "express";
import { StatusCodes } from "http-status-codes";
import * as adminService from "./admin.service";
import * as employeeService from "../employee/employee.service";
import { Role } from '@prisma/client';

export const changeEmployeeRoleToCoordenador = async (
  req: Request,
  res: Response
) => {
  try {
    const { id } = req.params;
    const record = await employeeService.findEmployeeById(id);

    if (!record) {
      res.status(StatusCodes.NOT_FOUND).json({ error: "Professor not found" });
      return;
    }

    if (record.role === Role.COORDENADOR) {
      res.status(StatusCodes.BAD_REQUEST).json({
        error: "Professor is already a Coordenador",
      });
      return;
    }

    await adminService.changeEmployeeRoleToCoordenador(id, Role.COORDENADOR);
    res.status(StatusCodes.OK).json({ message: "Role updated to Coordenador" });
  } catch (error: any) {
    res
      .status(StatusCodes.INTERNAL_SERVER_ERROR)
      .json({ error: error.message });
  }
}

