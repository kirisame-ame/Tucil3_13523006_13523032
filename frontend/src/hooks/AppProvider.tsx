import { createContext, useState, useContext } from "react";
import type { ReactNode } from "react";

interface AppState {
    algorithm: string;
    heuristic: string;
}
type AppContextType = {
    state: AppState;
    setState: React.Dispatch<React.SetStateAction<AppState>>;
};
const AppContext = createContext<AppContextType | undefined>(undefined);

export function AppProvider({ children }: { children: ReactNode }) {
    const [globalState, setGlobalState] = useState<AppState>({
        algorithm: "",
        heuristic: "",
    });

    return (
        <AppContext.Provider
            value={{ state: globalState, setState: setGlobalState }}
        >
            {children}
        </AppContext.Provider>
    );
}
export function useAppContext() {
    const context = useContext(AppContext);
    if (!context) {
        throw new Error("useAppContext must be used within an AppProvider");
    }
    return context;
}
