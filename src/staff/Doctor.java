package staff;

public class Doctor extends Person {

    /**
     * The doctor type with name, division and id given as parameters.
     * @param name
     * @param div
     * @param id
     */

    public Doctor(String name, Division div, String id) {
        super(name, id, div);
    }
}
