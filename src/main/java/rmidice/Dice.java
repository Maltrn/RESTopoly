package rmidice;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

/**
 * Created by Krystian.Graczyk on 26.10.15.
 */
public class Dice extends UnicastRemoteObject implements DiceRMI {
    private static final long serivalVersionUID = 1338L;

    public Dice() throws RemoteException {}

    public  Roll roll() throws RemoteException {
        Random rnd = new Random();
        return new Roll(rnd.nextInt(6)+1);
    }
}
