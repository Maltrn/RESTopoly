package restopoly.resources;

/**
 * Created by final-work on 19.01.16.
 */
public class Estate {

    String place;
    String owner;
    String value;
    String rent;
    String cost;
    String houses;
    String visit;
    String hypocredit;

    public Estate(String place, String owner, String value, String rent, String cost, String houses, String visit, String hypocredit) {
        this.place = place;
        this.owner = owner;
        this.value = value;
        this.rent = rent;
        this.cost = cost;
        this.houses = houses;
        this.visit = visit;
        this.hypocredit = hypocredit;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getHouses() {
        return houses;
    }

    public void setHouses(String houses) {
        this.houses = houses;
    }

    public String getVisit() {
        return visit;
    }

    public void setVisit(String visit) {
        this.visit = visit;
    }

    public String getHypocredit() {
        return hypocredit;
    }

    public void setHypocredit(String hypocredit) {
        this.hypocredit = hypocredit;
    }
}
