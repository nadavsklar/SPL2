package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

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
        APIService[] apiServices = null;
        Customer[] Customers = null;


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
                //
                BooksInfo[i] = new BookInventoryInfo(bookTitle, amount, price);

            }

            JsonElement Vehicles = jsonObject.getAsJsonArray("initialResources").get(0).getAsJsonObject();
            JsonElement temp = ((JsonObject) Vehicles).get("vehicles");
            JsonArray VehiclesArray = temp.getAsJsonArray();

            VehiclesInfo = new DeliveryVehicle[VehiclesArray.size()];

            for (int i = 0; i < VehiclesArray.size(); i++) {
                String licenseInfo = VehiclesArray.get(i).getAsJsonObject().get("license").toString();
                int license = Integer.parseInt(licenseInfo);
                String speedInfo = VehiclesArray.get(i).getAsJsonObject().get("speed").toString();
                int speed = Integer.parseInt(speedInfo);
                VehiclesInfo[i] = new DeliveryVehicle(license, speed);
            }

            JsonElement Time = jsonObject.getAsJsonObject("services").get("time");
            String TimeInfo = Time.toString();
            String speedInfo = TimeInfo.substring(TimeInfo.indexOf(':') + 1, TimeInfo.indexOf(','));
            TimeInfo = TimeInfo.substring(TimeInfo.indexOf(',') + 1);
            String durationInfo = TimeInfo.substring(TimeInfo.indexOf(':') + 1, TimeInfo.indexOf('}'));
            int speed = Integer.parseInt(speedInfo);
            int duration = Integer.parseInt(durationInfo);
            TimerService = new TimeService("Timer", speed, duration);

            JsonElement SellingAmount = jsonObject.getAsJsonObject("services").get("selling");
            int size = Integer.parseInt(SellingAmount.toString());
            SellingServices = new SellingService[size];
            for(int i = 0; i < SellingServices.length; i++)
                SellingServices[i] = new SellingService("Seller " + i);

            JsonElement InventoryAmount = jsonObject.getAsJsonObject("services").get("inventoryService");
            size = Integer.parseInt(InventoryAmount.toString());
            InventoryServices = new InventoryService[size];
            for(int i = 0; i < InventoryServices.length; i++)
                InventoryServices[i] = new InventoryService("Inventory Handler " + i);

            JsonElement LogisticAmount = jsonObject.getAsJsonObject("services").get("logistics");
            size = Integer.parseInt(LogisticAmount.toString());
            LogisticServices = new LogisticsService[size];
            for(int i = 0; i < LogisticServices.length; i++)
                LogisticServices[i] = new LogisticsService("Logistic Handler " + i);

            JsonElement ResourceAmount = jsonObject.getAsJsonObject("services").get("resourcesService");
            size = Integer.parseInt(ResourceAmount.toString());
            ResourceServices = new ResourceService[size];
            for(int i = 0; i < ResourceServices.length; i++)
                ResourceServices[i] = new ResourceService("Resource Handler " + i);

            JsonArray CustomersArray = jsonObject.getAsJsonObject("services").getAsJsonArray("customers");
            size = CustomersArray.size();
            Customers = new Customer[size];
            apiServices = new APIService[size];
            for(int i = 0; i < Customers.length; i++){
                JsonElement CurrentCustomer = CustomersArray.get(i);
                String idInfo = CurrentCustomer.getAsJsonObject().get("id").toString();
                int id = Integer.parseInt(idInfo);
                String name = CurrentCustomer.getAsJsonObject().get("name").toString();
                String address = CurrentCustomer.getAsJsonObject().get("address").toString();
                String distanceInfo = CurrentCustomer.getAsJsonObject().get("distance").toString();
                int distance = Integer.parseInt(distanceInfo);
                String creditCardNumberInfo = CurrentCustomer.getAsJsonObject().get("creditCard").getAsJsonObject().get("number").toString();
                int creditCardNumber = Integer.parseInt(creditCardNumberInfo);
                String amountCreditCardNumberInfo = CurrentCustomer.getAsJsonObject().get("creditCard").getAsJsonObject().get("amount").toString();
                int amountCreditCard = Integer.parseInt(amountCreditCardNumberInfo);

                Customers[i] = new Customer(id, name, address, distance, new Vector<OrderReceipt>(), creditCardNumber, amountCreditCard);
                JsonArray ListOrders = CurrentCustomer.getAsJsonObject().getAsJsonArray("orderSchedule");
                for (int j = 0; j < ListOrders.size(); j++) {

                }
                //continue implement TODO


                //
                ConcurrentHashMap<BookOrderEvent, Integer> Orders = new ConcurrentHashMap<>();
                Orders.put(new BookOrderEvent(Customers[i], "Harry Poter", 1), 1);
                apiServices[i] = new APIService(Customers[i].getName(), Orders);
            }



        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ///// Execution

        Inventory.getInstance().load(BooksInfo);
        ResourcesHolder.getInstance().load(VehiclesInfo);
        MicroService[] Workers = MainHelper.initiateWorkers(SellingServices, InventoryServices, LogisticServices, ResourceServices, apiServices);

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
