package bgu.spl.mics.application.services;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailabilityBook;
import bgu.spl.mics.application.messages.InitServiceEvent;
import bgu.spl.mics.application.messages.TakeBook;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderResult;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

/**
 * InventoryService is in charge of the book inventory and stock.
 * Holds a reference to the {@link Inventory} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class InventoryService extends MicroService{

	private Inventory inventory; //Reference to the inventory in the store

	//Constructor
	public InventoryService(String name) {
		super(name);
		inventory = Inventory.getInstance();
	}

	@Override
	protected void initialize() {
		sendEvent(new InitServiceEvent(this));
		//Subscribing to check availability of the book
	    subscribeEvent(CheckAvailabilityBook.class, iv -> {
	        int price = inventory.checkAvailabiltyAndGetPrice(iv.getBook());
	        complete(iv, price);
        });

	    //Subscribing to take the book
        subscribeEvent(TakeBook.class, iv -> {
            OrderResult result = inventory.take(iv.getBook());
            complete(iv, result);
        });

		//Subscribing to know when time ends
		subscribeBroadcast(TerminateBroadcast.class, message -> {
            terminate();
		});

	}

}
