package serverUtil;

import staff.*;
import java.util.*;
import java.io.*;

public class PersonInformationManager {
    private final String filePath = "Database/personInformation";
    private HashMap<String, Person> persons;
    private ArrayList<Division> divisions;

    public PersonInformationManager () {
        persons = new HashMap<String, Person>();
        divisions = new ArrayList<Division>();
        readFile();
    }

    public Person getPersonFromId(String id) {
        if (persons.containsKey(id)) {
            return persons.get(id);
        }
        return null;
    }

    public void readFile() {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveFile() {

    }
}
