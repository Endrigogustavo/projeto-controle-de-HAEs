import { Router } from "express";

import * as authController from "./auth.controller";
import * as emailController from "./email-verification/email-verification.controller";
import * as requestRateLimit from "../../shared/middleware/request-rate-limit";

export const authRoutes = Router();

authRoutes.post(
  "/send-email-code",
  requestRateLimit.sendCodeRateLimit,
  emailController.sendVerificationEmailCodeValidation,
  emailController.sendVerificationEmailCode
);

authRoutes.post(
  "/verify-email-code",
  requestRateLimit.verifyCodeRateLimit,
  emailController.verifyVerificationEmailCodeValidation,
  emailController.verifyVerificationEmailCode
);

authRoutes.post(
  "/login",
  requestRateLimit.loginRateLimit,
  authController.signInValidation,
  authController.signIn
);

