package restopoly;

/**
 * Created by Krystian.Graczyk on 25.11.15.
 */
public class Service {
    private String name;
    private String description;
    private String service;
    private String uri;

    public Service(String name, String description, String service, String uri){
        this.name=name;
        this.description=description;
        this.service=service;
        this.uri=uri;
    }
}
