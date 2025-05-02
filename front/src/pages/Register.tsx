import { TextField } from "@mui/material";
import { PasswordField } from "@components/PasswordField";

export default function Register() {
  return (
    <>
      <div className="flex flex-col items-center justify-center h-screen">
        <div className="p-8 xl:w-xl">
          <img
            src="/fatec_zona_leste_icon.png"
            alt="Logo da Fatec da Zona Leste"
            className="mb-4 w-24"
          />
          <h1 className="text-xl my-4 font-semibold">
            Sistema de Controle de
            <span className="text-red-fatec"> HAEs</span>
          </h1>

          <form action="" className="flex flex-col">
            <TextField required label="Nome" />

            <TextField
              type="email"
              label="E-mail institucional"
              placeholder="nome@fatec.sp.gov.br"
              sx={{ margin: "1rem 0" }}
              required
            />

            <PasswordField />

            <button
              type="submit"
              className="bg-red-fatec text-white p-2 rounded my-2 uppercase"
            >
              Cadastrar
            </button>
          </form>

          <p>
            Já possui uma conta? <a href="/login">Entre</a>
          </p>
        </div>
      </div>
    </>
  );
}
