import { TextField, MenuItem } from '@mui/material';
import { StepProps } from './types';

const StepOne: React.FC<StepProps> = ({ onNext }) => {
  return (
    <form className="flex flex-col gap-10">
      <h2 className="font-semibold text-gray-700 mb-2">
        Solicitação de <span className="text-red-fatec font-bold">HAE</span>
      </h2>

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
        className="w-full bg-red-fatec text-white py-2 rounded"
      >
        CONTINUAR
      </button>
    </form>
  );
};

export default StepOne;