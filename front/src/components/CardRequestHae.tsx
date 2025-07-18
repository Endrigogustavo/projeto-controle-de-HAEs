import { Button } from "@mui/material";

type CardHaeProps = {
	titulo: string;
	curso: string;
	descricao: string;
	onDelete: () => void;
};

export const CardRequestHae = ({ titulo, curso, descricao, onDelete }: CardHaeProps) => {
	return (
		<div className="bg-white rounded-xl shadow p-6 mx-auto my-4 border border-gray-200">
			<h2 className="text-xl font-semibold text-gray-800 mb-1">{titulo}</h2>
			<h3 className="text-sm text-gray-600 font-medium mb-4">{curso}</h3>
			<p className="text-sm text-gray-700 mb-6 leading-relaxed">Descrição: {descricao}</p>
			<div className="flex gap-3">
				<Button variant="contained" color="error" className="!capitalize !rounded-full px-6">
					Editar Solicitação
				</Button>
				<Button
					variant="outlined"
					color="error"
					className="!capitalize !rounded-full px-6"
					onClick={onDelete}
				>
					Deletar
				</Button>
			</div>
		</div>
	);
};
