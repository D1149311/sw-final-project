import { useImperativeHandle, forwardRef, useState } from "react";
import "./Board.css";

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
  isWhite: boolean;
  piece: string | null;
}

export interface ChessBoardRef {
  movePiece: (fromX: number, fromY: number, toX: number, toY: number) => void;
}

const Board = forwardRef<ChessBoardRef>((_, ref) => {
  const [board, setBoard] = useState<Square[][]>(initializeBoard());

  // 初始化棋盤
  function initializeBoard(): Square[][] {
    const newBoard: Square[][] = [];
    for (let row = 0; row < 8; row++) {
      const squares: Square[] = [];
      for (let col = 0; col < 8; col++) {
        const isWhite = (row + col) % 2 === 0;
        squares.push({ isWhite, piece: null });
      }
      newBoard.push(squares);
    }

    for (let col = 0; col < 8; col++) {
      newBoard[1][col].piece = "bp";
    }

    for (let col = 0; col < 8; col++) {
      newBoard[6][col].piece = "wp";
    }

    return newBoard;
  }

  // 提供給外部的功能
  useImperativeHandle(ref, () => ({
    movePiece: (fromX: number, fromY: number, toX: number, toY: number) => {
      setBoard((prevBoard) => {
        const newBoard = [...prevBoard];
        newBoard[toY][toX].piece = newBoard[fromY][fromX].piece;
        newBoard[fromY][fromX].piece = null;
        return newBoard;
      });
    }
  }));

  return (
    <div className="board">
      {board.map((row, rowIndex) => (
        <div key={rowIndex} className="row">
          {row.map((square, colIndex) => (
            <div
              id={`${rowIndex}-${colIndex}`}
              key={`${rowIndex}-${colIndex}`}
              className={`square ${square.isWhite ? "white" : "black"} ${rowIndex == 0 ? "predicted" : ""}`}
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
  )
});

export default Board;
