package serverUtil;

import serverUtil.*;
import staff.*;
import javax.security.cert.X509Certificate;
import java.util.ArrayList;

public class ClientInputManager {

    public static final String LIST_PATIENT_RECORDS = "1";
    public static final String LIST_DIVISION_RECORDS = "2";
    public static final String READ_PATIENT_RECORD = "3";
    public static final String WRITE_PATIENT_RECORD = "4";
    public static final String CREATE_PATIENT_RECORD = "5";
    public static final String DELETE_PATIENT_RECORD = "6";

    private PersonInformationManager personInformationManager;
    private JournalsManager journalsManager;
    private Logger logger;

    public ClientInputManager() {
        personInformationManager = new PersonInformationManager();
        journalsManager = new JournalsManager(personInformationManager);
        logger = new Logger();
    }

    public void save() {
        journalsManager.saveJournals();
    }

    public String handleClientInput(String clientInput, Person person) {
        String[] inputs = clientInput.split(" ");
        String option = (inputs.length > 0 ? inputs[0] : "");

        if (option.equals(LIST_PATIENT_RECORDS) &&
                (person instanceof Nurse || person instanceof Doctor)) {
            Logger.log(person.getId(), person.getName(), "viewed associated patient records");

        }
        else if (option.equals(LIST_DIVISION_RECORDS) &&
                (person instanceof Nurse || person instanceof Doctor)) {
            Logger.log(person.getId(), person.getDivision().toString(), "viewed division patient records");

        }
        else if (option.equals(READ_PATIENT_RECORD) && person instanceof Patient) {
            Logger.log(person.getId(), person.getName(), "read patient record");
            ArrayList<Journal> journals = journalsManager.getJournals(person.getId());
            String response = "";
            if (journals == null) {
                response += "You don't have any records\n";
            } else {
                for (Journal journal: journals) {
                    response += journal;
                }
            }
            return response + "\n" + listOptions(person);
        }
        else if (option.equals(READ_PATIENT_RECORD) && (person instanceof GovernmentAgency ||
                person instanceof Nurse || person instanceof Doctor)) {
            //Logger.log(person.getId(), patientId, "accessed patient records");
        }
        else if (option.equals(WRITE_PATIENT_RECORD) &&
                (person instanceof Nurse || person instanceof Doctor)) {
            //Logger.log(person.getId(), patientId, "wrote to patient record");
        }
        else if (option.equals(CREATE_PATIENT_RECORD) && person instanceof Doctor) {
            //Logger.log(person.getId(), patientId, "created patient record");
        }
        else if (option.equals(DELETE_PATIENT_RECORD) && person instanceof GovernmentAgency) {
            //Logger.log(person.getId(), patientId, "deleted patient record");

        }

        return listOptions(person);
    }

    public String listOptions(Person person) {
        String options = "";

        if (person instanceof Nurse || person instanceof Doctor) {
            options += "Enter 1 to list patient records\n" +
                    "Enter 2 to list division records\n" +
                    "Enter 3 followed by id to read a patient record (example: 3 5)\n" +
                    "Enter 4 followed by patient id to write a patient record (example 4 5)\n";
        }
        if (person instanceof Doctor) {
            options += "Enter 5 followed by patient id followed by nurse id to create a patient record (example 5 6 2)\n";
        }
        if (person instanceof GovernmentAgency) {
            options += "Enter 3 followed by id to read a patient record (example: 3 5)\n" +
                    "Enter 6 followed by id to delete a patient record (example 6 5)\n";
        }
        if (person instanceof Patient) {
            options += "Enter 3 to read your patient record\n";
        }

        return options;
    }

    public Person getPerson(X509Certificate cert) {
        //System.out.println(cert.getSerialNumber());
        return personInformationManager.getPersonFromSerialNumber(cert.getSerialNumber());
    }
}