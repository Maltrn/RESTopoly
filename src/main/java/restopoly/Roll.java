package restopoly;
/**
 * Created by nickdiedrich on 19.10.15.
 */
import static spark.Spark.get;
import static spark.Spark.post;
import spark.Request;
import spark.Response;
import spark.Route;

public class Roll {
    public static void main( String[] args) {
        get("/posts", (req, res) -> {
            return "42";
        });
    }
}
