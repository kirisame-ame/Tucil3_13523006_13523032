import "./App.css";
import { useState } from "react";
import FileUploader from "./components/FileUploader";
import PathCarousel from "./components/carousel/PathCarousel";

function App() {
    const [solutionData, setSolutionData] = useState<{
        algorithm: string;
        heuristic: string;
        executionTime: number;
        visitedNodes: number;
        path: Array<{
            board: string;
            movement?: {
                piece: string;
                distance: number;
                direction: string;
            };
        }>;
    } | null>(null);
    const [error, setError] = useState<string | null>(null);

    const handleSolutionFound = (
        data: typeof solutionData,
        err: string | null,
    ) => {
        setSolutionData(data);
        setError(err);
    };

    return (
        <div className="flex flex-col bg-black min-h-screen items-center">
            <div className="my-5 text-amber-400 items-center text-center">
                <h1 className="text-3xl font-bold">Rush Hour Puzzle Solver</h1>
                <h1 className="text-amber-400">Gurt: Yo</h1>
            </div>
            <div className="flex min-w-full justify-center items-center">
                <FileUploader onSolutionFound={handleSolutionFound} />
            </div>
            {error && (
                <div className="flex text-red-500 items-center text-center mt-4">
                    <p>{error}</p>
                </div>
            )}
            {solutionData && (
                <div className="text-white flex flex-col items-center ">
                    <div>
                        <p>
                            Found solution with {solutionData.path.length} steps
                        </p>
                        <p>Time: {solutionData.executionTime}ms</p>
                        <p>Nodes visited: {solutionData.visitedNodes}</p>
                    </div>

                    <PathCarousel slides={solutionData.path}></PathCarousel>
                </div>
            )}
        </div>
    );
}

export default App;
