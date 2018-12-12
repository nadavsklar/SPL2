package bgu.spl.mics.application.passiveObjects;

public class OrdersId {

    private static int currentOrderId = 0;

    public static int getCurrentOrderId() { return currentOrderId; }
    public static void nextOrder() { currentOrderId++; }
}
