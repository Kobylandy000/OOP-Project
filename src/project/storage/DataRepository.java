package project.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataRepository {
    private static final String FILE_PATH = "data/university.ser";

    private static DataRepository instance;

    private DataRepository() {
    }

    public static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    public void save(AppData data) {
        try {
            File file = new File(FILE_PATH);
            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(FILE_PATH)
            );

            out.writeObject(data);
            out.close();

            System.out.println("Data saved successfully.");

        } catch (IOException e) {
            System.out.println("Error while saving data: " + e.getMessage());
        }
    }

    public AppData load() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.out.println("No saved data found. Creating default data...");
            return null;
        }

        try {
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(FILE_PATH)
            );

            AppData data = (AppData) in.readObject();
            in.close();

            System.out.println("Data loaded successfully.");
            return data;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while loading data: " + e.getMessage());
            return null;
        }
    }
}