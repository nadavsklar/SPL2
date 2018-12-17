package bgu.spl.mics.application;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.*;

import java.io.*;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static void main(String[] args) {

        BookInventoryInfo[] BooksInfo = null;
        DeliveryVehicle[] VehiclesInfo = null;
        TimeService TimerService = null;
        SellingService[] SellingServices = null;
        InventoryService[] InventoryServices = null;
        LogisticsService[] LogisticServices = null;
        ResourceService[] ResourceServices = null;
        APIService[] APIServices = null;
        Customer[] Customers = null;
        MicroService[] Workers = null;
        JsonParser parser = new JsonParser();

        try {
            //Reading from Json file
            Object obj = parser.parse(new FileReader(args[0]));
            JsonObject jsonObject = (JsonObject) obj;
            BooksInfo = MainHelper.InitBooks(jsonObject);
            VehiclesInfo = MainHelper.InitVehicles(jsonObject);
            SellingServices = MainHelper.InitSellingServices(jsonObject);
            InventoryServices = MainHelper.InitInventoryServices(jsonObject);
            LogisticServices = MainHelper.InitLogisticServices(jsonObject);
            ResourceServices = MainHelper.InitResourceServices(jsonObject);
            Object[][] tmp = MainHelper.InitAPIServices(jsonObject);
            APIServices = (APIService[]) tmp[0];
            Customers = (Customer[]) tmp[1];
            Workers = MainHelper.initiateWorkers(SellingServices, InventoryServices, LogisticServices, ResourceServices, APIServices);
            TimerService = MainHelper.InitTimerService(jsonObject, Workers.length);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /////Execution
        Inventory.getInstance().load(BooksInfo);
        ResourcesHolder.getInstance().load(VehiclesInfo);

        Thread[] WorkersThreads = MainHelper.initiateThreads(Workers);
        for (Thread thread : WorkersThreads)
            thread.start();

        Thread TimeThread = new Thread(TimerService);
        TimeThread.start();
        try {
            TimeThread.join();
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        MainHelper.printOutputs(Customers, args[1], args[2], args[3], args[4]);



    }

}
