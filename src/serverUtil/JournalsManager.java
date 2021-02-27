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

    public JournalsManager(PersonInformationManager personInformationManager) {
        journals = new HashMap<String, ArrayList<Journal>>();
        this.personInformationManager = personInformationManager;
        readJournals();
    }

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

    public ArrayList<Journal> getJournals(String patientId) {
        if (journals.containsKey(patientId)) {
            return journals.get(patientId);
        }
        return null;
    }

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

    public void deleteJournal(String patientId) {
        journals.remove(patientId);
    }

    public boolean addJournal(String patientId, Doctor doctor, String nurseId) {
        Journal newJournal = new Journal(patientId, doctor.getId(), nurseId, doctor.getDivision());
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
