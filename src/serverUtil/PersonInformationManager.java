package serverUtil;

import staff.*;

import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class PersonInformationManager {
    private final String filePath = "Database/personInformation";
    private HashMap<BigInteger, Person> persons;
    private ArrayList<Division> divisions;

    public PersonInformationManager () {
        persons = new HashMap<BigInteger, Person>();
        divisions = new ArrayList<Division>();
        readFile();
    }

    public Person getPersonFromSerialNumber(BigInteger serialNumber) {
        if (persons.containsKey(serialNumber)) {
            return persons.get(serialNumber);
        }
        return null;
    }

    public Person getPersonFromId (String id) {
        for (Map.Entry<BigInteger, Person> e : persons.entrySet()) {
            Person person = e.getValue();
            if (person.getId().equals(id)) {
                return person;
            }
        }
        return null;
    }

    public Division getDivisionFromId (String id) {
        for (Division division : divisions) {
            if (division.getId().equals(id)) {
                return division;
            }
        }
        return null;
    }

    public void readFile() {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = null;
            while((line = bufferedReader.readLine()) != null && !line.equals("---")){
                String[] divisionInfo = line.split(":");
                String id = divisionInfo[0];
                String name = divisionInfo[1];
                divisions.add(new Division(id, name));
            }
            while((line = bufferedReader.readLine()) != null){
                String[] personInfo = line.split(":");
                BigInteger serialNumber = new BigInteger(personInfo[0]);
                String type = personInfo[1];
                Division division = getDivisionFromId(personInfo[2]);
                String id = personInfo[3];
                String name = personInfo[4];
                Person person;
                if (type.equals("Nurse")) {
                    person = new Nurse(name, division, id);
                } else if (type.equals("Doctor")) {
                    person = new Doctor(name, division, id);
                } else if (type.equals("GovernmentAgency")) {
                    person = new GovernmentAgency(name, id);
                } else {
                    person = new Patient(name ,division, id, personInfo[5]);
                }
                persons.put(serialNumber, person);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
