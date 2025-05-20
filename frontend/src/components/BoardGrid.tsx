import React from "react";

interface BoardGridProps {
    board: string;
}

const BoardGrid: React.FC<BoardGridProps> = ({ board }) => {
    // Convert board string into 2D array
    const rows = board.trim().split("\n");
    const numRows = rows.length;
    const numCols = rows[0].length;

    // Map characters to colors
    const getBackgroundColor = (cell: string) => {
        if (
            cell === "P" ||
            cell === "K" ||
            cell === "." ||
            cell === " " ||
            cell === "©"
        )
            return undefined;
        var charCode = cell.charCodeAt(0) - 65;
        var red = ((charCode + 1) * 50) % 256;
        var green = ((charCode + 1) * 230) % 256;
        var blue = ((charCode + 1) * 120) % 256;
        return { backgroundColor: `rgb(${red},${green},${blue})` };
    };

    return (
        <div
            className="flex justify-center items-center w-full max-w-[400px]"
            style={{
                aspectRatio: `${numCols} / ${numRows}`,
            }}
        >
            <div
                className="grid gap-0.5 p-2 bg-gray-900 rounded-lg w-full h-full"
                style={{
                    gridTemplateColumns: `repeat(${numCols}, 1fr)`,
                    gridTemplateRows: `repeat(${numRows}, 1fr)`,
                }}
            >
                {rows.map((row, i) =>
                    row.split("").map((cell, j) => {
                        const isSpecial =
                            cell === "P" ||
                            cell === "K" ||
                            cell === "." ||
                            cell === " " ||
                            cell === "©";
                        let className =
                            "aspect-square flex items-center justify-center rounded transition-colors duration-200 ";
                        if (cell === "P") className += "bg-red-500";
                        else if (cell === "K") className += "bg-yellow-500";
                        else if (cell === "." || cell === " ")
                            className += "bg-gray-800";
                        else if (cell === "©") className += "bg-gray-900";
                        return (
                            <div
                                key={`${i}-${j}`}
                                className={className}
                                style={
                                    !isSpecial
                                        ? getBackgroundColor(cell)
                                        : undefined
                                }
                                title={
                                    cell !== "." &&
                                    cell !== " " &&
                                    cell !== "©"
                                        ? `Piece ${cell}`
                                        : "Empty"
                                }
                            >
                                {cell !== "." &&
                                    cell !== " " &&
                                    cell !== "©" && (
                                        <span className="flex items-center justify-center h-full text-white font-bold text-sm">
                                            {cell}
                                        </span>
                                    )}
                            </div>
                        );
                    }),
                )}
            </div>
        </div>
    );
};

export default BoardGrid;
