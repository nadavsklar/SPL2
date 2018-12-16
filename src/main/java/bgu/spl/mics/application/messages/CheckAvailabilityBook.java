package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class CheckAvailabilityBook implements Event {

    private String book; //the book title

    //Constructor
    public CheckAvailabilityBook(String book) {
        this.book = book;
    }

    //Getter
    public String getBook() { return this.book; }

}
