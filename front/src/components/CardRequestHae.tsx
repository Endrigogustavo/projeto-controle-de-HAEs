import { useNavigate } from "react-router-dom";
import { StatusBadge } from "@/components/StatusBadge";
import {
  PersonOutline,
  NotesOutlined,
} from "@mui/icons-material";

type CardHaeProps = {
  id: string;
  titulo: string;
  curso: string;
  descricao: string;
  status: "PENDENTE" | "APROVADO" | "REPROVADO" | "COMPLETO" | "FECHAMENTO_SOLICITADO" | string;
  endDate: string;
  professor: string;
  onDelete: () => void;
  onEdit: () => void;
};

export const CardRequestHae = ({
  id,
  titulo,
  curso,
  descricao,
  status,
  endDate,
  professor,
  onDelete,
  onEdit,
}: CardHaeProps) => {
  const navigate = useNavigate();

  const canRequestClosure = () => {
    if (!endDate || status !== "APROVADO") {
      return false;
    }

    const endDateTime = new Date(endDate + "T23:59:59");
    const today = new Date();

    if (isNaN(endDateTime.getTime())) {
      console.error(
        `[HAE ID: ${id}] - Data de término inválida recebida:`,
        endDate
      );
      return false;
    }

    const oneWeekInMilliseconds = 7 * 24 * 60 * 60 * 1000;
    const oneWeekBeforeEnd = new Date(
      endDateTime.getTime() - oneWeekInMilliseconds
    );

    return today >= oneWeekBeforeEnd;
  };

  const handleRequestClosure = (e: React.MouseEvent) => {
    e.stopPropagation();
    navigate(`/request-closure/${id}`, { state: { projectTitle: titulo } });
  };

  const handleVerMais = () => {
    navigate(`/hae/${id}`);
  };

  return (
    <div
      onClick={handleVerMais}
      className="bg-white rounded-md shadow p-6 border border-gray-200 flex flex-col h-72 cursor-pointer transition-shadow hover:shadow-lg"
    >
      <div className="flex justify-between items-start mb-4">
        <div className="flex-1 min-w-0 pr-4">
          <h2 className="text-xl font-semibold text-gray-800 truncate mb-1">
            {titulo}
          </h2>
          <h3 className="text-sm text-gray-600 font-medium truncate">
            {curso}
          </h3>
        </div>
        <div className="flex-shrink-0">
          <StatusBadge status={status} isFullView={false} />
        </div>
      </div>

      <div className="flex-grow space-y-2 mb-4">
        <div className="flex items-center gap-1.5 text-sm text-gray-600">
          <PersonOutline sx={{ fontSize: "1.2rem", flexShrink: 0 }} />
          <span className="truncate">{professor}</span>
        </div>
        <div className="flex items-start gap-1.5 text-sm text-gray-600">
          <NotesOutlined
            sx={{ fontSize: "1.2rem", marginTop: "2px", flexShrink: 0 }}
          />
          <p className="line-clamp-3 text-gray-700">{descricao}</p>
        </div>
      </div>

      <div className="flex flex-col md:flex-row gap-3 justify-end items-center mt-auto pt-4 border-t border-gray-100">
        {canRequestClosure() && (
          <button
            onClick={handleRequestClosure}
            className="btnFatec mb-4 px-3 py-2 text-white uppercase bg-red-800 hover:bg-red-900"
          >
            Solicitar Fechamento
          </button>
        )}

        {status === "PENDENTE" && (
          <>
            <button
              className="btnFatec bg-gray-600 text-white uppercase hover:bg-gray-900"
              onClick={(e) => {
                e.stopPropagation();
                onEdit();
              }}
            >
              Editar Solicitação
            </button>
            <button
              onClick={(e) => {
                e.stopPropagation();
                onDelete();
              }}
              className="btnFatec text-white uppercase hover:bg-red-900"
            >
              Excluir
            </button>
          </>
        )}

        {!(status === "PENDENTE" || canRequestClosure()) && (
          <p className="text-sm text-gray-500">Clique para ver mais detalhes</p>
        )}
      </div>
    </div>
  );
};
