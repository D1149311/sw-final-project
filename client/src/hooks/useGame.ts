import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { GameContext } from "../components/GameContext/GameContext";

function useGame() {
  const gameContext = useContext(GameContext);

  const navigate = useNavigate();
  if (!gameContext || gameContext.auth) {
    navigate("/login", {replace: true});
    return;
  }

  return gameContext;
}

export default useGame;
