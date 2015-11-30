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
    private String deck;
    private String event;

    Components(){
    }

    Components(String gameHost, String diceHost, String boardHost, String bankHost, String brokerHost, String deckHost, String eventHost){
        game =  gameHost;
        dice = diceHost;
        board = boardHost;
        bank = bankHost;
        broker = brokerHost;
        deck = deckHost;
        event = eventHost;
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

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
