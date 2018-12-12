package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

import java.util.Vector;

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

	private Vector<BookOrderEvent> Orders;
	public APIService(String name, Vector<BookOrderEvent> Orders) {
		super(name);
		this.Orders = Orders;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, message -> {

		});

		Future<OrderReceipt> Order = sendEvent(Orders.firstElement());

	}

}
