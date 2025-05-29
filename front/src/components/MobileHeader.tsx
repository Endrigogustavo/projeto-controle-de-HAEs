import MenuIcon from "@mui/icons-material/Menu";
import IconButton from "@mui/material/IconButton";

type MobileHeaderProps = {
	onMenuClick: () => void;
};

export const MobileHeader = ({ onMenuClick }: MobileHeaderProps) => {
	return (
		<header className="md:hidden fixed top-0 left-0 w-full bg-white shadow z-50 flex justify-between items-center px-4 h-16">
			<IconButton onClick={onMenuClick} aria-label="Abrir menu">
				<MenuIcon className="text-gray-800" />
			</IconButton>

			<img
				src="/fatec_zona_leste_icon_branco.png"
				alt="Logo da Fatec da Zona Leste"
				className="w-10 h-10"
			/>
		</header>
	);
};
