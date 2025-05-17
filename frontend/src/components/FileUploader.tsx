import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useRef } from "react";

export default function FileUploader({
    onUpload,
}: {
    onUpload?: (msg: string) => void;
}) {
    const fileRef = useRef<HTMLInputElement>(null);

    const handleUpload = async () => {
        const file = fileRef.current?.files?.[0];
        if (!file) {
            onUpload?.("No file selected");
            return;
        }
        if (!file.name.toLowerCase().endsWith(".txt")) {
            onUpload?.("Only .txt files are allowed");
            return;
        }
        const formData = new FormData();
        formData.append("file", file);
        try {
            const res = await fetch("/api/parse", {
                method: "POST",
                body: formData,
            });
            const text = await res.text();
            onUpload?.(text);
        } catch (err) {
            onUpload?.("Upload failed");
        }
    };

    return (
        <div className="grid text-white w-full max-w-sm items-center gap-1.5">
            <Label htmlFor="picture">Puzzle txt file</Label>
            <Input id="picture" type="file" ref={fileRef} />
            <button
                className="bg-white text-black mt-2 transition hover:bg-amber-400 p-2 rounded-md duration-200"
                type="button"
                onClick={handleUpload}
            >
                Upload & Parse
            </button>
        </div>
    );
}
