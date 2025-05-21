import { StepProps } from './types';

const StepThree: React.FC<StepProps> = ({ onBack }) => {
  return (
    <div className="space-y-4">
      <p>Observações finais (exemplo de conteúdo)</p>
      <div className="flex justify-between">
        <button
          onClick={onBack}
          className="bg-gray-300 text-gray-800 py-2 px-4 rounded hover:bg-gray-400"
        >
          Voltar
        </button>
        <button
          className="bg-green-600 text-white py-2 px-4 rounded hover:bg-green-700"
        >
          Enviar
        </button>
      </div>
    </div>
  );
};

export default StepThree;
