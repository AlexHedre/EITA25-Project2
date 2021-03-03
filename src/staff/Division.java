package staff;

import java.util.*;

public class Division {
    private String id;
    private String name;
    private ArrayList<Patient> members;

    /**
     * The division class has an id and a name as attributes and a
     * list of patients.
     * @param id
     * @param name
     */

    public Division(String id, String name) {
        this.id = id;
        this.name = name;
        members = new ArrayList<Patient>();
    }

    public void addMember (Patient patient) {
        members.add(patient);
    }

    public ArrayList<Patient> getMembers() {
        return members;
    }

    public String getId() {
        return id;
    }

    public String toString(){
        return name;
    }
}