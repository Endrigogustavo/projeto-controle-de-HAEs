import { StepProps } from "./types";

const StepTwo: React.FC<StepProps> = ({ onNext, onBack }) => {
	return (
		<div className="space-y-4">
			<div>
				<h2 className="font-semibold subtitle">Cronograma da Atividade</h2>
				<p>
					Informe as datas de início e fim, dias da semana e horários da sua
					HAE.
				</p>
			</div>
			<div className="flex justify-between">
				<button
					onClick={onBack}
					className="bg-gray-300 text-gray-800 py-2 px-4 rounded hover:bg-gray-400"
				>
					Voltar
				</button>
				<button
					onClick={onNext}
					className="btnFatec py-2 px-4 hover:bg-red-700"
				>
					<p>Continuar</p>
				</button>
			</div>
		</div>
	);
};

export default StepTwo;
