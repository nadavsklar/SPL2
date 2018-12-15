package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailabilityVehicle;
import bgu.spl.mics.application.messages.ReleaseVehicle;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.*;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourcesHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService{

	private ResourcesHolder Resources;

	public ResourceService(String name) {
		super(name);
		Resources = ResourcesHolder.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeEvent(CheckAvailabilityVehicle.class, message ->{
            //System.out.println(getName() + " aquiring vehicle ");
			DeliveryVehicle Vehicle = Resources.acquireVehicle().get();
			complete(message, Vehicle);
		});
		subscribeEvent(ReleaseVehicle.class, message->{
		    //System.out.println(getName() + " releasing vehicle ");
			Resources.releaseVehicle(message.getVehicle());
		});

		subscribeBroadcast(TerminateBroadcast.class, message -> {
            //System.out.println(getName() + " is terminating ");
            terminate();
		});
		
	}

}
