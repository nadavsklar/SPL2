package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;


public class BookOrderEvent implements Event {

    private Customer Customer; //Reference to the customer which made the order
    private String BookTitle; //Book title
    private int timeTick; //Tick in which the order needs to be done

    //Constructor
    public BookOrderEvent(Customer Customer, String BookTitle, int timeTick){
        this.Customer = Customer;
        this.BookTitle = BookTitle;
        this.timeTick = timeTick;
    }

    //Getters
    public Customer getCustomer() {
        return Customer;
    }
    public String getBookTitle() { return this.BookTitle; }
    public int getTimeTick() { return timeTick; }

}
