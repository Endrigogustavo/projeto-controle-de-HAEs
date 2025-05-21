import { Hae } from "@prisma/client";;
import { repository } from "../../database/repository";

const haeRepository = repository.hae;

const createHae = async (name: string, email: string, password: string, createdAt: Date, updatedAt: Date) => {}
const updateHaeInfo = async (id: string, name: string, email: string, password: string) => {}
const deleteHae = async (id: string) => {}
const defineHaeAprovedStatus = async (id: string, status: string) => {}
const findHaeById = async (id: string) => {}
const generateHaeRelatoryToDean = async (id: string) => {}
const findAllStudentsIntoHae = async (id: string) => {}
const findAllHaes = async () => {}
const findAllHaesByProfessorEmail = async (email: string) => {}

export const haeRepositoryImpl = {

}