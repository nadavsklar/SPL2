package bgu.spl.mics.application;

import java.io.*;

//Class which is responsible for printing to serializable files
public class Printer {

    //Printing
    public static void SerializablePrinter(Object o, String filename){
        try {
            FileOutputStream outputStream = new FileOutputStream(filename, true);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(o);
            out.close();
            outputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
