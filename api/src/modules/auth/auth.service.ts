import { cryptUtils, jwtUtils } from "../../shared/utils";
import * as employeeRepositoryImpl from "../employee/employee.repository";

export const signInUser = async (email: string, password: string) => {
  try {
    const record = await employeeRepositoryImpl.findByEmail(email);

    if (!record) throw new Error("Invalid Credentials");

    const passwordMatch = await cryptUtils.verifyPassword(
      password,
      record.password
    );

    if (!passwordMatch) throw new Error("Invalid Credentials");

    const acessUserToken = jwtUtils.sign({ sub: record.id });

    return { acessUserToken, record };
  } catch (error: any) {
    throw new Error(error.message);
  }
};
