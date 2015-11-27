package restopoly.resources;

import java.util.ArrayList;

/**
 * Created by Krystian.Graczyk on 27.11.15.
 */
public class Boards {
    private ArrayList<Board> boards = new ArrayList<Board>();

    public Boards(){}

    public ArrayList<Board> getBoards() {
        return boards;
    }

    public Board getBoard(String gameid) {
        for(Board board : boards){
            if(board.getGameid().equals(gameid)) return board;
        }
        return null;
    }
    public void addBoard(Board board){
        boards.add(board);
    }
}
