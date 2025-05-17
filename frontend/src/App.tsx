import "./App.css";
import { useState } from "react";
import FileUploader from "./components/FileUploader";

function App() {
    const [message, setMessage] = useState("");
    return (
        <div className="flex flex-col items-center justify-center h-screen bg-gray-900">
            <h1 className="text-amber-400">Gurt: Yo</h1>

            <p className="text-amber-400">{message}</p>
            <FileUploader onUpload={setMessage} />
        </div>
    );
}

export default App;
