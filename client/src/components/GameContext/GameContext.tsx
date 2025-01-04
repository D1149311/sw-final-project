import { createContext, ReactNode, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

interface GameContextType {
    chessSocket: WebSocket | null;
    auth: boolean;
    setAuth: (auth: boolean) => void;
}

export const GameContext = createContext<GameContextType | null>(null);

function GameContextProvider({ children }: { children?: ReactNode }) {
    const [chessSocket, setChessSocket] = useState<WebSocket | null>(null);
    const [auth, setAuth] = useState<boolean>(false);
    const navigate = useNavigate();

    function createSocket() {
        const socket = new WebSocket('ws://localhost:8081');

        socket.onopen = () => {
            console.log('WebSocket connection established');
        };

        socket.onerror = (error) => {
            console.error('WebSocket error:', error);
            alert("Cannot connect to server!");
            navigate("/login", {replace: true});
            socket.close();
        };

        socket.onclose = () => {
            console.error('WebSocket connection closed');
            alert("Cannot connect to server!");
            navigate("/login", {replace: true});
            socket.close();
        };

        return socket;
    }

    useEffect(() => {
        if (chessSocket === null) {
            setChessSocket(createSocket());
        }

        // Cleanup function to close WebSocket when the component is unmounted
        return () => {
            chessSocket?.close();
        };
    }, [chessSocket]);

    const contextValue: GameContextType = {
        chessSocket,
        auth,
        setAuth,
    };

    return <GameContext.Provider value={contextValue}>{children}</GameContext.Provider>;
}

export default GameContextProvider;
