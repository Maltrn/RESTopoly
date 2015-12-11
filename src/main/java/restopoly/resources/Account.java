package restopoly.resources;

/**
 * Created by Krystian.Graczyk on 05.11.15.
 */
public class Account {

    String id;
    Player player;
    int saldo;

    public Account(String playerid, Player player, int saldo){
        this.id = playerid;
        this.player = player;
        this.saldo = saldo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
}
