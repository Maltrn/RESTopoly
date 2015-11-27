package restopoly.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import restopoly.Service;
import restopoly.resources.*;

import java.util.ArrayList;

import static spark.Spark.*;

/**
 * Created by Krystian.Graczyk on 27.11.15.
 */
public class BoardService {

    public static void main(String[] args) {

        Boards boards = new Boards();

        put("/boards/:gameid", (req, res) -> {
            Board board = new Board(req.params(":gameid"));
            ArrayList<Field> fields = new ArrayList<Field>();
            fields.add(new Field(new Place("0"),new ArrayList<Player>()));
            fields.add(new Field(new Place("1"),new ArrayList<Player>()));
            fields.add(new Field(new Place("2"),new ArrayList<Player>()));
            fields.add(new Field(new Place("3"),new ArrayList<Player>()));

            board.setFields(fields);
            boards.addBoard(board);
            return "";
        });

        put("/boards/:gameid/players/:playerid", (req, res) -> {
            HttpResponse playerResponse = Unirest.get("http://vs-docker.informatik.haw-hamburg.de:18191/games/" + req.params(":gameid")+"/players/"+req.body()).asJson();
            Gson gson = new Gson();
            Player player = gson.fromJson(playerResponse.getBody().toString(),Player.class);
            Board board = boards.getBoard(req.params(":gameid"));
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
