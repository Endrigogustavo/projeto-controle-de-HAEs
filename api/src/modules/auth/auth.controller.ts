import { Request, Response } from "express";
import { StatusCodes } from "http-status-codes";

import * as authService from "./auth.service";
import { yupValidation } from "../../shared/middleware";
import * as yup from "yup";
interface ISignInUser {
  email: string;
  password: string;
}

// Validations
export const signInValidation = yupValidation.validation((get) => ({
  body: get<ISignInUser>(
    yup.object().shape({
      email: yup
        .string()
        .email()
        .matches(/@(fatec)\.sp\.gov\.br$/, "O email deve ser institucional")
        .required(),
      password: yup.string().min(6).required(),
    })
  ),
}));

export const signIn = async (req: Request, res: Response) => {
  const { email, password } = req.body;

  try {
    const user = await authService.signInUser(email, password);

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
      message: "Login successful",
      role: user.record.role,
      userId: user.record.id,
    });
    return;
  } catch (error: any) {
    res.status(StatusCodes.BAD_REQUEST).json({
      message: "Error with login",
      error: error.message,
    });
    return;
  }
};

export const signOut = async (req: Request, res: Response) => {
  try {
    res.clearCookie("token");
    res.status(StatusCodes.OK).json({ message: "Logout successful" });
  } catch (error: any) {
    res.status(StatusCodes.BAD_REQUEST).json({
      message: "Error with logging out",
      error: error.message,
    });
  }
};

export const getCookie = async (req: Request, res: Response) => {
  try {
    const token = req.cookies?.token;

    if (!token) {
      res.status(StatusCodes.UNAUTHORIZED).json({
        message: "Not authenticated",
      });
      return;
    }

    res.status(StatusCodes.OK).json({ token });
  } catch (error: any) {
    res.status(StatusCodes.BAD_REQUEST).json({
      message: "Error retrieving cookie",
      error: error.message,
    });
  }
}
