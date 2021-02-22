package staff;

public class Person {
    private String id;
    private String name;
    private Division div;

    public Person(String id, String name, Division div) {
        this.id = id;
        this.name = name;
        this.div = div;
    }

    public String getName() {
        return name;
    }

    public String getId(){
        return id;
    }

    public Division getDivision() {
        return div;
    }

}
