package pointclub.entity.chess;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "chess_game")
public class ChessGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToMany(mappedBy = "chessGame")
    private List<MoveHistory> moveHistoryList;

    private String result;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(id, chessGame.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}