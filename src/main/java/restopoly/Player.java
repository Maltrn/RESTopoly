package restopoly;

/**
 * Created by Krystian.Graczyk on 27.10.15.
 */
public class Player {

    private String playerid;
    private String name;
    private String uri;
    private Place place;
    private int position;
    private boolean ready = false;

    public Player(String playerid){
        this.playerid = playerid;
    }


    public String getPlayerid() {
        return playerid;
    }

    public void setPlayerid(String playerid) {
        this.playerid = playerid;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        return
                "playerid:'" + playerid + '\'' +
                ", name:'" + name + '\'' +
                ", uri:'" + uri + '\'' +
                ", ready:" + ready +
                '}';
    }
}
