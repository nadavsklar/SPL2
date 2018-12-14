package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.services.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Vector;

public class MainHelper {

    public static BookInventoryInfo[] InitBooks(JsonObject jsonObject, BookInventoryInfo[] BooksInfo){
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
        return BooksInfo;
    }

    public static DeliveryVehicle[] InitVehicles(JsonObject jsonObject, DeliveryVehicle[] VehiclesInfo){
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
        return VehiclesInfo;
    }

    public static TimeService InitTimerService(JsonObject jsonObject, TimeService TimerService){
        JsonElement Time = jsonObject.getAsJsonObject("services").get("time");
        String TimeInfo = Time.toString();
        String speedInfo = TimeInfo.substring(TimeInfo.indexOf(':') + 1, TimeInfo.indexOf(','));
        TimeInfo = TimeInfo.substring(TimeInfo.indexOf(',') + 1);
        String durationInfo = TimeInfo.substring(TimeInfo.indexOf(':') + 1, TimeInfo.indexOf('}'));
        int speed = Integer.parseInt(speedInfo);
        int duration = Integer.parseInt(durationInfo);
        TimerService = new TimeService("Timer", speed, duration);
        return TimerService;
    }

    public static SellingService[] InitSellingServices(JsonObject jsonObject, SellingService[] SellingServices){
        JsonElement SellingAmount = jsonObject.getAsJsonObject("services").get("selling");
        int size = Integer.parseInt(SellingAmount.toString());
        SellingServices = new SellingService[size];
        for(int i = 0; i < SellingServices.length; i++)
            SellingServices[i] = new SellingService("Seller " + i);
        return SellingServices;
    }

    public static InventoryService[] InitInventoryServices(JsonObject jsonObject, InventoryService[] InventoryServices){
        JsonElement InventoryAmount = jsonObject.getAsJsonObject("services").get("inventoryService");
        int size = Integer.parseInt(InventoryAmount.toString());
        InventoryServices = new InventoryService[size];
        for(int i = 0; i < InventoryServices.length; i++)
            InventoryServices[i] = new InventoryService("Inventory Handler " + i);
        return InventoryServices;
    }

    public static LogisticsService[] InitLogisticServices(JsonObject jsonObject, LogisticsService[] LogisticServices){
        JsonElement LogisticAmount = jsonObject.getAsJsonObject("services").get("logistics");
        int size = Integer.parseInt(LogisticAmount.toString());
        LogisticServices = new LogisticsService[size];
        for(int i = 0; i < LogisticServices.length; i++)
            LogisticServices[i] = new LogisticsService("Logistic Handler " + i);
        return LogisticServices;
    }

    public static ResourceService[] InitResourceServices(JsonObject jsonObject, ResourceService[] ResourceServices){
        JsonElement ResourceAmount = jsonObject.getAsJsonObject("services").get("resourcesService");
        int size = Integer.parseInt(ResourceAmount.toString());
        ResourceServices = new ResourceService[size];
        for(int i = 0; i < ResourceServices.length; i++)
            ResourceServices[i] = new ResourceService("Resource Handler " + i);
        return ResourceServices;
    }

    public static Object[][] InitAPIServices(JsonObject jsonObject, APIService[] APIServices, Customer[] Customers){
        JsonArray CustomersArray = jsonObject.getAsJsonObject("services").getAsJsonArray("customers");
        int size = CustomersArray.size();
        Customers = new Customer[size];
        APIServices = new APIService[size];
        for(int i = 0; i < Customers.length; i++){
            JsonElement CurrentCustomer = CustomersArray.get(i);
            String idInfo = CurrentCustomer.getAsJsonObject().get("id").toString();
            int id = Integer.parseInt(idInfo);
            String name = CurrentCustomer.getAsJsonObject().get("name").toString();
            name = name.substring(1, name.length() - 1);
            String address = CurrentCustomer.getAsJsonObject().get("address").toString();
            address = address.substring(1, address.length() - 1);
            String distanceInfo = CurrentCustomer.getAsJsonObject().get("distance").toString();
            int distance = Integer.parseInt(distanceInfo);
            String creditCardNumberInfo = CurrentCustomer.getAsJsonObject().get("creditCard").getAsJsonObject().get("number").toString();
            int creditCardNumber = Integer.parseInt(creditCardNumberInfo);
            String amountCreditCardNumberInfo = CurrentCustomer.getAsJsonObject().get("creditCard").getAsJsonObject().get("amount").toString();
            int amountCreditCard = Integer.parseInt(amountCreditCardNumberInfo);

            Customers[i] = new Customer(id, name, address, distance, new Vector<OrderReceipt>(), creditCardNumber, amountCreditCard);
            Vector<BookOrderEvent> CustomerEvents = new Vector<>();

            JsonArray ListOrders = CurrentCustomer.getAsJsonObject().getAsJsonArray("orderSchedule");
            for (int j = 0; j < ListOrders.size(); j++) {
                JsonElement CurrentOrder = ListOrders.get(j);
                String BookTitle = CurrentOrder.getAsJsonObject().get("bookTitle").toString();
                BookTitle = BookTitle.substring(1, BookTitle.length() - 1);
                String tickInfo = CurrentOrder.getAsJsonObject().get("tick").toString();
                int tick = Integer.parseInt(tickInfo);
                BookOrderEvent CurrentEvent = new BookOrderEvent(Customers[i], BookTitle, tick);
                CustomerEvents.add(CurrentEvent);
            }
            APIServices[i] = new APIService("API Service " + i, CustomerEvents);
        }
        Object[][] toReturn = new Object[2][size];
        toReturn[0] = APIServices;
        toReturn[1] = Customers;
        return toReturn;
    }

    public static MicroService[] initiateWorkers(SellingService[] SellingServices, InventoryService[] InventoryServices, LogisticsService[] LogisticServices, ResourceService[] ResourceServices, APIService[] apiServices) {
        int numOfWorkers = SellingServices.length;
        numOfWorkers += InventoryServices.length;
        numOfWorkers += LogisticServices.length;
        numOfWorkers += ResourceServices.length;
        numOfWorkers += apiServices.length;

        MicroService[] Workers = new MicroService[numOfWorkers];
        int currentIndex = 0;
        for (int i = 0; i < SellingServices.length; i++) {
            Workers[currentIndex] = SellingServices[i];
            currentIndex++;
        }
        for (int i = 0; i < InventoryServices.length; i++) {
            Workers[currentIndex] = InventoryServices[i];
            currentIndex++;
        }
        for (int i = 0; i < LogisticServices.length; i++) {
            Workers[currentIndex] = LogisticServices[i];
            currentIndex++;
        }
        for (int i = 0; i < ResourceServices.length; i++) {
            Workers[currentIndex] = ResourceServices[i];
            currentIndex++;
        }
        for (int i = 0; i < apiServices.length; i++) {
            Workers[currentIndex] = apiServices[i];
            currentIndex++;
        }

        return Workers;
    }

    public static Thread[] initiateThreads(MicroService[] Workers) {
        Thread[] WorkersThreads = new Thread[Workers.length];
        for (int i = 0; i < WorkersThreads.length; i++)
            WorkersThreads[i] = new Thread(Workers[i]);
        return WorkersThreads;
    }

    public static void printOutputs(Customer[] Customers, String customers, String books, String orderReceipts, String moveyRegister) {
        HashMap<Integer, Customer> customerHashMap = new HashMap<>();
        for (int i = 0; i < Customers.length; i++)
            customerHashMap.put(Customers[i].getId(), Customers[i]);
        Printer.SerializablePrinter(customerHashMap, customers);
        Inventory.getInstance().printInventoryToFile(books);
        MoneyRegister.getInstance().printOrderReceipts(orderReceipts);
        Printer.SerializablePrinter(MoneyRegister.getInstance(), moveyRegister);
    }
}
