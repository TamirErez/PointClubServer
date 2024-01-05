package pointclub.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pointclub.entity.chess.ChessGame;
import pointclub.entity.User;
import pointclub.entity.chess.ChessRoom;
import pointclub.entity.chess.MoveHistory;
import pointclub.repository.ChessGameRepository;
import pointclub.repository.ChessRoomRepository;
import pointclub.repository.MoveHistoryRepository;

@RestController
@RequestMapping("chess")
public class ChessController {

    @Autowired
    ChessGameRepository chessGameRepository;
    @Autowired
    ChessRoomRepository chessRoomRepository;
    @Autowired
    MoveHistoryRepository moveHistoryRepository;

    @PostMapping("create")
    public int create() {
        ChessGame chessGame = new ChessGame();
        ChessRoom chessRoom = new ChessRoom();
        chessRoom.setChessGame(chessGame);
        int chessRoomId = chessRoomRepository.save(chessRoom).getId();
        chessGameRepository.save(chessGame);

        return chessRoomId;
    }

    @PostMapping("join")
    public void join(@RequestBody ChessUser chessUser) {
        ChessRoom room = chessRoomRepository.getById(chessUser.chessRoomId);
        if(room.getWhite() != null){
            room.setBlack(chessUser.user);
        } else if (room.getBlack() != null) {
            room.setWhite(chessUser.user);
        }
        else {
            setChessFirstUser(room, chessUser.user);
        }
        chessRoomRepository.save(room);
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


