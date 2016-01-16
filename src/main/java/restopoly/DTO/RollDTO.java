package restopoly.DTO;

import restopoly.resources.Roll;

/**
 * Created by final-work on 15.01.16.
 */
public class RollDTO {
    Roll roll1;
    Roll roll2;

    public RollDTO(int number1, int number2){
        roll1.setNumber(number1);
        roll2.setNumber(number2);
    }

    public Roll getRoll2() {
        return roll2;
    }

    public void setRoll2(Roll roll2) {
        this.roll2 = roll2;
    }

    public Roll getRoll1() {
        return roll1;
    }

    public void setRoll1(Roll roll1) {
        this.roll1 = roll1;
    }
}
