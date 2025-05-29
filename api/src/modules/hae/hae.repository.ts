import { Hae, Status } from "@prisma/client";;
import { repository } from "../../database/repository";

const haeRepository = repository.hae;

export const createHae = async (data: {
  nameEmployee: string;
  course: string;
  projectTitle: string;
  weeklyHours: number;
  projectType: string;
  dayOfWeek:   string;
  timeRange: string;
  cronograma: string[];
  projectDescription: string;
  observations: string;
  startDate: string;
  endDate: string;
  employeeId: string;
}) => {
  try {
    const newHae = await haeRepository.create({
      data: {
        ...data,
        resultAchieved: "", 
        coordenatorId: "", 
        comprovanteDoc: "", 
      },
    });

    return newHae;
  } catch (error) {
    console.error('Erro ao criar HAE:', error);
    throw new Error('Erro ao criar HAE');
  }
};

export const findAllHaes = async () => {
  try {
    const haes = await haeRepository.findMany();
    return haes;
  } catch (error) {
    console.error('Erro ao encontrar todos os HAEs:', error);
    throw new Error('Erro ao encontrar todos os HAEs');
  }
}

export const deleteHae = async (id: string) => {
  try {
    await haeRepository.delete({
      where: {
        id: id,
      },
    });
  } catch (error) {
    console.error('Erro ao deletar HAE:', error);
    throw new Error('Erro ao deletar HAE');
  }
 }

export const findHaeById = async (id: string) => {
  try {
    const hae = await haeRepository.findUnique({
      where: {
        id: id,
      },
    });

    if (!hae) {
      throw new Error('Hae not found');
    }

    return hae;
  } catch (error) {
    console.error('Erro ao encontrar HAE:', error);
    throw new Error('Erro ao encontrar HAE');
  }
 }


export const findAllHaesByProfessorId = async (id: string) => { 
  try {
    const haes = await haeRepository.findMany({
      where: {
        employeeId: id,
      },
    });
    return haes;
  } catch (error) {
    console.error('Erro ao encontrar todos os HAEs por ID do professor:', error);
    throw new Error('Erro ao encontrar todos os HAEs por ID do professor');
  }
}

export const changeHAEStatus = async (HAEId: string, status: Status, coordenadorId: string) => {
  return await haeRepository.update({
    where: {
      id: HAEId,
    },
    data: {
      status: status,
      coordenatorId: coordenadorId,
    },
  });
}

