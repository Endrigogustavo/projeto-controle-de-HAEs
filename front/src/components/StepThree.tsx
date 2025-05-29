import { TextField } from "@mui/material";
import { StepProps } from "./types";

const StepThree: React.FC<StepProps> = ({ onBack }) => {
	return (
		<div className="space-y-4">
			<div>
				<h2 className="font-semibold subtitle">Informações Adicionais</h2>
				<p>
					Adicione detalhes complementares, observações relevantes ou anexe
					comprovantes da sua HAE.
				</p>
			</div>
			<div className="my-2">
				<TextField fullWidth label="Observações" multiline minRows={3} />
			</div>

			<div className="flex justify-between">
				<button
					onClick={onBack}
					className="bg-gray-300 text-gray-800 py-2 px-4 btnFatec hover:bg-gray-400"
				>
					Voltar
				</button>
				<button className="bg-green-600 text-white py-2 px-4 btnFatec hover:bg-green-700">
					<p>Enviar</p>
				</button>
			</div>
		</div>
	);
};

export default StepThree;
