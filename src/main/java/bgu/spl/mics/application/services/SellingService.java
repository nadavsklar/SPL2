package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.*;

/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService{

	private MoneyRegister MoneyRegister;

	public SellingService() {
		super("Change_This_Name");
		MoneyRegister = MoneyRegister.getInstance();
	}



	@Override
	protected void initialize() {
		subscribeEvent(BookOrderEvent.class, message ->{
			//Create Receipt
			MoneyRegister.file(message.getReceipt());
			MoneyRegister.chargeCreditCard(message.getCustomer(), message.getReceipt().getPrice());
			complete(message, MoneyRegister.getTotalEarnings());
		});
		
	}

}
