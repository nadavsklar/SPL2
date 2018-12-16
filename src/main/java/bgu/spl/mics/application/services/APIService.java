package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.List;
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

	private Vector<BookOrderEvent> Orders; //List of books ordered by the customer
	private Customer Customer; //Customer which is matched to the API Service

    //Constructor
	public APIService(String name, Vector<BookOrderEvent> Orders) {
		super(name);
		this.Orders = Orders;
		Customer = Orders.get(0).getCustomer();
	}

	@Override
	protected void initialize() {
	    //Subscribing to ticks which are sent by the Time Service
		subscribeBroadcast(TickBroadcast.class, message -> {
            List<Future> results = new Vector<>();
		    Integer currentTimeTick = message.getCurrentTick(); //current tick
		    Vector<OrderReceipt> currentReceipts = new Vector<>();
		    //Sending all orders which made by the customer in the current tick
		    for (BookOrderEvent bookOrderEvent : Orders) {
		        if (currentTimeTick.equals(bookOrderEvent.getTimeTick())) {
                    Future currentResult = sendEvent(bookOrderEvent);
                    results.add(currentResult);
                }
            }
            //Getting the results of the orders
            for (int i = 0; i < results.size(); i++) {
                Future currentResult = results.get(i);
                //The order was succeeded
                if (currentResult.get() instanceof OrderReceipt) {
                    OrderReceipt currentReceipt = (OrderReceipt) currentResult.get();
                    currentReceipt.setOrderTick(currentTimeTick);
                    currentReceipts.add(currentReceipt);
                    String address = Customer.getAddress();
                    int distance = Customer.getDistance();
                    //Sending delivery event
                    sendEvent(new DeliveryEvent(address, distance));
                    //Adding all the receipts to the customer
                    Customer.addOrderReceipt(currentReceipts);
                }
            }
		});
        //Subscribing to know when time ends
		subscribeBroadcast(TerminateBroadcast.class, message -> { ;
            terminate();
        });
	}

}
