package pointclub.exception;

public class ChessGameFullException extends RuntimeException {
    public ChessGameFullException() {
        super("The current chess game is full");
    }
}
