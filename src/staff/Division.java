package staff;

public class Division {
    private String id;
    private String name;

    public Division(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String toString(){
        return name;
    }
}