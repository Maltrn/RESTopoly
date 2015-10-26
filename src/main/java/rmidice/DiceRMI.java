package rmidice;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Krystian.Graczyk on 26.10.15.
 */
public interface DiceRMI extends Remote {
    Roll roll() throws RemoteException;
}
