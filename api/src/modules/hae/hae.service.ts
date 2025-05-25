import * as haeRepositoryImpl from "./hae.repository";
import * as employeeRepositoryImpl from "../employee/employee.repository";
import { Status } from '@prisma/client';

export const changeHAEStatus = async (
  id: string,
  haeStatus: Status
) => {
  try {
    const record = await employeeRepositoryImpl.findById(id);

    if (!record) throw new Error("Employee not found");

    return await haeRepositoryImpl.changeHAEStatus(id, haeStatus);
  } catch (error: any) {
    throw new Error(error.message);
  }
} 