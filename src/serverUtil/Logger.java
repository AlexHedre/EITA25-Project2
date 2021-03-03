package serverUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static PrintStream output;

    /**
     * The Logger class writing to Database/logs.
     */

    public Logger() {
        try {
            FileOutputStream fileOutput = new FileOutputStream("Database/logs", true);
            output = new PrintStream(fileOutput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * A simple function of getting a time string
     * with format yyyy-MM-dd HH:mm:ss.
     * @return String
     */

    public static String getTimeDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Similar to above byt with format yyyy-MM-dd.
     * @return String
     */

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * The important method that writes to the logs file.
     * Example: 2021-02-27 20:09:14: 1 wrote to patient record for 5.
     * @param editor
     * @param patient
     * @param action
     */

    public static void log(String editor, String patient, String action) {
        output.println(getTimeDate() + ": " + editor + " " + action + " for "  + patient);
        output.flush();
    }
}