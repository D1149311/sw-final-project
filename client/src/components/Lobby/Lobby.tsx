import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { GameContext } from "../GameContext/GameContext";
import "./Lobby.css";

function Lobby() {
  const [top10, setTop10] = useState<{ name: string, point: string }[]>([]);
  const [name, setName] = useState<string>("");
  const [point, setPoint] = useState<string>("0");
  const gameContext = useContext(GameContext);
  const navigate = useNavigate();

  const handlePlay = (type: string) => {
    gameContext!.chessSocket!.send(`play ${type}`);
  };

  const handleLogout = () => {
    gameContext!.chessSocket!.send(`logout`);
  };

  useEffect(() => {
    if (!gameContext || !gameContext.chessSocket) return;

    gameContext.chessSocket.send("info");
    gameContext.chessSocket.send("top10");
  }, [gameContext]);

  useEffect(() => {
    if (!gameContext || !gameContext.chessSocket) return;
    gameContext.chessSocket.onmessage = (event) => {
      console.log('Message from server:', event.data);
      const index = (event.data as string).indexOf(' ');
      const [cmd, message] = [(event.data as string).slice(0, index), (event.data as string).slice(index + 1)]

      if (cmd === "play") {
        if (message === "success") {
          navigate("/game");
        } else {
          console.warn(message);
          alert(message);
        }
      } else if (cmd === "top10") {
        const msg = message.trimEnd().split(' ');
        const top10 = [];
        for (let i = 0; i < msg.length; i += 2) {
          top10.push({ name: msg[i], point: msg[i + 1] });
        }
        setTop10(top10);
      } else if (cmd === "info") {
        const msg = message.trimEnd().split(' ');
        setName(msg[0]);
        setPoint(msg[1]);
      } else if (cmd === "logout") {
        if (message === "success") {
          navigate("/login");
        } else {
          console.warn(message);
          alert(message);
        }
      }
    };

    gameContext.chessSocket.onerror = (error) => {
      console.error('WebSocket error:', error);
    };
  }, [gameContext, navigate]);

  return (
    <div className="lobby">
      <div className="header">
        <h1>西洋棋Online</h1>
        <div className="right">
          <div id="name">歡迎，{name}</div>
          <div id="point">
            <svg height="21" viewBox="0 0 21 21" width="21" xmlns="http://www.w3.org/2000/svg"><g fill="none" fill-rule="evenodd" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" transform="translate(1 3)"><path d="m17.5 8.5v3c0 1.2994935-3.1340068 3-7 3-3.86599325 0-7-1.7005065-7-3 0-.4275221 0-1.2608554 0-2.5" /><path d="m3.79385803 9.25873308c.86480173 1.14823502 3.53999333 2.22489962 6.70614197 2.22489962 3.8659932 0 7-1.60524016 7-2.98595204 0-.77476061-.9867994-1.62391104-2.5360034-2.22001882" /><path d="m14.5 3.5v3c0 1.29949353-3.1340068 3-7 3-3.86599325 0-7-1.70050647-7-3 0-.64128315 0-2.35871685 0-3" /><path d="m7.5 6.48363266c3.8659932 0 7-1.60524012 7-2.985952 0-1.38071187-3.1340068-2.99768066-7-2.99768066-3.86599325 0-7 1.61696879-7 2.99768066 0 1.38071188 3.13400675 2.985952 7 2.985952z" /></g></svg>
            {point}
          </div>
          <button id="logout" onClick={handleLogout}>登出</button>
        </div>
      </div>
      <div className="action">
        <button id="friend" onClick={() => handlePlay("friend")}>跟好友玩</button>
        <button id="online" onClick={() => handlePlay("online")}>線上配對</button>
      </div>
      <div className="rank">
        <ol>
          {
            top10.map((value, index) => (
              <li>
                <div className="left">
                  <div className="number">{index + 1}</div>
                  <svg height="21" viewBox="0 0 21 21" width="21" xmlns="http://www.w3.org/2000/svg"><g fill="none" fill-rule="evenodd" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" transform="translate(2 2)"><circle cx="8.5" cy="8.5" r="8" /><path d="m14.5 13.5c-.6615287-2.2735217-3.1995581-3.0251263-6-3.0251263-2.72749327 0-5.27073171.8688092-6 3.0251263" /><path d="m8.5 2.5c1.6568542 0 3 1.34314575 3 3v2c0 1.65685425-1.3431458 3-3 3-1.65685425 0-3-1.34314575-3-3v-2c0-1.65685425 1.34314575-3 3-3z" /></g></svg>
                  <div className="name">{value.name}</div>
                </div>
                <div className="point">{value.point}</div>
              </li>
            ))
          }
        </ol>
      </div>
    </div>
  )
}

export default Lobby;
