import { useState } from "react";
import { HaeDataType } from "@/components/StepperForm/types/haeFormTypes";
export interface IAuthService {
	createHae(data: HaeDataType): Promise<any>;
}

export const useHaeService = (haeService: IAuthService) => {
	const [openSnackbar, setOpenSnackbar] = useState(false);
	const [snackbarMessage, setSnackbarMessage] = useState("");
	const [snackbarSeverity, setSnackbarSeverity] = useState<
		"error" | "success" | "info" | "warning"
	>("info");

	const handleCloseSnackbar = () => {
		setOpenSnackbar(false);
	};

	const handleCreateHae = async (data: HaeDataType) => {
		try {
			await haeService.createHae(data);
			setSnackbarMessage("HAE criada com sucesso!");
			setSnackbarSeverity("success");
			setOpenSnackbar(true);
			return true;
		} catch (error) {
			setSnackbarMessage("Erro ao criar HAE.");
			setSnackbarSeverity("error");
			setOpenSnackbar(true);
			return false;
		}
	};

	return {
		openSnackbar,
		snackbarMessage,
		snackbarSeverity,
		handleCloseSnackbar,
		handleCreateHae,
	};
};
