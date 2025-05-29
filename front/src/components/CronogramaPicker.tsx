import React from "react";
import { DayPicker } from "react-day-picker";
import "react-day-picker/dist/style.css";
import { format, parseISO } from "date-fns";

interface CronogramaPickerProps {
	selectedDates: string[];
	onSelect: (dates: string[]) => void;
	errorMessage?: string;
}

const CronogramaPicker: React.FC<CronogramaPickerProps> = ({
	selectedDates,
	onSelect,
	errorMessage,
}) => {
	const initialSelectedDates = selectedDates.map((dateStr) =>
		parseISO(dateStr)
	);

	const handleDaySelect = (selectedDays: Date[] | undefined) => {
		if (!selectedDays || selectedDays.length === 0) {
			onSelect([]);
			return;
		}

		const newSelectedDates = selectedDays.map((day) =>
			format(day, "yyyy-MM-dd")
		);
		onSelect(newSelectedDates);
	};

	return (
		<div className="relative">
			<DayPicker
				mode="multiple"
				selected={initialSelectedDates}
				onSelect={handleDaySelect}
			/>
			{errorMessage && (
				<p className="text-red-600 text-xs mt-1 ml-4 absolute bottom-[-20px] left-0">
					{errorMessage}
				</p>
			)}
		</div>
	);
};

export default CronogramaPicker;
