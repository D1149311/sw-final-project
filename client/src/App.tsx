import { Route, Routes } from "react-router-dom";
import LoginForm from "./components/LoginForm/LoginForm";
import GameContextProvider from "./components/GameContext/GameContext";
import ProtectedRoutes from "./components/ProtectedRoutes/ProtectedRoutes";
import SignupForm from "./components/SignupForm/SignupForm";
import Lobby from "./components/Lobby/Lobby";
import Game from "./components/Game/Game";
import "./App.css";

function App() {
  return (
    <GameContextProvider>
      <Routes>
        <Route path="login" element={<LoginForm />} />
        <Route path="signup" element={<SignupForm />} />
        <Route element={<ProtectedRoutes />}>
          <Route path="/" index element={<Lobby />} />
          <Route path="/game" index element={<Game />} />
        </Route>
      </Routes>
    </GameContextProvider>
  );
}

export default App;
