package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class CheckAvailabilityBook implements Event {

    private String book;

    public CheckAvailabilityBook(String book) {
        this.book = book;
    }

    public String getBook() { return this.book; }

}
