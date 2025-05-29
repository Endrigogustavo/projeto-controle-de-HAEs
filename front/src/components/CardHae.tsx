type CardHaeProps = {
	titulo: string;
	curso: string;
	status: string;
	descricao: string;
};

export const CardHae = ({ titulo, curso, status, descricao }: CardHaeProps) => {
	return (
		<div className="relative w-full my-4 p-6 bg-white backdrop-blur-md rounded-xl shadow-md">
			{/* Status mobile: centralizado no topo */}
			<div className="block md:hidden mb-4">
				<span className="text-sm bg-gray-200 text-gray-600 px-4 py-1 rounded-full shadow-sm inline-block">
					{status}
				</span>
			</div>

			{/* Status desktop: topo direito absoluto */}
			<div className="hidden md:flex absolute top-4 right-4">
				<span className="text-sm bg-gray-200 text-gray-600 px-4 py-1 rounded-full shadow-sm">
					{status}
				</span>
			</div>

			<h2 className="text-xl font-semibold text-gray-800">{titulo}</h2>

			<p className="text-sm font-medium text-gray-600 mb-2">{curso}</p>

			<p className="text-sm text-gray-700 leading-relaxed">
				Descrição: {descricao}
			</p>
		</div>
	);
};
