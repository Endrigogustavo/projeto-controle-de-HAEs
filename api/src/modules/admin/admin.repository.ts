import { repository } from "../../database/repository";
import { Role } from '@prisma/client';

const employeeRepository = repository.employee;

export const changeEmployeeRole = async (id: string, role: Role) => {
  return await employeeRepository.update({
    where: {
      id: id,
    },
    data: {
      role: role,
    },
  });
}

