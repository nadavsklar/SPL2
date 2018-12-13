package bgu.spl.mics.application;

import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
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

        BookInventoryInfo[] BooksInfo;
        DeliveryVehicle[] VehiclesInfo;
        TimeService TimerService;
        SellingService[] SellingServices;
        InventoryService[] InventoryServices;
        LogisticsService[] LogisticServices;
        ResourceService[] ResourceServices;
        APIService[] apiServices;
        Customer[] Customers;


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

            //JsonElement Vehicles = jsonObject.getAsJsonArray("initialResources").get(0);
            //LinkedTreeMap<String, JsonArray> V = Vehicles.
            /*VehiclesInfo = new DeliveryVehicle[Vehicles.];

            for(int i = 0; i < Vehicles.size(); i++){
                String vehicleInfo = Vehicles.get(i).toString();
                String licenseInfo = vehicleInfo.substring(vehicleInfo.indexOf(':') + 1, vehicleInfo.indexOf(','));
                vehicleInfo = vehicleInfo.substring(vehicleInfo.indexOf(',') + 1);
                int license = Integer.parseInt(licenseInfo);
                //
                String speedInfo = vehicleInfo.substring(vehicleInfo.indexOf(':') + 1, vehicleInfo.indexOf('}'));
                int speed = Integer.parseInt(speedInfo);
                //
                VehiclesInfo[i] = new DeliveryVehicle(license, speed);
            }*/

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
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
