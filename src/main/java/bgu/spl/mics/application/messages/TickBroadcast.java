package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {

    private Integer currentTick;

    public TickBroadcast(Integer currentTick) {
        this.currentTick = currentTick;
    }

    public Integer getCurrentTick() { return currentTick; }
}
