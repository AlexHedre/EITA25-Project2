package staff;

public class Patient extends Person {
    private Doctor doctor;

    public Patient(String name, Division div, String id, Doctor doctor) {
        super(name, id, div);
        this.doctor = doctor;
    }
}
