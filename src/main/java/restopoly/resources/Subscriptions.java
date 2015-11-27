package restopoly.resources;

import java.util.ArrayList;

/**
 * Created by Krystian.Graczyk on 27.11.15.
 */
public class Subscriptions {
    private ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();

    public Subscriptions(){}

    public ArrayList<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void addSubscription(Subscription subscription){
        subscriptions.add(subscription);
    }
}
