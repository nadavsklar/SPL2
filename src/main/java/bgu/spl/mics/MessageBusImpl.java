package bgu.spl.mics;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private ConcurrentHashMap<Event, Future> Results;
	private ConcurrentHashMap<Class<? extends Event>, List<MicroService>> Events;
	private ConcurrentHashMap<Class<? extends Broadcast>, List<MicroService>> Brodcasts;
	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> Missions;
	private int roundRobinIndex;

	private static class MessageBusHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl() {
		Results = new ConcurrentHashMap<>();
		Events = new ConcurrentHashMap<>();
        Brodcasts = new ConcurrentHashMap<>();
        Missions = new ConcurrentHashMap<>();
        roundRobinIndex = 0;
	}


	public static MessageBusImpl getInstance() {
		return MessageBusHolder.instance;
	}


	@Override
	public synchronized <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
	    if (!Events.containsKey(type))
	        Events.put(type, new LinkedList<>());
		Events.get(type).add(m);
		//notifyAll();
		//System.out.println("NOTIFIED! - subscribe event " + m.toString() + " type : " + type.toString());
	}

	@Override
	public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
	    if (!Brodcasts.containsKey(type))
            Brodcasts.put(type, new LinkedList<>());
	    Brodcasts.get(type).add(m);
	    //notifyAll();
        //System.out.println("NOTIFIED! - subscribe broadcast " + m.toString() + " type : " + type.toString());
    }

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub
		Future<T> F = Results.get(e);
		F.resolve(result);
		Results.remove(e);
	}

	@Override
	public synchronized void sendBroadcast(Broadcast b) {
	    try {
	        while (!Brodcasts.containsKey(b.getClass())) {
	            //System.out.println(" WAIT send broadcast  " + b.toString());
                wait();
            }
        }
        catch (InterruptedException ie) {
	        ie.printStackTrace();
        }
		List<MicroService> Services = Brodcasts.get(b.getClass());
		for (MicroService m : Services)
			Missions.get(m).add(b);
		notifyAll();
		//System.out.println(" NOTIFIED send broadcast " + b.toString());
	}

	
	@Override
	public synchronized <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
        try {
            while (!Events.containsKey(e.getClass())) {
                //System.out.println(" WAIT send event  " + e.toString());
                wait();
            }
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }
		List<MicroService> Services = Events.get(e.getClass());
        if (roundRobinIndex >= Services.size())
            roundRobinIndex = 0;
		MicroService m = Services.get(roundRobinIndex);
		roundRobinIndex++;
		Missions.get(m).add(e);
		Results.put(e, new Future());
		notifyAll();
		//System.out.println(" NOTIFIED send event   " + e.toString());
		return Results.get(e);
	}

	@Override
	public void register(MicroService m) {
		Missions.put(m, new ConcurrentLinkedQueue<>());
		//System.out.println(m.toString() + " has registered");
	}

	@Override
	public void unregister(MicroService m) {
		Queue<Message> mEvents = Missions.get(m);
		mEvents.clear();
		Missions.remove(m);
		for(int i = 0; i < Events.size(); i++) {
			List<MicroService> serviceList = Events.get(i);
			if(serviceList.contains(m))
				serviceList.remove(m);
		}

		for(int i = 0; i < Brodcasts.size(); i++) {
			List<MicroService> serviceList = Brodcasts.get(i);
			if(serviceList.contains(m))
				serviceList.remove(m);
		}
	}

	@Override
	public synchronized Message awaitMessage(MicroService m) throws InterruptedException {
	    try {
            Queue<Message> listOfEvents = Missions.get(m);
            while (listOfEvents.isEmpty())
                wait();
            Message toReturn = listOfEvents.poll();
            notifyAll();
            return toReturn;
        }
        catch (InterruptedException ie) {
	        throw new InterruptedException();
	    }
	}
}
