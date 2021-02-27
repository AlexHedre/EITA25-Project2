package serverUtil;

import serverUtil.*;
import staff.*;
import javax.security.cert.X509Certificate;

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

    public String handleClientInput(String input, Person person) {

        return "";
    }

    public Person getPerson(X509Certificate cert) {
        //System.out.println(cert.getSerialNumber());
        return personInformationManager.getPersonFromSerialNumber(cert.getSerialNumber());
    }
}