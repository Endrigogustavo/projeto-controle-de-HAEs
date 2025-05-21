import { repository } from "../../database/repository";

const employeeRepository = repository.employee;

export const createProfessor = async (
  name: string,
  email: string,
  password: string
) => {
  return await employeeRepository.create({
    data: {
      name,
      email,
      password,
    },
  });
};

export const findByEmailAndPassword = async (
  email: string,
  password: string
) => {
  return await employeeRepository.findUnique({
    where: {
      email: email,
      password: password,
    },
  });
};

export const updateProfessorInfo = async (
  id: string,
  name: string,
  email: string,
  password: string
) => {
  return await employeeRepository.update({
    where: {
      id: id,
    },
    data: {
      name: name,
      email: email,
      password: password,
    },
  });
};

export const deleteProfessor = async (id: string) => {
  await employeeRepository.delete({
    where: {
      id: id,
    },
  });
};

export const findById = async (id: string) => {
  return await employeeRepository.findUnique({
    where: {
      id: id,
    },
  });
};

export const findByEmail = async (email: string) => {
  return await employeeRepository.findUnique({
    where: {
      email: email,
    },
  });
};
