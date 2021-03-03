package serverUtil;

import staff.Patient;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import staff.*;

public class JournalsManager {
    private final String filePath = "Database/journals";
    private HashMap<String, ArrayList<Journal>> journals;
    private PersonInformationManager personInformationManager;

    /**
     * This is the class that manages the read/write of the journal file
     * and uses a PersonInformationManager when adding to the journal (a sort of authentication and nonrepudiation).
     * The journals map has the patientID as key.
     *
     * @param personInformationManager
     */

    public JournalsManager(PersonInformationManager personInformationManager) {
        journals = new HashMap<String, ArrayList<Journal>>();
        this.personInformationManager = personInformationManager;
        readJournals();
    }

    /**
     * Writes the journals map to the journals file
     * according to each journal entry's toString.
     */

    public void saveJournals() {
        PrintWriter pw = null;
        File journalBaseSave = new File(filePath);
        if (!journalBaseSave.exists()) {
            try {
                journalBaseSave.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            pw = new PrintWriter(journalBaseSave);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (Entry<String, ArrayList<Journal>> e : journals.entrySet()) {
            pw.println(e.getKey());
            for (Journal j : e.getValue()) {
                pw.println(j.toString());
            }
            pw.println("---");
        }
        pw.close();
    }

    /**
     * Reads a given journal file and imports all data into this model class.
     */

    public void readJournals() {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                ArrayList<Journal> temp = new ArrayList<Journal>();
                String patientId = line;
                while (!(line = bufferedReader.readLine()).equals("---")) {
                    if (line.substring(0, 6).equals("Doctor")) {
                        String[] lines = line.split(",");
                        String doctorId = lines[0].substring(7);
                        String nurseId = lines[1].substring(6);
                        temp.add(new Journal(patientId, doctorId, nurseId, personInformationManager.getPersonFromId(doctorId).getDivision()));
                    } else {
                        String[] lines = line.split(":", 2);
                        temp.get(temp.size() - 1).addRecord(new Record(lines[1], lines[0]));
                    }
                }
                journals.put(patientId, temp);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Used by a doctor or a nurse when requesting the journal of a given
     * patient, which will be rejected if the patient does not have the input person
     * registered as a doctor/nurse (and returns null).
     *
     * @param patientId
     * @param doctorOrNurseId
     * @return
     */

    public Journal getJournal(String patientId, String doctorOrNurseId) {
        if (journals.containsKey(patientId)) {
            for (Journal j : journals.get(patientId)) {
                if (j.getDoctorId().equals(doctorOrNurseId) || j.getNurseId().equals(doctorOrNurseId)) {
                    return j;
                }
            }
        }
        return null;
    }

    /**
     * Used by a patient when requesting to see its journals.
     *
     * @param patientId
     * @return
     */

    public ArrayList<Journal> getJournals(String patientId) {
        if (journals.containsKey(patientId)) {
            return journals.get(patientId);
        }
        return null;
    }

    /**
     * Used by a doctor or a nurse when requesting to see the journals
     * of all of its corresponding patients.
     *
     * @param person
     * @return
     */

    public ArrayList<Patient> getPatientsForPerson(Person person) {
        ArrayList<Patient> patients = new ArrayList<Patient>();
        for (Entry<String, ArrayList<Journal>> e : journals.entrySet()) {
            for (Journal j : e.getValue()) {
                if (j.isNurseOrDoctor(person)) {
                    patients.add((Patient) personInformationManager.getPersonFromId(j.getPatientId()));
                }
            }
        }
        return patients;
    }

    /**
     * Deletes a journal entry from the model.
     *
     * @param patientId
     */

    public void deleteJournal(String patientId) {
        journals.remove(patientId);
    }

    /**
     * Adding a new Journal give patientId, doctor and nurseID only if
     * they have appropriate clearances.
     *
     * @param patientId
     * @param doctor
     * @param nurseId
     * @return
     */

    public boolean addJournal(String patientId, Doctor doctor, String nurseId) {
        Journal newJournal = new Journal(patientId, doctor.getId(), nurseId, doctor.getDivision());
        Person patient = personInformationManager.getPersonFromId(patientId);
        Person nurse = personInformationManager.getPersonFromId(nurseId);
        if (!(patient instanceof Patient && nurse instanceof Nurse) ||
            !(patient.getDivision().equals(doctor.getDivision()) && nurse.getDivision().equals(doctor.getDivision()))) {
            return false;
        }

        if (!journals.containsKey(patientId)) {
            ArrayList<Journal> patientsJournals = new ArrayList<Journal>();
            patientsJournals.add(newJournal);
            journals.put(patientId, patientsJournals);
            return true;
        } else if (getJournal(patientId, doctor.getId()) == null){
            journals.get(patientId).add(newJournal);
            return true;
        }
        return false;
    }
}
