package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
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

	private ResourcesHolder Resources; //Reference to the resources of the store

	public ResourceService(String name) {
		super(name);
		Resources = ResourcesHolder.getInstance();
	}

	@Override
	protected void initialize() {
		//Subscribing to take vehicle
		subscribeEvent(CheckAvailabilityVehicle.class, message -> {
			Future<DeliveryVehicle> FutureVehicle = Resources.acquireVehicle();
			complete(message, FutureVehicle); // Vehicle
		});
		//Subscribing to release vehicle
		subscribeEvent(ReleaseVehicle.class, message-> {
			Resources.releaseVehicle(message.getVehicle());
		});
		//Subscribing to know when time ends
		subscribeBroadcast(TerminateBroadcast.class, message -> {
		    Resources.load(null);
            terminate();
		});
		
	}

}
