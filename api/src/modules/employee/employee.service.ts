import * as employeeRepositoryImpl from "./employee.repository";
import { Status } from '@prisma/client';


export const createEmployee = async (
  name: string,
  email: string,
  password: string
) => {
  try {
    return await employeeRepositoryImpl.createEmployee(name, email, password);
  } catch (error) {
    throw new Error("Error while creating the employee");
  }
};

export const updateEmployee = async (
  id: string,
  name: string,
  email: string,
  password: string
) => {
  try {
    const record = await employeeRepositoryImpl.findByEmail(email);

    if (!record) throw new Error("Employee not found");

    return await employeeRepositoryImpl.updateEmployeeInfo(
      id,
      name,
      email,
      password
    );
  } catch (error: any) {
    throw new Error(error.message);
  }
};

export const deleteEmployeeById = async (id: string) => {
  try {
    const record = await employeeRepositoryImpl.findById(id);

    if (!record) throw new Error("Employee not found");

    return await employeeRepositoryImpl.deleteEmployee(id);
  } catch (error: any) {
    throw new Error(error.message);
  }
};

export const findEmployeeById = async (id: string) => {
  try {
    const record = await employeeRepositoryImpl.findById(id);

    if (!record) throw new Error("Employee not found");

    return record;
  } catch (error: any) {
    throw new Error(error.message);
  }
};


