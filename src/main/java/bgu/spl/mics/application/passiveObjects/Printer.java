package bgu.spl.mics.application.passiveObjects;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Printer {

    public static void SerializablePrinter(Object o, String filename){
        try {
            FileOutputStream outputStream = new FileOutputStream(filename + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(o);
            out.close();
            outputStream.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Error printing file");
        }
    }
}
