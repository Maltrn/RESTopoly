package restopoly.resources;

/**
 * Created by Krystian.Graczyk on 27.11.15.
 */
public class Subscription {
    private String gameid;
    private String uri;
    private Event event;

    public Subscription(String gameid, String uri, Event event){
        this.gameid=gameid;
        this.uri=uri;
        this.event=event;
    }

    public String getGameid() {
        return gameid;
    }

    public Event getEvent() {
        return event;
    }

    public String getUri() {
        return uri;
    }
}
