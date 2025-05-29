import { TextField, MenuItem } from "@mui/material";
import { StepProps } from "./types";

const StepOne: React.FC<StepProps> = ({ onNext }) => {
	return (
		<form className="flex flex-col gap-10">
			<div>
        <h2 className="font-semibold subtitle">
          Definição de Atividade
        </h2>
        <p>
          Forneça os dados essenciais da sua HAE, como título, curso e descrição
          completa.
        </p>
      </div>

			<TextField
				fullWidth
				label="Título do Projeto"
				variant="outlined"
				placeholder="Ex.: Aulas de Legislação para Concurso Público"
			/>

			<TextField
				fullWidth
				label="Curso"
				select
				defaultValue="Análise e Desenvolvimento de Sistemas"
			>
				<MenuItem value="Análise e Desenvolvimento de Sistemas">
					Análise e Desenvolvimento de Sistemas
				</MenuItem>
			</TextField>

			<TextField
				fullWidth
				label="Descrição do Projeto"
				multiline
				minRows={3}
				placeholder="Ex.: O projeto consiste em ministrar aulas preparatórias..."
			/>

			<button
				type="button"
				onClick={onNext}
				className="w-full py-2 btnFatec"
			>
				<p>CONTINUAR</p>
			</button>
		</form>
	);
};

export default StepOne;
