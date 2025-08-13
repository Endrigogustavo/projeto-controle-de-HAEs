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
      text: "text-gray-900",
      icon: <HourglassEmpty sx={{ fontSize: "1.3rem" }} />,
    },
    APROVADO: {
      bg: "bg-green-100",
      text: "text-gray-900",
      icon: <CheckCircleOutline sx={{ fontSize: "1.3rem" }} />,
    },
    REPROVADO: {
      bg: "bg-red-100",
      text: "text-gray-900",
      icon: <HighlightOffOutlined sx={{ fontSize: "1.3rem" }} />,
    },
    COMPLETO: {
      bg: "bg-blue-100",
      text: "text-gray-900",
      icon: <WorkspacePremiumOutlined sx={{ fontSize: "1.3rem" }} />,
    },
  };

  const style = statusStyles[status] || statusStyles.PENDENTE;

  return (
    <div
      className={`inline-flex items-center gap-1.5 px-2.5 py-2.5 rounded-full font-medium  text-sm ${style.bg} ${style.text}`}
    >
      {style.icon}
      <span>{status}</span>
    </div>
  );
};
