package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.CheckAvailabilityBook;
import bgu.spl.mics.application.messages.TakeBook;
import bgu.spl.mics.application.messages.TerminateBroadcast;
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

	public SellingService(String name) {
		super(name);
		MoneyRegister = MoneyRegister.getInstance();
	}



	@Override
	protected void initialize() {

		subscribeEvent(BookOrderEvent.class, message ->{
            OrderReceipt receipt = new OrderReceipt();
            receipt.setProccesTick(TimeService.getCurrentTick());
            Future<Integer> price = sendEvent(new CheckAvailabilityBook(message.getBookTitle()));
            int priceValue = price.get();
			if (priceValue >= 0 && message.getCustomer().getAvailableCreditAmount() >= priceValue) {
                    sendEvent(new TakeBook(message.getBookTitle()));
                    receipt.setBookTitle(message.getBookTitle());
                    receipt.setCustomerId(message.getCustomer().getId());
                    receipt.setPrice(price.get());
                    receipt.setSeller(getName());
                    receipt.setOrderId(OrdersId.getCurrentOrderId());
                    OrdersId.nextOrder();
                    MoneyRegister.file(receipt);
                    MoneyRegister.chargeCreditCard(message.getCustomer(), receipt.getPrice());
                    receipt.setIssuedTick(TimeService.getCurrentTick());
                    complete(message, receipt);
			}
            else {
                complete(message, OrderResult.NOT_IN_STOCK);
            }
		});

        subscribeBroadcast(TerminateBroadcast.class, message -> {
            terminate();
        });
		
	}

}
