package restopoly.resources;

/**
 * Created by Krystian.Graczyk on 27.11.15.
 */
public class Event {

    private String gameid;
    private String eventid;
    private String uri;
    private String type;
    private String name;
    private String reason;
    private String resource;
    private Player player;

    public Event(String type, String name, String reason){
        this.type=type;
        this.name=name;
        this.reason=reason;
    }

    public Event(String type, String name, String reason, String resource, Player player){
        this.type=type;
        this.name=name;
        this.reason=reason;
        this.resource=resource;
        this.player=player;
    }

    public Event(String gameid, String type, String name, String reason, String resource, Player player){
        this.gameid = gameid;
        this.type=type;
        this.name=name;
        this.reason=reason;
        this.resource=resource;
        this.player=player;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
