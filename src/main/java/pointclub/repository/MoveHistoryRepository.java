package pointclub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pointclub.entity.chess.MoveHistory;

public interface MoveHistoryRepository extends JpaRepository<MoveHistory, Integer> {
}