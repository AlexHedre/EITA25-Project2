package database;

import staff.*;

public class Record {
    private Patient patient;
    private Doctor doctor;
    private Nurse nurse;
    private Division division;
    private String data;

    public Record(Patient patient, Doctor doctor, Nurse nurse, String data) {
        this.patient = patient;
        this.doctor = doctor;
        this.nurse = nurse;
        this.division = doctor.getDivision();
        this.data = data;
    }

    public boolean canRead(Person person) {
        return (person.getId().equals(patient.getId()));
    }

    public boolean canWrite(Person person) {
        return (person.getId().equals(doctor.getId()) || person.getId().equals(nurse.getId()));
    }

    public void writeToRecord(String data) {
        this.data = data;
    }
}
