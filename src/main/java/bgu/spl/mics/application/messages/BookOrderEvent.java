package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

public class BookOrderEvent implements Event {

    private Customer Customer;
    private String BookTitle;
    private int timeTick;

    public BookOrderEvent(Customer Customer, String BookTitle, int timeTick){
        this.Customer = Customer;
        this.BookTitle = BookTitle;
        this.timeTick = timeTick;
    }

    public Customer getCustomer() {
        return Customer;
    }

    public String getBookTitle() { return this.BookTitle; }

    public int getTimeTick() { return timeTick; }
}
