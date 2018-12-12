package bgu.spl.mics.application.messages;

import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import bgu.spl.mics.Event;

public class DeliveryEvent implements Event {

    private String Address;
    private int distance;

    public DeliveryEvent(String Address, int distance){
        this.Address = Address;
        this.distance = distance;
    }

    public final Future<DeliveryVehicle> getVehicle(){
        ResourcesHolder Resources = ResourcesHolder.getInstance();
        return Resources.acquireVehicle();
    }

    public String getAddress() {
        return Address;
    }

    public int getDistance() {
        return distance;
    }
}
