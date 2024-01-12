package pointclub.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pointclub.entity.chat.ChatRoom;
import pointclub.entity.chat.ChatRoomWithUser;
import pointclub.entity.chess.ChessGame;
import pointclub.entity.User;
import pointclub.entity.chess.ChessRoom;
import pointclub.entity.chess.MoveHistory;
import pointclub.exception.ChessGameFullException;
import pointclub.repository.ChessGameRepository;
import pointclub.repository.ChatRoomRepository;
import pointclub.repository.ChessRoomRepository;
import pointclub.repository.MoveHistoryRepository;
import pointclub.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("chess")
public class ChessController {

    @Autowired
    private ChessGameRepository chessGameRepository;
    @Autowired
    private ChessRoomRepository chessRoomRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private MoveHistoryRepository moveHistoryRepository;
    @Autowired
    private ChatService chatService;

    @PostMapping("create")
    public int create() {
        ChessGame chessGame = new ChessGame();
        chessGameRepository.save(chessGame);

        ChatRoom chatRoom = new ChatRoom();
        chatRoomRepository.save(chatRoom);

        ChessRoom chessRoom = new ChessRoom();
        chessRoom.setChessGame(chessGame);
        chessRoom.setChatRoom(chatRoom);

        return chessRoomRepository.save(chessRoom).getId();
    }

    @PostMapping("join")
    public void join(@RequestBody ChessUser chessUser) {
        ChessRoom room = chessRoomRepository.getById(chessUser.chessRoomId);
        if (room.getWhite() != null && room.getBlack() != null){
            throw new ChessGameFullException();
        }
        if(room.getWhite() != null){
            room.setBlack(chessUser.user);
        } else if (room.getBlack() != null) {
            room.setWhite(chessUser.user);
        }
        else {
            setChessFirstUser(room, chessUser.user);
        }

        chatService.addUserToChatRoom(new ChatRoomWithUser(room.getChatRoom().getServerId(), chessUser.getUser().getServerId()));
        chessRoomRepository.save(room);
    }

    @GetMapping()
    public List<ChessRoom> getAllRooms() {
        return chessRoomRepository.findAll();
    }

    @PostMapping("move")
    public void move(@RequestBody ChessMove move) {
        ChessGame chessGame = chessGameRepository.getById(move.chessGameId);
        moveHistoryRepository.save(new MoveHistory(move.move, chessGame, move.currentPlayer));
        if (chessRoomRepository.getByChessGame(chessGame).getBlack().equals(move.currentPlayer)) {
            //TODO: send move to white
        } else {
            //TODO: send move to black
        }
    }

    private void setChessFirstUser(ChessRoom room, User user) {
        room.setWhite(user);
    }

    @Data
    @AllArgsConstructor
    private static class ChessMove {
        private String move;
        private int chessGameId;
        private User currentPlayer;
    }

    @Data
    @AllArgsConstructor
    private static class ChessUser {
        private int chessRoomId;
        private User user;
    }
}


