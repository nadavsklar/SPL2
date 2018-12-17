package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.MicroService;

public class InitServiceEvent implements Event {

    private MicroService Worker;

    //Constructor
    public InitServiceEvent(MicroService Worker){this.Worker = Worker;}

    //Getter
    public MicroService getWorker() { return Worker;}

}
