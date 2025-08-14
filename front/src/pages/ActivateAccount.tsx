import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { CircularProgress, Box, Typography } from "@mui/material";
import { authService } from "@/services";

export const ActivateAccount = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const queryParams = new URLSearchParams(location.search);
  const token = queryParams.get("token");
  const institutionId = queryParams.get("institutionId");

  const [status, setStatus] = useState<"activating" | "success" | "error">(
    "activating"
  );
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    if (!token || !institutionId) {
      setStatus("error");
      setErrorMessage("Link de ativação inválido ou incompleto.");
      return;
    }

    const activate = async () => {
      try {
        const user = await authService.verifyCode(token, institutionId);

        localStorage.setItem("email", user.email);
        localStorage.setItem("token", "session_active");

        setStatus("success");
        setTimeout(() => {
          let redirectPath = "/";
          switch (user.role) {
            case "ADMIN":
              redirectPath = "/dashboard-admin";
              break;
            case "DIRETOR":
              redirectPath = "/dashboard-diretor";
              break;
            case "PROFESSOR":
            default:
              redirectPath = "/dashboard";
              break;
            case "COORDENADOR":
              redirectPath = "/dashboard-coordenador";
              break;
          }
          navigate(redirectPath, { replace: true });
        }, 3000);
      } catch (err: any) {
        setStatus("success");
        setErrorMessage(
          err.response?.data?.message ||
            "Falha ao ativar a conta. O link pode ter expirado."
        );
      }
    };

    activate();
  }, [token, institutionId, navigate]);

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      height="100vh"
      textAlign="center"
      p={2}
    >
      {status === "activating" && (
        <>
          <CircularProgress
            size={40}
            sx={{
              "& .MuiCircularProgress-circle": {
                stroke: "#c10007",
              },
            }}
          />

          <Typography variant="h6" mt={2}>
            Ativando sua conta, por favor aguarde...
          </Typography>
        </>
      )}
      {status === "success" && (
        <>
          <Typography variant="h5" color="success.main">
            Conta Ativada com Sucesso!
          </Typography>
          <Typography mt={1}>
            Você será redirecionado para o seu painel em breve.
          </Typography>
        </>
      )}
      {status === "error" && (
        <>
          <Typography variant="h5" color="error.main">
            Ocorreu um Erro
          </Typography>
          <Typography mt={1}>{errorMessage}</Typography>
        </>
      )}
    </Box>
  );
};
