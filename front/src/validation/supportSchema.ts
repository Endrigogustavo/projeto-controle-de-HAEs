import * as yup from "yup";

export type ErrorFields = {
  email: string;
  subject: string;
  description: string;
};

export const supportSchema = yup.object().shape({
  email: yup
    .string()
    .email("Por favor, insira um email válido.")
    .required("O email é obrigatório."),
  subject: yup.string().required("O assunto é obrigatório."),
  description: yup.string().required("Por favor, descreva o erro."),
});
