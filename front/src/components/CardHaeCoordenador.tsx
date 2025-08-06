import { useNavigate } from "react-router-dom";
import { SchoolOutlined, PersonOutline } from "@mui/icons-material";
import { StatusBadge } from "@components/StatusBadge";

type CardHaeProps = {
	id: string;
	titulo: string;
	curso: string;
	status: "PENDENTE" | "APROVADO" | "REPROVADO" | "COMPLETO" | string;
	descricao: string;
	professor: string;
};

export const CardHaeCoordenador = ({
	id,
	titulo,
	curso,
	status,
	descricao,
	professor,
}: CardHaeProps) => {
	const navigate = useNavigate();
	const handleVerMais = () => navigate(`/hae/${id}`);

	return (
		<div className="bg-white shadow-md rounded-lg border border-gray-200 p-5 flex flex-col gap-4 hover:shadow-xl hover:border-blue-300 transition-all duration-300">
			<div className="flex justify-between items-start gap-4">
				<div className="flex-1">
					<h3 className="text-lg font-bold text-gray-900 leading-tight">
						{titulo}
					</h3>
					<div className="flex items-center gap-2 mt-1 text-sm text-gray-500">
						<PersonOutline sx={{ fontSize: "1.1rem" }} />
						<span>{professor}</span>
					</div>
				</div>
				<StatusBadge status={status} />
			</div>
			<div className="flex-grow">
				<div className="flex items-center gap-1.5 text-sm text-gray-600 mb-2">
					<SchoolOutlined sx={{ fontSize: "1rem" }} />
					<span>{curso}</span>
				</div>
				<p className="text-gray-700 text-sm line-clamp-2" title={descricao}>
					{descricao}
				</p>
			</div>
			<div className="mt-auto pt-4 flex justify-end border-t border-gray-100">
				<button
					onClick={handleVerMais}
					className="text-sm font-semibold text-blue-600 hover:text-blue-800 hover:underline transition-colors"
				>
					Ver mais detalhes →
				</button>
			</div>
		</div>
	);
};
