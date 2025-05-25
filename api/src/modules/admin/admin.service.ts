import * as adminRepositoryImpl from "./admin.repository";
import * as employeeRepositoryImpl from "../employee/employee.repository";
import { Role } from '@prisma/client';


export const changeEmployeeRoleToCoordenador = async (id: string, role: Role) => {
  try {
    const record = await employeeRepositoryImpl.findById(id);

    if (!record) throw new Error("Employee not found");

    return await adminRepositoryImpl.changeEmployeeRole(id, role);
  } catch (error: any) {
    throw new Error(error.message);
  }
}