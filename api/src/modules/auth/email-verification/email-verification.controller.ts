import { Request, Response } from "express";
import { StatusCodes } from "http-status-codes";
import * as yup from "yup";

import * as emailService from "./email-verification.service";
import { yupValidation } from "../../../shared/middleware";

interface ISendCodeBodyProps {
  email: string;
  password: string;
  name: string;
}

interface IVerifyCodeBodyProps {
  email: string;
  code: string;
}

// Validations
export const sendVerificationEmailCodeValidation = yupValidation.validation(
  (get) => ({
    body: get<ISendCodeBodyProps>(
      yup.object().shape({
        email: yup
          .string()
          .email()
          .matches(/@(fatec)\.sp\.gov\.br$/, "Email must be institutional")
          .required(),
        name: yup.string().min(3).required(),
        password: yup.string().min(6).required(),
      })
    ),
  })
);

export const verifyVerificationEmailCodeValidation = yupValidation.validation(
  (get) => ({
    body: get<IVerifyCodeBodyProps>(
      yup.object().shape({
        email: yup
          .string()
          .email()
          .matches(/@fatec\.sp\.gov\.br$/, "Email must be institutional")
          .required("Email is required"),
        code: yup
          .string()
          .matches(/^\d{6}$/, "The code must contain exactly 6 numeric digits")
          .required("Code is required"),
      })
    ),
  })
);

export const sendVerificationEmailCode = async (
  req: Request,
  res: Response
) => {
  try {
    const { email, password, name } = req.body;

    const record = await emailService.generateCodeAndSendEmailCode(
      email,
      password,
      name
    );

    if (!record) {
      res.status(StatusCodes.UNAUTHORIZED).json({
        message: "Unable to process request.",
      });
      return;
    }

    res
      .status(StatusCodes.OK)
      .json({ message: "Verification code sent successfully." });
  } catch (error: any) {
    res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
      message: "An error occurred while processing the request.",
      error: error.message,
    });
  }
};

export const verifyVerificationEmailCode = async (
  req: Request,
  res: Response
) => {
  try {
    const { email, code } = req.body;

    const user = await emailService.verifyEmailCode(email, code);

    if (!user) {
      res.status(StatusCodes.UNAUTHORIZED).json({
        message: "Unable to process request.",
      });
      return;
    }

    res.cookie("token", user.acessUserToken, {
      httpOnly: true,
      secure: false,
      sameSite: "strict",
      maxAge: 3600000,
    });

    res.status(StatusCodes.OK).json({
      message: "Email verified successfully And User Registered",
      role: user.record.role,
      userId: user.record.id,
    });
  } catch (error: any) {
    res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
      message: "An error occurred while processing the request.",
      error: error.message,
    });
  }
};
