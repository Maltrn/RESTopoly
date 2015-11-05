package restopoly.resources;

/**
 * Created by Krystian.Graczyk on 29.10.15.
 */
public class Transfer {
    private String from;
    private String to;
    private String reason;
    private String event;
    private int amount;

    public Transfer(String from, String to, int amount){
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public Transfer(String from, String to, String reason, String event, int amount){
        this.from = from;
        this.to = to;
        this.reason = reason;
        this.event = event;
        this.amount = amount;
    }



}
