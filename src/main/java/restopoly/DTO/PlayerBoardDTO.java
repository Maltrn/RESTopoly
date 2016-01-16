package restopoly.DTO;

import restopoly.resources.Board;
import restopoly.resources.Player;

/**
 * Created by final-work on 15.01.16.
 */
public class PlayerBoardDTO {

    Player player;
    Board board;

    public PlayerBoardDTO(Player player, Board board){
        this.player = player;
        this.board = board;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
