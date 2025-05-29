import { useState } from "react";
import StepOne from "./StepOne";
import StepTwo from "./StepTwo";
import StepThree from "./StepThree";

export const StepperForm: React.FC = () => {
	const [step, setStep] = useState<number>(1);

	const next = () => setStep((prev) => prev + 1);
	const back = () => setStep((prev) => prev - 1);

	const steps = [
		{ id: 1, label: "Título e Descrição" },
		{ id: 2, label: "Calendário" },
		{ id: 3, label: "Observações" },
	];

	return (
		<div className="flex items-center justify-center my-6">
			<div className="p-8 rounded-xl w-full lg:max-w-lg">
				<div className="flex justify-between mb-6">
					{steps.map(({ id, label }) => (
						<div key={id} className="flex items-center space-x-2">
							<div
								className={`w-8 h-8 flex items-center justify-center rounded text-white text-sm font-bold ${
									step === id ? "bg-red-600" : "bg-gray-300"
								}`}
							>
								{id}
							</div>
							<span className={"text-sm"}>{label}</span>
						</div>
					))}
				</div>

				{step === 1 && <StepOne onNext={next} />}
				{step === 2 && <StepTwo onNext={next} onBack={back} />}
				{step === 3 && <StepThree onBack={back} />}
			</div>
		</div>
	);
};