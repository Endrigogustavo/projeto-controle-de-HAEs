import React from "react";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import { TextField, FormControl, InputLabel, OutlinedInput, InputAdornment, IconButton } from "@mui/material";

export default function Login() {
  const [showPassword, setShowPassword] = React.useState(false);

  const handleClickShowPassword = () => setShowPassword((show) => !show);

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
            <TextField
              type="email"
              label="E-mail institucional"
              placeholder="nome@fatec.sp.gov.br"
              sx={{ margin: "1rem 0" }}
              required
            />

            <FormControl>
              <InputLabel htmlFor="outlined-adornment-password">
                Password
              </InputLabel>
              <OutlinedInput
                id="outlined-adornment-password"
                type={showPassword ? "text" : "password"}
                required
                endAdornment={
                  <InputAdornment position="end">
                    <IconButton
                      aria-label={
                        showPassword
                          ? "Esconder a senha"
                          : "Mostrar a senha"
                      }
                      onClick={handleClickShowPassword}
                      edge="end"
                    >
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                }
                label="Password"
              />
            </FormControl>

            <a href="">Esqueceu a senha?</a>

            <button
              type="submit"
              className="bg-red-fatec text-white p-2 rounded mt-6 mb-2 uppercase"
            >
              Entrar
            </button>
          </form>

          <p>
            É seu primeiro acesso? <a href="">Cadastre-se</a>
          </p>
        </div>
      </div>
    </>
  );
}
