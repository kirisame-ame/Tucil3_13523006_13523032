import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useRef } from "react";
import Combobox from "./ComboBox";
import { useAppContext } from "@/hooks/AppProvider";

interface FileUploaderProps {
    onSolutionFound?: (
        data: {
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
        } | null,
    ) => void;
}

export default function FileUploader({ onSolutionFound }: FileUploaderProps) {
    const fileRef = useRef<HTMLInputElement>(null);
    const appContext = useAppContext();

    const handleUpload = async () => {
        const file = fileRef.current?.files?.[0];
        if (!file) {
            onSolutionFound?.(null);
            return;
        }
        if (!file.name.toLowerCase().endsWith(".txt")) {
            onSolutionFound?.(null);
            return;
        }
        const formData = new FormData();
        formData.append("file", file);
        let url = "/api/solve";
        if (appContext.state.algorithm) {
            url += `?algo=${appContext.state.algorithm}`;
        }
        if (appContext.state.heuristic) {
            url += `${url.includes("?") ? "&" : "?"}heur=${appContext.state.heuristic}`;
        }

        try {
            const res = await fetch(url, {
                method: "POST",
                body: formData,
            });
            const data = await res.json();
            if (res.ok) {
                onSolutionFound?.(data);
            } else {
                onSolutionFound?.(null);
            }
        } catch (err) {
            onSolutionFound?.(null);
        }
    };

    return (
        <div className="grid w-full max-w-sm items-center gap-1.5">
            <Label className="text-amber-400" htmlFor="picture">
                Puzzle txt file
            </Label>
            <Input id="picture" type="file" ref={fileRef} />
            <div className="flex flex-row gap-x-2 justify-center">
                <Combobox
                    param="algorithm"
                    options={
                        new Map([
                            ["A*", "astar"],
                            ["GBFS", "gbfs"],
                            ["UCS", "ucs"],
                        ])
                    }
                ></Combobox>
                <div
                    className={` transition duration-750
                        ${appContext.state.algorithm === "ucs" ? "pointer-events-none opacity-50" : ""}`}
                >
                    <Combobox
                        param="heuristic"
                        options={
                            new Map([
                                ["Blocking Pieces", "blocking"],
                                ["Distance to Goal", "distance"],
                                ["Combined Heuristic", "blocking_distance"],
                            ])
                        }
                    ></Combobox>
                </div>
            </div>
            <button
                className="bg-black text-amber-400 text-lg mt-2 transition hover:bg-amber-400 hover:text-black p-2 rounded-md duration-200"
                type="button"
                onClick={handleUpload}
            >
                Upload & Solve
            </button>
        </div>
    );
}
