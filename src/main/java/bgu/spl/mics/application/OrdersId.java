package bgu.spl.mics.application;

//Class which is responsible for the id's of orders
public class OrdersId {

    private static int currentOrderId = 0; //Current id of orders in the store

    public static int getCurrentOrderId() { return currentOrderId; } //Get the current id id orders in the store
    public static void nextOrder() { currentOrderId++; } //Set the next id of orders in the store
}
