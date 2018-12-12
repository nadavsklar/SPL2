package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class TakeBook implements Event {

    private String book;

    public TakeBook(String book) {
        this.book = book;
    }

    public String getBook() { return this.book; }
}
