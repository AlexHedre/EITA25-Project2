package serverUtil;

public class Record {
    private String entry;
    private String date;

    /**
     * The Record class is responsible for
     * the lines written out into the journal.
     * @param entry
     * @param date
     */

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
