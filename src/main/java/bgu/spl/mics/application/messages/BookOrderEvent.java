package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class BookOrderEvent implements Event {

    private Customer Customer;
    private String BookTitle;
    private int timeTick;
    private AtomicReference<Customer> CustomerBackup;
    private AtomicReference<String> BookTitleBackup;
    private AtomicInteger timeTickBackup;


    public BookOrderEvent(Customer Customer, String BookTitle, int timeTick){
        this.Customer = Customer;
        this.BookTitle = BookTitle;
        this.timeTick = timeTick;
        this.CustomerBackup = new AtomicReference<>(Customer);
        this.BookTitleBackup = new AtomicReference<>(BookTitle);
        this.timeTickBackup = new AtomicInteger(timeTick);
    }

    public Customer getCustomer() {
        return Customer;
    }

    public String getBookTitle() { return this.BookTitle; }

    public int getTimeTick() { return timeTick; }

    public AtomicReference<Customer> getCustomerBackup() {
        return CustomerBackup;
    }

    public AtomicReference<String> getBookTitleBackup() {
        return BookTitleBackup;
    }

    public AtomicInteger getTimeTickBackup() {
        return timeTickBackup;
    }
}
