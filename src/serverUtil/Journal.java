package serverUtil;

import java.util.ArrayList;
import staff.*;

public class Journal {
    private ArrayList<Record> records;
    private String patientId;
    private String doctorId;
    private String nurseId;
    private Division division;

    public Journal(Patient patient, Doctor doctor, Nurse nurse, Division division) {
        records = new ArrayList<Record>();
        patientId = patient.getId();
        doctorId = doctor.getId();
        nurseId = nurse.getId();
    }

    public String getDoctorId(){
        return doctorId;
    }

    public String getNurseId(){
        return nurseId;
    }

    public void addEntry(Record record) {
        records.add(record);
    }

    public boolean canRead(Person person) {
        return (person.getId().equals(patientId));
    }

    public boolean canWrite(Person person) {
        return (person.getId().equals(doctorId) || person.getId().equals(nurseId));
    }

    public String toString(){
        String output = "Doctor="+ doctorId + ",Nurse=" + nurseId;
        for(Record r : records){
            output+= "\n" +r.toString();
        }
        return output;
    }

}
