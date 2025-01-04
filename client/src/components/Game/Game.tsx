import { useContext, useEffect, useState } from "react";
import { GameContext } from "../GameContext/GameContext";
import "./Game.css";

// 匯入棋子圖片
import bRookImg from "../../assets/br.png";
import bPawnImg from "../../assets/bp.png";
import bKnightImg from "../../assets/bn.png";
import bBishopImg from "../../assets/bb.png";
import bQueenImg from "../../assets/bq.png";
import bKingImg from "../../assets/bk.png";
import wRookImg from "../../assets/wr.png";
import wPawnImg from "../../assets/wp.png";
import wKnightImg from "../../assets/wn.png";
import wBishopImg from "../../assets/wb.png";
import wQueenImg from "../../assets/wq.png";
import wKingImg from "../../assets/wk.png";
import { useNavigate } from "react-router-dom";

// 建立一個對應表來管理棋子名稱與圖片
const pieceImages: Record<string, string> = {
  br: bRookImg,
  bp: bPawnImg,
  bn: bKnightImg,
  bb: bBishopImg,
  bq: bQueenImg,
  bk: bKingImg,
  wr: wRookImg,
  wp: wPawnImg,
  wn: wKnightImg,
  wb: wBishopImg,
  wq: wQueenImg,
  wk: wKingImg,
};

// 定義棋格的類型
interface Square {
  piece: string | null;
  predicted: boolean;
  eatable: boolean;
}

function Game() {
  const [start, setStart] = useState<boolean>(false);
  const [player1, setPlayer1] = useState<string>("");
  const [player2, setPlayer2] = useState<string>("");
  const [lastPosition, setLastPosition] = useState<{ x: number, y: number } | null>();
  const [threat, setThreat] = useState<boolean | null>(null);
  const [promote, setPromote] = useState<boolean>(false);
  const gameContext = useContext(GameContext);
  const [board, setBoard] = useState<Square[][]>(initializeBoard());
  const navigate = useNavigate();

  // 初始化棋盤
  function initializeBoard(): Square[][] {
    const newBoard: Square[][] = [];
    for (let row = 0; row < 8; row++) {
      const squares: Square[] = [];
      for (let col = 0; col < 8; col++) {
        squares.push({ piece: null, predicted: false, eatable: false });
      }
      newBoard.push(squares);
    }

    return newBoard;
  }

  const handleClickChess = (colIndex: number, rowIndex: number) => {
    if (!promote && board[rowIndex][colIndex].predicted === true) {
      gameContext!.chessSocket!.send(`move ${lastPosition!.x} ${lastPosition!.y} ${colIndex} ${rowIndex}`);
      setLastPosition(null);
      setBoard((prevBoard) => {
        const newBoard = [...prevBoard];

        for (let row = 0; row < 7; row++) {
          for (let col = 0; col < 7; col++) {
            newBoard[row][col].predicted = false;
            newBoard[row][col].eatable = false;
          }
        }

        return newBoard;
      });
    } else {
      gameContext!.chessSocket!.send(`possible ${colIndex} ${rowIndex}`);
      setLastPosition({ x: colIndex, y: rowIndex });
    }
  }

  const handlePromote = (type: string) => {
    if (promote) {
      gameContext!.chessSocket!.send(`promote ${type}`);
    }
  }

  useEffect(() => {
    if (!gameContext || !gameContext.chessSocket) return;
    gameContext.chessSocket.onmessage = (event) => {
      console.log('Message from server:', event.data);
      let index = (event.data as string).indexOf(' ');
      if (index < 0) {
        index = (event.data as string).length;
      }
      const [cmd, message] = [(event.data as string).slice(0, index), (event.data as string).slice(index + 1).trim()]
      
      if (cmd === "start") {
        setStart(true);
        setPlayer1(message.split(" ")[0]);
        setPlayer2(message.split(" ")[1]);
      } else if (cmd === "move") {
        if (message === "invalid") {
          return;
        }

        const updateBoard = message.split(" ");

        // console.log(updateBoard);
        setPromote(false);

        setBoard((prevBoard) => {
          const newBoard = [...prevBoard];
          for (let row = 0; row < 8; row++) {
            for (let col = 0; col < 8; col++) {
              const index = (row * 8 + col) * 2;
              let piece = "";
              if (updateBoard[index] === "WHITE") {
                piece += "w";
              } else if (updateBoard[index] === "BLACK") {
                piece += "b";
              }

              if (updateBoard[index + 1] === "PAWN") {
                piece += "p";
              } else if (updateBoard[index + 1] === "KING") {
                piece += "k";
              } else if (updateBoard[index + 1] === "KNIGHT") {
                piece += "n";
              } else if (updateBoard[index + 1] === "QUEEN") {
                piece += "q";
              } else if (updateBoard[index + 1] === "BISHOP") {
                piece += "b";
              } else if (updateBoard[index + 1] === "ROOK") {
                piece += "r";
              }

              newBoard[row][col].piece = piece;
              newBoard[row][col].predicted = false;
              newBoard[row][col].eatable = false;
            }
          }
          console.log(newBoard);

          return newBoard;
        });
        setThreat(null);
      } else if (cmd === "possible") {
        setBoard((prevBoard) => {
          const newBoard = [...prevBoard];

          for (let row = 0; row < 8; row++) {
            for (let col = 0; col < 8; col++) {
              newBoard[row][col].predicted = false;
              newBoard[row][col].eatable = false;
            }
          }

          const predictions = message.split(" ");
          if (predictions.length < 3) {
            return newBoard;
          }

          for (let i = 0; i < predictions.length; i += 3) {
            newBoard[parseInt(predictions[i + 1])][parseInt(predictions[i])].predicted = true;
            newBoard[parseInt(predictions[i + 1])][parseInt(predictions[i])].eatable = (predictions[i + 2] == "true");
          }

          return newBoard;
        });
      } else if (cmd === "end") {
        alert(message);
        navigate("/");
      } else if (cmd === "threat") {
        if (message === "WHITE") {
          setThreat(false);
        }

        if (message === "BLACK") {
          setThreat(true);
        }
      } else if (cmd === "promote") {
        setPromote(true);
      }
    };
  });

  return (
    start ? (
      <div className="game">
        {promote ? (
          <div className="promote">
            <div className="dialog">
              <h3>升變</h3>
              <p>請選擇一種棋子</p>
              <div className="choices">
                <div className="choice" onClick={() => handlePromote("ROOK")}>
                  <img src={pieceImages["br"]} alt="ROOK" />
                </div>
                <div className="choice" onClick={() => handlePromote("KNIGHT")}>
                  <img src={pieceImages["bn"]} alt="KNIGHT" />
                </div>
                <div className="choice" onClick={() => handlePromote("BISHOP")}>
                  <img src={pieceImages["bb"]} alt="BISHOP" />
                </div>
                <div className="choice" onClick={() => handlePromote("QUEEN")}>
                  <img src={pieceImages["bq"]} alt="QUEEN" />
                </div>
              </div>
            </div>
          </div>) : <></>}
        <h3>白 {player1} vs 黑 {player2}</h3>
        <div className="board">
          {board.map((row, rowIndex) => (
            <div key={rowIndex} className="row">
              {row.map((square, colIndex) => (
                <div
                  id={`${rowIndex}-${colIndex}`}
                  key={`${rowIndex}-${colIndex}`}
                  className={`square ${(rowIndex + colIndex) % 2 === 0 ? "white" : "black"} ${square.predicted || square.piece && ((threat === true && square.piece === "bk") || (threat === false && square.piece === "wk")) ? "predicted" : ""} ${(square.eatable || square.piece && (threat === true && square.piece === "bk") || (threat === false && square.piece === "wk")) ? "eatable" : ""}`}
                  onClick={() => handleClickChess(colIndex, rowIndex)}
                >
                  {square.piece && (
                    <img
                      src={pieceImages[square.piece]}
                      alt={square.piece}
                      className="chess-piece"
                    />
                  )}
                </div>
              ))}
            </div>
          ))}
        </div>
      </div>
    ) : (
      <div className="waiting">等待中...</div>
    )
  )
}

export default Game;
