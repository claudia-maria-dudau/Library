package library.database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Audit {
    private static Audit instance = null;

    private Audit() {}

    public static Audit getInstance(){
        if (instance == null){
            synchronized (Audit.class) {
                if (instance == null) {
                    instance = new Audit();
                }
            }
        }

        return instance;
    }

    public void log(String action, Date timestamp, String threadName) {
        try (FileWriter writer = new FileWriter(new File("data/output/log.csv"), Boolean.TRUE)){
            writer.write(String.format("%s, %s, %s\n", action, timestamp.toString(), threadName));
        } catch (IOException e){
            System.out.println("The logging file couldn't be opened.");
        }
    }
}
