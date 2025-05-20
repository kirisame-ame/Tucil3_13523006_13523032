import React from "react";

interface BoardGridProps {
    board: string;
}

const BoardGrid: React.FC<BoardGridProps> = ({ board }) => {
    // Convert board string into 2D array
    const rows = board.trim().split("\n");
    const gridSize = rows.length;

    // Map characters to colors
    const getBackgroundColor = (cell: string) => {
        if (cell === "P") return "bg-red-500"; // Primary piece
        if (cell === "K") return "bg-yellow-500"; // Target
        if (cell === "." || cell === " ") return "bg-gray-800"; // Empty space
        if (cell === "©") return "bg-gray-900"; // Wall
        return "bg-blue-500"; // Other pieces
    };

    return (
        <div
            className="grid gap-1 p-2 bg-gray-900 rounded-lg"
            style={{
                gridTemplateColumns: `repeat(${gridSize}, 1fr)`,
                width: "fit-content",
            }}
        >
            {rows.map((row, i) =>
                row.split("").map((cell, j) => (
                    <div
                        key={`${i}-${j}`}
                        className={`w-8 h-8 ${getBackgroundColor(cell)} rounded transition-colors duration-200`}
                        title={
                            cell !== "." && cell !== " " && cell !== "©"
                                ? `Piece ${cell}`
                                : "Empty"
                        }
                    >
                        {cell !== "." && cell !== " " && cell !== "©" && (
                            <span className="flex items-center justify-center h-full text-white font-bold">
                                {cell}
                            </span>
                        )}
                    </div>
                )),
            )}
        </div>
    );
};

export default BoardGrid;
