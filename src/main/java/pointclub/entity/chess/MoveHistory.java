package pointclub.entity.chess;

import lombok.*;
import org.hibernate.Hibernate;
import pointclub.entity.User;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "chess_move_history")
public class MoveHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    private String move;

    @ManyToOne
    @JoinColumn(name = "game")
    private ChessGame chessGame;

    @ManyToOne
    @JoinColumn(name = "current_player")
    private User currentPlayer;

    public MoveHistory(String move, ChessGame chessGame, User currentPlayer) {
        this.move = move;
        this.chessGame = chessGame;
        this.currentPlayer = currentPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MoveHistory that = (MoveHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}