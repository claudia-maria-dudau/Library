package library;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class CSV {
    public static CSV instance = null;

    private CSV(){}

    public static CSV getInstance(){
        if (instance == null){
            synchronized (CSV.class) {
                if (instance == null) {
                    instance = new CSV();
                }
            }
        }

        return instance;
    }

    public List<String> read(String fileName){
        List<String> lines = null;

        try {
            lines = Files.readAllLines(Paths.get(String.format("data/input/%s", fileName)));
        } catch (IOException e){
            System.out.println("The file " + fileName + " couldn't be open.");
        }

        return lines;
    }

    public void write(String fileName, List<String> content){
        try {
            Files.write(Paths.get(String.format("data/output/%s", fileName)), content);
        } catch (IOException e){
            System.out.println("The file " + fileName + " couldn't be open.");
        }
    }
}
