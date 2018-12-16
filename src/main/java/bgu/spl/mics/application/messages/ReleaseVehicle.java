package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

public class ReleaseVehicle implements Event {

    private DeliveryVehicle Vehicle; //Vehicle which is released

    //Constructor
    public ReleaseVehicle(DeliveryVehicle Vehicle){
        this.Vehicle = Vehicle;
    }

    //Getter
    public DeliveryVehicle getVehicle() {
        return Vehicle;
    }
}

