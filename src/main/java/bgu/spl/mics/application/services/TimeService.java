package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
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

	private int speed;
	private int duration;
	private int currentTick;
	private Timer SystemTimer;

	public TimeService(String name, int speed, int duration) {
		super(name);
		this.speed = speed;
		this.duration = duration;
		SystemTimer = new Timer();
		currentTick = 1;
	}

	@Override
	protected void initialize() {
	    System.out.println(getName() + " has initiated");
		SystemTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
                if (duration == currentTick) {
                    System.out.println(" timer ended, sending terminate");
                    SystemTimer.cancel();
                    sendBroadcast(new TerminateBroadcast());
                    System.out.println("Timer is terminating");
                    terminate();
                }
                else {
                    System.out.println(getName() + " is sending tick broadcast " + currentTick);
                    TickBroadcast TickBroadcast = new TickBroadcast(currentTick);
                    sendBroadcast(TickBroadcast);
                    System.out.println("broadcast number " + currentTick + " sent ");
                    currentTick++;
                }
			}
		}, speed, duration * speed);
	}

	public int getSpeed() {
		return speed;
	}

	public int getDuration() {
		return duration;
	}

	public int getCurrentTick() {
		return currentTick;
	}

	public Timer getSystemTimer() {
		return SystemTimer;
	}
}
