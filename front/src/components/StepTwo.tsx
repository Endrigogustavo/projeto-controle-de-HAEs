import { StepProps } from './types';

const StepTwo: React.FC<StepProps> = ({ onNext, onBack }) => {
  return (
    <div className="space-y-4">
      <p>Calendário (exemplo de conteúdo)</p>
      <div className="flex justify-between">
        <button
          onClick={onBack}
          className="bg-gray-300 text-gray-800 py-2 px-4 rounded hover:bg-gray-400"
        >
          Voltar
        </button>
        <button
          onClick={onNext}
          className="bg-red-600 text-white py-2 px-4 rounded hover:bg-red-700"
        >
          Continuar
        </button>
      </div>
    </div>
  );
};

export default StepTwo;
