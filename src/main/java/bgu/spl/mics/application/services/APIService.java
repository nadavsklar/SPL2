package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class APIService extends MicroService{

	private ConcurrentHashMap<BookOrderEvent, Integer> Orders;

	public APIService(String name, ConcurrentHashMap<BookOrderEvent, Integer> Orders) {
		super(name);
		this.Orders = Orders;
	}

	@Override
	protected void initialize() {
	    System.out.println(getName() + " Has initiated");

		subscribeBroadcast(TickBroadcast.class, message -> {
		    System.out.println(getName() + " Has received broadcast " + message.getClass());

		    Integer currentTimeTick = message.getCurrentTick();
		    Vector<OrderReceipt> currentReceipts = new Vector<>();
		    for (BookOrderEvent bookOrderEvent : Orders.keySet()) {
		        if (currentTimeTick.equals(Orders.get(bookOrderEvent))) {
		            System.out.println(getName() + " is sending book order event" );
		            OrderReceipt currentResult = (OrderReceipt)sendEvent(bookOrderEvent).get();
		            System.out.println(getName() + " has received Order receipt");
		            if (currentResult != null) {
                        currentReceipts.add(currentResult);
                        // TIME TICK
                        String address = bookOrderEvent.getCustomer().getAddress();
                        int distance = bookOrderEvent.getCustomer().getDistance();
                        System.out.println(getName() + " sending delivery event");
                        sendEvent(new DeliveryEvent(address, distance));
                    }
                }
            }

		});

		subscribeBroadcast(TerminateBroadcast.class, message -> {
		    terminate();
        });
	}

}
