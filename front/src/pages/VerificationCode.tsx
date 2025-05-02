import {
  TextField,
  FormControl,
} from "@mui/material";

export default function VerificationCode() {


  return (
    <>
      <div className="flex flex-col items-center justify-center h-screen">
        <div className="p-8 xl:w-xl">
          <h1 className="text-xl my-4 font-semibold">
            Confirme seu
            <span className="text-red-fatec"> Cadastro</span>
          </h1>
          <p className="my-4">
          Nós enviamos um código de confirmação para o seu <a href="/#">e-mail institucional.</a>
          </p>

          <form action="" className="flex flex-col ">
            <FormControl >
              <TextField required label="Código" 
              placeholder="369777"
              />
            </FormControl>

            <button
              type="submit"
              className="bg-red-fatec text-white p-2 rounded my-2 uppercase my-4"
            >
              Cadastrar
            </button>
          </form>
        </div>
      </div>
    </>
  );
}
