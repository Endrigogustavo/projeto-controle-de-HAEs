import dayjs from "dayjs";

import { cryptUtils, jwtUtils, mailerUtils } from "../../../shared/utils";
import * as emailRepositoryImpl from "./email-verification.repository";
import * as employeeService from "../../employee/employee.service";
import * as employeeRepositoryImpl from "../../employee/employee.repository";

export const generateCodeAndSendEmailCode = async (
  email: string,
  password: string,
  name: string
) => {
  try {
    const employee = await employeeRepositoryImpl.findByEmail(email);

    if (employee) {
      throw new Error("Email Already In Use");
    }

    const code = mailerUtils.generateCode();

    const expiresAt = dayjs().add(10, "minute").toDate();

    await emailRepositoryImpl.save(email, password, name, code, expiresAt);

    await mailerUtils.transporter.sendMail(
      mailerUtils.mailOptions(email, code)
    );

    return code;
  } catch (error: any) {
    throw new Error(error.message);
  }
};

export const verifyEmailCode = async (email: string, code: string) => {
  try {
    const record = await emailRepositoryImpl.findByEmail(email, code);

    if (!record) {
      throw new Error("Invalid verification code or email.");
    }

    if (dayjs().isAfter(record.expiresAt)) {
      await emailRepositoryImpl.deleteByEmail(email);
      throw new Error("Verification code has expired.");
    }

    await emailRepositoryImpl.deleteByEmail(email);

    const user = await employeeService.createEmployee(
      record.name,
      record.email,
      await cryptUtils.hashPassword(record.password)
    );

    if (!user) {
      throw new Error("Error while registering the user");
    }

    const acessUserToken = await jwtUtils.sign({ sub: user.id });

    return { acessUserToken, record: user };
  } catch (error: any) {
    throw new Error(error.message);
  }
};
