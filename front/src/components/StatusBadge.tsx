import React from "react";

import {
  CheckCircleOutline,
  HighlightOffOutlined,
  HourglassEmpty,
  WorkspacePremiumOutlined,
} from "@mui/icons-material";

interface StatusBadgeProps {
  status: "PENDENTE" | "APROVADO" | "REPROVADO" | "COMPLETO" | string;
  isFullView: boolean;
}

export const StatusBadge: React.FC<StatusBadgeProps> = ({
  status,
  isFullView,
}) => {
  const statusStyles: Record<
    string,
    { bg: string; text: string; icon: React.ReactElement }
  > = {
    PENDENTE: {
      bg: "bg-yellow-100",
      text: "text-gray-900",
      icon: (
        <HourglassEmpty sx={{ fontSize: isFullView ? "1.3rem" : "1.2rem" }} />
      ),
    },
    APROVADO: {
      bg: "bg-green-100",
      text: "text-gray-900",
      icon: (
        <CheckCircleOutline
          sx={{ fontSize: isFullView ? "1.3rem" : "1.2rem" }}
        />
      ),
    },
    REPROVADO: {
      bg: "bg-red-100",
      text: "text-gray-900",
      icon: (
        <HighlightOffOutlined
          sx={{ ontSize: isFullView ? "1.3rem" : "1.2rem" }}
        />
      ),
    },
    COMPLETO: {
      bg: "bg-blue-100",
      text: "text-gray-900",
      icon: (
        <WorkspacePremiumOutlined
          sx={{ fontSize: isFullView ? "1.3rem" : "1.2rem" }}
        />
      ),
    },
  };

  const style = statusStyles[status] || statusStyles.PENDENTE;

  return (
    <div
      className={`inline-flex items-center gap-1.5 ${
        isFullView ? "px-2.5" : "px-2"
      } ${isFullView ? "py-2.5" : "py-1.5"} rounded-full font-medium ${
        isFullView ? "text-sm " : "text-xs"
      } ${style.bg} ${style.text}`}
    >
      {style.icon}
      <span>{status}</span>
    </div>
  );
};
