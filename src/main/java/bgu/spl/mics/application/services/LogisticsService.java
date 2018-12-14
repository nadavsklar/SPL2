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
		subscribeEvent(DeliveryEvent.class, message -> {
			CheckAvailabilityVehicle check = new CheckAvailabilityVehicle();
			System.out.println(getName() + " is sending check availability event");
			Future<DeliveryVehicle> Result = sendEvent(check);
			DeliveryVehicle Vehicle = Result.get();
			if(Vehicle != null)
				System.out.println(this.getName() + ", " + Vehicle.getLicense() + ", " + Vehicle.getSpeed());
			Vehicle.deliver(message.getAddress(), message.getDistance());
			ReleaseVehicle release = new ReleaseVehicle(Vehicle);
			System.out.println(getName() + " is sending release event ");
			sendEvent(release);
		});

		subscribeBroadcast(TerminateBroadcast.class, message -> {
            System.out.println(getName() + " is terminating ");

            terminate();
		});
		
	}

}