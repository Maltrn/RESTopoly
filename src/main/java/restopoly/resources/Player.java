package restopoly.resources;

/**
 * Created by Krystian.Graczyk on 27.10.15.
 */
public class Player {

    private String id;
    private String name;
    private String uri;
    private Place place = null;
    private int position;
    private boolean ready = false;

    public Player(String playerid){
        this.id = playerid;
    }

    public Player(String playerid, String playername){
        this.id = playerid;
        this.name = playername;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

}
