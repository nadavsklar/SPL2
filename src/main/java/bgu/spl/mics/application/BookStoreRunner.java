package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.*;
import java.io.FileNotFoundException;
import java.io.FileReader;

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


        JsonParser parser = new JsonParser();

        try {

            Object obj = parser.parse(new FileReader("input.json"));
            JsonObject jsonObject = (JsonObject) obj;

            BooksInfo = MainHelper.InitBooks(jsonObject, BooksInfo);
            VehiclesInfo = MainHelper.InitVehicles(jsonObject, VehiclesInfo);
            TimerService = MainHelper.InitTimerService(jsonObject, TimerService);
            SellingServices = MainHelper.InitSellingServices(jsonObject, SellingServices);
            InventoryServices = MainHelper.InitInventoryServices(jsonObject, InventoryServices);
            LogisticServices = MainHelper.InitLogisticServices(jsonObject, LogisticServices);
            ResourceServices = MainHelper.InitResourceServices(jsonObject, ResourceServices);
            Object[][] tmp = MainHelper.InitAPIServices(jsonObject, APIServices, Customers);
            APIServices = (APIService[]) tmp[0];
            Customers = (Customer[]) tmp[1];
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ///// Execution

        Inventory.getInstance().load(BooksInfo);
        ResourcesHolder.getInstance().load(VehiclesInfo);
        MicroService[] Workers = MainHelper.initiateWorkers(SellingServices, InventoryServices, LogisticServices, ResourceServices, APIServices);

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

        //Outputs
    }
}
