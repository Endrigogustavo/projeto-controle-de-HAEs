import { Hae, Status } from "@prisma/client";;
import { repository } from "../../database/repository";

const haeRepository = repository.hae;

export const createHae = async (name: string, email: string, password: string, createdAt: Date, updatedAt: Date) => {}
export const updateHaeInfo = async (id: string, name: string, email: string, password: string) => {}
export const deleteHae = async (id: string) => {}
export const defineHaeAprovedStatus = async (id: string, status: string) => {}
export const findHaeById = async (id: string) => {}
export const generateHaeRelatoryToDean = async (id: string) => {}
export const findAllStudentsIntoHae = async (id: string) => {}
export const findAllHaes = async () => {}
export const findAllHaesByProfessorEmail = async (email: string) => {}

export const changeHAEStatus = async (id: string, status: Status) => {
  return await haeRepository.update({
    where: {
      id: id,
    },
    data: {
      status: status,
    },
  });
}