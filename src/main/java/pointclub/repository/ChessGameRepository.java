package pointclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pointclub.entity.chess.ChessGame;

public interface ChessGameRepository extends JpaRepository<ChessGame, Integer> {
}