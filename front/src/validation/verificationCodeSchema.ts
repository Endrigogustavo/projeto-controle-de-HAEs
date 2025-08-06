import * as yup from "yup";

export const verificationCodeSchema = yup
	.object({
		code: yup
			.string()
			.matches(/^\d{6}$/, "O código deve ter 6 dígitos numéricos")
			.required("O código é obrigatório"),
	})
	.required();
