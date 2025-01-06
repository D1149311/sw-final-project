import { useContext, useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { GameContext } from "../GameContext/GameContext";
import "./LoginForm.css";

function LoginForm() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState("");
    const gameContext = useContext(GameContext);
    const navigate = useNavigate();

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        setIsLoading(true);

        try {
            if (gameContext?.chessSocket && gameContext.chessSocket.readyState === WebSocket.OPEN) {
                gameContext.chessSocket.send(`login ${username} ${password}`);
            }
        } catch (e) {
            setError((e as Error).message);
        }
    };

    useEffect(() => {
        if (!gameContext || !gameContext.chessSocket) return;
        gameContext.chessSocket.onmessage = (event) => {
            console.log('Message from server:', event.data);
            const index = (event.data as string).indexOf(' ');
            const [cmd, message] = [(event.data as string).slice(0, index), (event.data as string).slice(index + 1)]

            if (cmd === "login") {
                setIsLoading(false);

                if (message === "success") {
                    gameContext.setAuth(true);
                    navigate("/", {replace: true});
                } else {
                    console.warn(message);
                    setError(message);
                }
            }
        };

        gameContext.chessSocket.onerror = (error) => {
            console.error('WebSocket error:', error);
        };
    }, [gameContext, navigate]);

    return (
        <div className="login">
            <form className="form" onSubmit={handleSubmit}>
                <h1 className="title">
                    西洋棋Online
                </h1>
                <div className="error">{error}</div>
                <div className="form-field">
                    <input
                        id="username"
                        type="text"
                        name="username"
                        placeholder=" "
                        required
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <label htmlFor="username">名稱</label>
                </div>
                <div className="form-field">
                    <input
                        id="password"
                        type="password"
                        name="password"
                        placeholder=" "
                        required
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <label htmlFor="password">密碼</label>
                </div>
                <button id="login" type="submit">
                    {isLoading ? <span className="loading-spinner"></span> : "登入"}
                </button>
                <span className="hint">
                    還沒有帳號嗎? <Link to="/signup">前往註冊</Link>
                </span>
            </form>
        </div>
    );
}

export default LoginForm;
