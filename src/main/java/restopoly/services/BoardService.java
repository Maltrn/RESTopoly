package restopoly.services;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.util.Service;
import restopoly.resources.*;

import java.util.ArrayList;

import static spark.Spark.*;

/**
 * Created by Krystian.Graczyk on 27.11.15.
 */
public class BoardService {

    public static void main(String[] args) {

        ArrayList<Board> boards = new ArrayList<Board>();

        get("/boards", (req, res) -> {
            res.status(200);
            res.header("Content-Type", "application/json");
            return new Gson().toJson(boards);
        });

        put("/boards/:gameid", (req, res) -> {
            Board board = new Board(req.params(":gameid"));
            ArrayList<Field> fields = new ArrayList<Field>();
            fields.add(new Field(new Place("0"), new ArrayList<Player>()));
            fields.add(new Field(new Place("1"), new ArrayList<Player>()));
            fields.add(new Field(new Place("2"), new ArrayList<Player>()));
            fields.add(new Field(new Place("3"), new ArrayList<Player>()));

            board.setFields(fields);
            boards.add(board);
            return "";
        });

        put("/boards/:gameid/players/:playerid", (req, res) -> {
            Player player = new Gson().fromJson(req.body().toString(),Player.class);
            Board board = null;
            for(Board b : boards){
                if(b.getGameid().equals(req.params(":gameid"))) board=b;
            }
            ArrayList<Field> fields = board.getFields();
            fields.get(0).addPlayer(player);
            return "";
        });

        try {
            Unirest.post("http://vs-docker.informatik.haw-hamburg.de:8053/services")
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("name", "BOARD")
                    .queryString("description", "Board Service")
                    .queryString("service", "boards")
                    .queryString("uri", "https://vs-docker.informatik.haw-hamburg.de/ports/18193/boards")
                    .body(new Gson().toJson(new Service("BOARD", "Board Service", "board", "https://vs-docker.informatik.haw-hamburg.de/ports/18193/boards")))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
