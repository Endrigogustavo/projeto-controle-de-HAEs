import { Button } from "@mui/material";
import { useNavigate } from "react-router-dom";

type CardHaeProps = {
	titulo: string;
	curso: string;
	descricao: string;
	id: string;
	onDelete: () => void;
};

export const CardRequestHae = ({
	titulo,
	curso,
	descricao,
	id,
	onDelete,
}: CardHaeProps) => {
	const navigate = useNavigate();

	const handleEdit = () => {
		navigate(`/requestHae`, { state: { haeId: id } });
	};

	return (
		<div className="bg-white rounded-xl shadow p-6 mx-auto my-4 border border-gray-200">
			<h2 className="text-xl font-semibold text-gray-800 mb-1">{titulo}</h2>
			<h3 className="text-sm text-gray-600 font-medium mb-4">{curso}</h3>
			<p className="text-sm text-gray-700 mb-6 leading-relaxed">
				Descrição: {descricao}
			</p>
			<div className="flex gap-3">
				<Button
					variant="contained"
					color="info"
					className="!capitalize !rounded-full px-6"
					onClick={handleEdit} 
				>
					Editar Solicitação
				</Button>
				<Button
					variant="contained"
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
