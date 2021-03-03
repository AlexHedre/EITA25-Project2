package staff;

public class Nurse extends Person {

    /**
     * The nurse type with name, division and id given as parameters.
     * @param name
     * @param div
     * @param id
     */

    public Nurse(String name, Division div, String id) {
        super(name, id, div);
    }
}
