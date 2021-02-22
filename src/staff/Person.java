package staff;

public class Person {
    private String id;
    private String name;
    private Division div;

    public Person(String name, Division div, String id) {
        this.name = name;
        this.div = div;
        this.id=id;
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
