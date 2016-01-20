package restopoly.DTO;

import restopoly.resources.Board;
import restopoly.resources.Event;
import restopoly.resources.Player;

/**
 * Created by final-work on 15.01.16.
 */
public class PlayerBoardDTO {

    Player player;
    Board board;
    Event[] events;

    public PlayerBoardDTO(Player player, Board board, Event[] events){
        this.player = player;
        this.board = board;
        this.events = events;
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

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
