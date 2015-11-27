package restopoly.resources;

import java.util.ArrayList;

/**
 * Created by Krystian.Graczyk on 27.11.15.
 */
public class Events {
        private ArrayList<Event> events = new ArrayList<Event>();

        public Events(){}

        public ArrayList<Event> getEvents() {
            return events;
        }

        public void addEvent(Event event) {
            events.add(event);
        }

        public void deleteEvent(Event event) {
        events.remove(event);
        }
}
