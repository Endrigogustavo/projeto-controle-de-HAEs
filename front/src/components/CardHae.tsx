import { SchoolOutlined } from "@mui/icons-material";
import { StatusBadge } from "@components/StatusBadge";

type CardHaeProps = {
	titulo: string;
	curso: string;
	status: "PENDENTE" | "APROVADO" | "REPROVADO" | "COMPLETO" | string;
	descricao: string;
};

export const CardHae = ({ titulo, curso, status, descricao }: CardHaeProps) => {
	return (
		<div className="bg-white shadow-md rounded-lg border border-gray-200 p-5 flex flex-col gap-4">
			<div className="flex justify-between items-start gap-4">
				<div className="flex-1">
					<h3 className="text-lg font-bold text-gray-900 leading-tight">
						{titulo}
					</h3>
				</div>
				<StatusBadge status={status} />
			</div>
			<div>
				<div className="flex items-center gap-1.5 text-sm text-gray-600 mb-2">
					<SchoolOutlined sx={{ fontSize: "1rem" }} />
					<span>{curso}</span>
				</div>
				<p className="text-gray-700 text-sm line-clamp-2" title={descricao}>
					{descricao}
				</p>
			</div>
		</div>
	);
};