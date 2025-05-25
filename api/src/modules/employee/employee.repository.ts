import { repository } from "../../database/repository";
import { Status } from '@prisma/client';

const employeeRepository = repository.employee;

export const createEmployee = async (
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

export const updateEmployeeInfo = async (
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

export const deleteEmployee = async (id: string) => {
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

export const findAllEmployees = async () => {
  return await employeeRepository.findMany();
};


