package restopoly.resources;

import restopoly.resources.Roll;

/**
 * Created by Krystian.Graczyk on 27.10.15.
 */
public class Throw {
    private Roll roll1;
    private Roll roll2;

    public Throw(){
        this.roll1 = new Roll();
        this.roll2 = new Roll();
    }

    public Roll getRoll1() {
        return roll1;
    }

    public Roll getRoll2() {
        return roll2;
    }

}
