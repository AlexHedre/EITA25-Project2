package serverUtil;

import java.io.*;
import java.util.*;

public class JournalsManager {
    private final String filePath = "Database/journals";
    private HashMap<String, ArrayList<Journal>> journals;

    public JournalsManager() {
        journals = new HashMap<String, ArrayList<Journal>>();
        readJournals();
    }

    public void saveJournal(Journal journal) {

    }


    public void readJournals() {
        FileReader fileReader;
    }
}
