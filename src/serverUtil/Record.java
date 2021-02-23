package serverUtil;

public class Record {
    private String entry;
    private String date;

    public Record(String entry, String date) {
        this.entry = entry;
        this.date = date;
    }

    public String getEntry() {
        return entry;
    }

    public String getDate() {
        return date;
    }

    public String toString(){
        return  date + ":" + entry;
    }
}
