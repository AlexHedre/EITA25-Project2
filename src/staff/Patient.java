package staff;

public class Patient extends Person {
    private String doctorId;

    /**
     * The patient type with name, division and id given as parameters.
     * A unique doctorId will also be associated with the patient.
     * @param name
     * @param div
     * @param id
     * @param doctorId
     */

    public Patient(String name, Division div, String id, String doctorId) {
        super(name, id, div);
        this.doctorId = doctorId;
    }
}
