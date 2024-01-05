package pointclub.entity.chess;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pointclub.entity.chat.ChatRoom;
import pointclub.entity.Room;
import pointclub.entity.User;

import javax.persistence.*;

@Entity
@Table(name = "chess_rooms")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ChessRoom extends Room {
    @ManyToOne
    @JoinColumn(name = "white_id")
    private User white;

    @ManyToOne
    @JoinColumn(name = "black_id")
    private User black;

    @OneToOne
    private ChessGame chessGame;

    @OneToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;
}
