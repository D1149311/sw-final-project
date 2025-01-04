import { useContext } from "react";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import { GameContext } from "../GameContext/GameContext";

function ProtectedRoutes() {
    const location = useLocation();
    const gameContext = useContext(GameContext);

    return gameContext?.auth ? (
        <Outlet />
    ) : (
        <Navigate to="/login" replace state={{ from: location }} />
    );
}

export default ProtectedRoutes;
