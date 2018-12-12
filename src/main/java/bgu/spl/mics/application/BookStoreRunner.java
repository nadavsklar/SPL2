package bgu.spl.mics.application;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static void main(String[] args) {

        BookInventoryInfo[] BooksInfo;
        JsonParser parser = new JsonParser();

        try {

            Object obj = parser.parse(new FileReader("input.json"));
            JsonObject jsonObject = (JsonObject) obj;

            JsonArray Books = jsonObject.getAsJsonArray("initialInventory");
            BooksInfo = new BookInventoryInfo[Books.size()];

            for (int i = 0; i < Books.size(); i++) {

                String bookInfo = Books.get(i).toString();
                String bookTitle = bookInfo.substring(bookInfo.indexOf(':') + 1, bookInfo.indexOf(','));
                bookTitle = bookTitle.substring(1, bookTitle.length() - 1);
                bookInfo = bookInfo.substring(bookInfo.indexOf(',') + 1);

                //
                String amountInfo = bookInfo.substring(bookInfo.indexOf(':') + 1, bookInfo.indexOf(','));
                bookInfo = bookInfo.substring(bookInfo.indexOf(',') + 1);
                int amount = Integer.parseInt(amountInfo);

                //
                String priceInfo = bookInfo.substring(bookInfo.indexOf(':') + 1, bookInfo.indexOf('}'));
                int price = Integer.parseInt(priceInfo);

                BooksInfo[i] = new BookInventoryInfo(bookTitle, amount, price);

            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
