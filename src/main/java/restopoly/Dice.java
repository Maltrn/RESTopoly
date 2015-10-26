package restopoly;

import java.util.Random;

/**
 * Created by Krystian.Graczyk on 26.10.15.
 */
public class Dice {

    private int number;

    public Dice(){
        Random rnd = new Random();
        number = rnd.nextInt(6)+1;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return
                "number:" + number
                ;
    }
}
