package restopoly.resources;

/**
 * Created by Krystian.Graczyk on 27.10.15.
 */
public class Components {

    private String game;
    private String dice;
    private String board;
    private String bank;
    private String broker;
    private String decks;
    private String events;

    Components(){
    }

    Components(String gameHost, String diceHost, String boardHost, String bankHost, String brokerHost, String decksHost, String eventsHost){
        game =  gameHost;
        dice = diceHost;
        board = boardHost;
        bank = bankHost;
        broker = brokerHost;
        decks = decksHost;
        events = eventsHost;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getDice() {
        return dice;
    }

    public void setDice(String dice) {
        this.dice = dice;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getDecks() {
        return decks;
    }

    public void setDecks(String decks) {
        this.decks = decks;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }
}
