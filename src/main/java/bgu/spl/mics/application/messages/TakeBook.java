package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class TakeBook implements Event {

    private String book; //Book title

    //Constructor
    public TakeBook(String book) {
        this.book = book;
    }

    //Getter
    public String getBook() { return this.book; }
}
