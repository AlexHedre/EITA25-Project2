package serverUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static PrintStream output;

    public Logger() {
        try {
            FileOutputStream fileOutput = new FileOutputStream("Database/logs", true);
            output = new PrintStream(fileOutput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getTimeDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public static void log(String editor, String patient, String action) {
        output.println(getTimeDate() + " " + editor + " " + action + " for patient "  + patient);
        output.flush();
    }
}