package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailabilityVehicle;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.ReleaseVehicle;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.*;

/**
 * Logistic service in charge of delivering books that have been purchased to customers.
 * Handles {@link DeliveryEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LogisticsService extends MicroService {

	public LogisticsService(String name) {
		super(name);
	}

	@Override
	protected void initialize() {
		//Subscribing to deliver the book
		subscribeEvent(DeliveryEvent.class, message -> {
			//Sending event which take vehicle
			Future<Future<DeliveryVehicle>> FutureVehicle = sendEvent(new CheckAvailabilityVehicle());
			Object var;
			if (FutureVehicle != null) {
				var = FutureVehicle.get();
				if (var instanceof DeliveryVehicle) {
					DeliveryVehicle Vehicle = FutureVehicle.get().get();
					Vehicle.deliver(message.getAddress(), message.getDistance());
					//Sending event which release vehicle
					sendEvent(new ReleaseVehicle(Vehicle));
				}
				complete(message, true);
			}
		});

		//Subscribing to know when time ends
		subscribeBroadcast(TerminateBroadcast.class, message -> {
			terminate();
		});
		
	}

}