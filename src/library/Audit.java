package library;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Audit {
    public static Audit instance = null;

    private Audit() {}

    public static Audit getInstance(){
        if (instance == null){
            instance = new Audit();
        }

        return instance;
    }

    public void write(String action, Date timestamp) {
        try (FileWriter writer = new FileWriter(new File("data/output/log.csv"), Boolean.TRUE)){
            writer.write(String.format("%s, %s\n", action, timestamp.toString()));
        } catch (IOException e){
            System.out.println("The logging file couldn't be opened.");
        }
    }
}