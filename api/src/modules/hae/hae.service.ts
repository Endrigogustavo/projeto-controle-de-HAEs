import * as haeRepositoryImpl from "./hae.repository";
import * as employeeRepositoryImpl from "../employee/employee.repository";
import { Status, Role } from '@prisma/client';

export const changeHAEStatus = async (
  HAEId: string,
  haeStatus: Status,
  coordenadorId: string
) => {
  try {
    const record = await employeeRepositoryImpl.findById(coordenadorId);

    if (!record) throw new Error("Employee not found");
    if (record.role !== Role.COORDENADOR) {
      throw new Error("Only coordinators can change HAE status");
    }

    return await haeRepositoryImpl.changeHAEStatus(HAEId, haeStatus, coordenadorId);
  } catch (error: any) {
    throw new Error(error.message);
  }
} 

export const deleteHae = async (id: string) => {
  try {
    await haeRepositoryImpl.deleteHae(id);
  } catch (error: any) {
    throw new Error(error.message);
  }
}

export const findHaeWithId = async (id: string) => {
  try {
    const hae = await haeRepositoryImpl.findHaeById(id);
    if (!hae) throw new Error("Hae not found");
    return hae;
  } catch (error: any) {
    throw new Error(error.message);
  }
}

export const findAllHaes = async () => {
  try {
    return await haeRepositoryImpl.findAllHaes();
  } catch (error: any) {
    throw new Error(error.message);
  }
}

export const findAllHaesByProfessorId = async (id: string) => {
  try {
    return await haeRepositoryImpl.findAllHaesByProfessorId(id);
  } catch (error: any) {
    throw new Error(error.message);
  }
}

export const createHae = async (haeData: any) => {
  try {
    return await haeRepositoryImpl.createHae(haeData);
  } catch (error: any) {
    throw new Error(error.message);
  }
}