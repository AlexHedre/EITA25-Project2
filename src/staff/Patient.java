package staff;

public class Patient extends Person {
    private String doctorId;

    public Patient(String name, Division div, String id, String doctorId) {
        super(name, id, div);
        this.doctorId = doctorId;
    }
}
