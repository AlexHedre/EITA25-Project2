package serverUtil;

import serverUtil.*;
import staff.*;
import javax.security.cert.X509Certificate;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

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

    /**
     * The main class used by the Server when handling the input given by a client.
     * It uses a personInformationManager (keeping track of every person in the system),
     * a journalsManager (writing and reading the journals file) and a logger.
     */

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

            String response = "Name:ID\n";
            for (Patient patient : journalsManager.getPatientsForPerson(person)) {
                response += patient + "\n";
            }
            logger.log(person.getId(), person.getId(), "viewed associated patient records");
            return response + "\n" + listOptions(person);
        }
        else if (option.equals(LIST_DIVISION_RECORDS) &&
                (person instanceof Nurse || person instanceof Doctor)) {

            String response = "Name:ID\n";
            for (Patient patient : person.getDivision().getMembers()) {
                response += patient + "\n";
            }
            logger.log(person.getId(), person.getDivision().toString(), "viewed division patient records");
            return response + "\n" + listOptions(person);
        }
        else if (option.equals(READ_PATIENT_RECORD) && person instanceof Patient) {

            ArrayList<Journal> journals = journalsManager.getJournals(person.getId());
            String response = "";
            if (journals == null) {
                response += "You don't have any record\n";
            } else {
                for (Journal journal: journals) {
                    response += journal + "\n";
                }
            }
            logger.log(person.getId(), person.getId(), "read patient record");
            return response + "\n" + listOptions(person);
        }
        else if (inputs.length > 1) {
            String patientId = inputs[1];
            if (option.equals(READ_PATIENT_RECORD) &&
                    (person instanceof Nurse || person instanceof Doctor)) {

                ArrayList<Journal> journals = journalsManager.getJournals(patientId);
                String response = "";
                if (journals == null) {
                    response += "Patient don't have any record\n";
                } else {
                    response += journalsManager.getJournal(patientId, person.getId()) + "\n";
                }
                logger.log(person.getId(), patientId, "accessed patient records");
                return response + "\n" + listOptions(person);
            }
            else if (option.equals(READ_PATIENT_RECORD) && person instanceof GovernmentAgency) {

                ArrayList<Journal> journals = journalsManager.getJournals(patientId);
                String response = "";
                if (journals == null) {
                    response += "Patient don't have any record\n";
                } else {
                    for (Journal journal: journals) {
                        response += journal + "\n";
                    }
                }
                logger.log(person.getId(), patientId, "accessed patient records");
                return response + "\n" + listOptions(person);
            }
            else if (option.equals(WRITE_PATIENT_RECORD) &&
                    (person instanceof Nurse || person instanceof Doctor)) {

                if (journalsManager.getJournal(patientId,person.getId()) == null) {
                    return "Patient has no record associated with you" + "\n\n" + listOptions(person);
                }
                return "Write information";
            }
            else if (option.equals(CREATE_PATIENT_RECORD) &&
                    person instanceof Doctor && inputs.length > 2) {

                String nurseId = inputs[2];
                String response = "";
                if (journalsManager.addJournal(patientId, (Doctor) person, nurseId)) {
                    response += "Record for patient was successfully created\n";
                    logger.log(person.getId(), patientId, "created patient record");
                } else {
                    response += "Unable to create record for patient\n";
                    logger.log(person.getId(), patientId, "tried to create patient record");
                }
                return response + "\n" + listOptions(person);
            }
            else if (option.equals(DELETE_PATIENT_RECORD) && person instanceof GovernmentAgency) {

                logger.log(person.getId(), patientId, "deleted patient record");
                journalsManager.deleteJournal(patientId);
                return "Patient record was deleted" + "\n\n" + listOptions(person);
            }
        }

        return listOptions(person);
    }

    public String writeInformation(String patientId, String information, Person person) {
        logger.log(person.getId(), patientId, "wrote to patient record");
        journalsManager.getJournal(patientId, person.getId()).addRecord(new Record(information, Logger.getDate()));
        return "Record was successfully written" + "\n\n" + listOptions(person);
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

        options += "Enter quit to log off\n";
        return options;
    }

    public Person getPerson(X509Certificate cert) {
        return personInformationManager.getPersonFromSerialNumber(cert.getSerialNumber());
    }
}