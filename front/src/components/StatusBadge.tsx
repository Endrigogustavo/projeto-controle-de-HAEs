import React from "react";

import {
	CheckCircleOutline,
	HighlightOffOutlined,
	HourglassEmpty,
	WorkspacePremiumOutlined, 
} from "@mui/icons-material";

interface StatusBadgeProps {
	status: "PENDENTE" | "APROVADO" | "REPROVADO" | "COMPLETO" | string;
}

export const StatusBadge: React.FC<StatusBadgeProps> = ({ status }) => {
	const statusStyles: Record<
		string,
		{ bg: string; text: string; icon: React.ReactElement }
	> = {
		PENDENTE: {
			bg: "bg-yellow-100",
			text: "text-yellow-800",
			icon: <HourglassEmpty sx={{ fontSize: "1rem" }} />,
		},
		APROVADO: {
			bg: "bg-green-100",
			text: "text-green-800",
			icon: <CheckCircleOutline sx={{ fontSize: "1rem" }} />,
		},
		REPROVADO: {
			bg: "bg-red-100",
			text: "text-red-800",
			icon: <HighlightOffOutlined sx={{ fontSize: "1rem" }} />,
		},
		COMPLETO: {
			bg: "bg-blue-100",
			text: "text-blue-800",
			icon: <WorkspacePremiumOutlined sx={{ fontSize: "1rem" }} />,
		},
	};

	const style = statusStyles[status] || statusStyles.PENDENTE;

	return (
		<div
			className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full font-medium text-xs ${style.bg} ${style.text}`}
		>
			{style.icon}
			<span>{status}</span>
		</div>
	);
};
