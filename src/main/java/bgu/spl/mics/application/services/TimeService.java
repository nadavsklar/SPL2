package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.InitServiceEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{

	private int speed; //Increasing tick every @speed milliseconds
	private int duration; //Duration of the process
	private static int currentTick; //Current tick in the system
	private int numberOfServicesRegistered;
	private int numberOfServicesExisted;
	private Timer SystemTimer; //Java timer

	//Constructor
	public TimeService(String name, int speed, int duration, int numberOfServicesExisted) {
		super(name);
		this.speed = speed;
		this.duration = duration;
		this.SystemTimer = new Timer();
		this.currentTick = 1;
		this.numberOfServicesExisted = numberOfServicesExisted;
		this.numberOfServicesRegistered = 0;
	}

	@Override
	protected void initialize() {
	    try {
            Thread.currentThread().sleep(100);
        }
	    catch (InterruptedException ie){
	        ie.printStackTrace();
        }
        SystemTimer.schedule(new TimerTask() {
        	@Override
			public void run() {
        		if(numberOfServicesExisted == numberOfServicesRegistered) {
        			//Time has ended!
					if (duration == currentTick) {
						SystemTimer.cancel(); //Canceling timer
						sendBroadcast(new TerminateBroadcast()); //Terminate all other services
					}
					//Time has not ended
					else {
						TickBroadcast TickBroadcast = new TickBroadcast(currentTick); //Creating new tick broadcast
						sendBroadcast(TickBroadcast); //Sending tick to other services
						currentTick++; //Increasing tick
					}
        		}
        	}
        	}, 0, speed);
		//Subscribing to know when time ends
		subscribeBroadcast(TerminateBroadcast.class, message -> {
            terminate();
        });
		//Subscribing to know when Services are registered
		subscribeEvent(InitServiceEvent.class, message -> {
			this.numberOfServicesRegistered++;
		});
	}

	//Getters
	public int getSpeed() {
		return speed;
	}
	public int getDuration() {
		return duration;
	}
	public static int getCurrentTick() {
		return currentTick;
	}
	public Timer getSystemTimer() {
		return SystemTimer;
	}
}
