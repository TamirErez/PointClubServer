package pointclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pointclub.entity.chess.ChessGame;
import pointclub.entity.chess.ChessRoom;

public interface ChessRoomRepository extends JpaRepository<ChessRoom, Integer> {
    ChessRoom getByChessGame(ChessGame chessGame);
}