import * as yup from "yup";

export const registerSchema = yup
	.object({
		name: yup
			.string()
			.min(3, "Nome deve ter no mínimo 3 caracteres")
			.required("Nome é obrigatório"),
		email: yup
			.string()
			.email("E-mail inválido")
			.matches(/@fatec\.sp\.gov\.br$/, "E-mail deve ser institucional")
			.required("E-mail é obrigatório"),
		password: yup
			.string()
			.min(6, "Senha deve ter no mínimo 6 caracteres")
			.required("Senha é obrigatória"),
	})
	.required();
