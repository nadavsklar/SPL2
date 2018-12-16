package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class DeliveryEvent implements Event {

    private String Address; //Address for the delivery
    private int distance; //Distance between address and the store

    //Constructor
    public DeliveryEvent(String Address, int distance){
        this.Address = Address;
        this.distance = distance;
    }

    //Getters
    public String getAddress() {
        return Address;
    }
    public int getDistance() {
        return distance;
    }
}
