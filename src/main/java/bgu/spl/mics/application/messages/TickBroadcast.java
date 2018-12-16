package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {

    private Integer currentTick; //the current tick in the system

    //Constructor
    public TickBroadcast(Integer currentTick) {
        this.currentTick = currentTick;
    }

    //Getter
    public Integer getCurrentTick() { return currentTick; }
}
