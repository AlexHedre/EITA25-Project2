package staff;



public class Person {
    private String id;
    private String name;
    private Division div;

    /**
     * The general class that will be
     * extended by person types Doctor, Nurse
     * Patient and GovernmentAgency.
     * @param name
     * @param id
     * @param div
     */

    public Person(String name, String id, Division div) {
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

    public String toString() {
        return name + ":" + id;
    }

}
