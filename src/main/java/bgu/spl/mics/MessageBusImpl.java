package bgu.spl.mics;
import java.util.*;
import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private ConcurrentHashMap<Event, Future> Results;
	private ConcurrentHashMap<Class<? extends Event>, BlockingDeque<MicroService>> Events;
	private ConcurrentHashMap<Class<? extends Broadcast>, BlockingDeque<MicroService>> Brodcasts;
	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> Missions;
	private Object lockBrodcast = new Object();
	private Object lockEvents = new Object();

	private static class MessageBusHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl() {
		Results = new ConcurrentHashMap<>();
		Events = new ConcurrentHashMap<>();
		Brodcasts = new ConcurrentHashMap<>();
		Missions = new ConcurrentHashMap<>();
	}


	public static MessageBusImpl getInstance() {
		return MessageBusHolder.instance;
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (lockEvents) {
			if (!Events.containsKey(type))
				Events.put(type, new LinkedBlockingDeque<>());
			Events.get(type).addFirst(m);
            //System.out.println(m.getName() + " added to " + type);
		}
	}


	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (lockBrodcast){
			if (!Brodcasts.containsKey(type))
				Brodcasts.put(type, new LinkedBlockingDeque<>());
			Brodcasts.get(type).add(m);
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		synchronized (lockEvents){
			Future<T> F = Results.get(e);
			F.resolve(result);
		}
	}

	@Override
	public synchronized void sendBroadcast(Broadcast b) {
		synchronized (lockBrodcast) {
			if(Brodcasts.containsKey(b.getClass())){
				BlockingDeque<MicroService> Services = Brodcasts.get(b.getClass());
				for (MicroService m : Services)
					Missions.get(m).add(b);
				notifyAll();
			}
		}
	}


	@Override
	public synchronized <T> Future<T> sendEvent(Event<T> e) {
		synchronized (lockEvents){
			if(Events.containsKey(e.getClass())){
				BlockingDeque<MicroService> Services = Events.get(e.getClass());
				if (!Services.isEmpty()) {
                    MicroService m = Services.removeFirst();
                    //System.out.println(m.getName());
                    Missions.get(m).add(e);
                    Results.put(e, new Future());
                    Services.addLast(m);
                    notifyAll();
                }
			}
			return Results.get(e);
		}
	}

	@Override
	public void register(MicroService m) {
		Missions.put(m, new ConcurrentLinkedQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
		Queue<Message> mEvents = Missions.get(m);
		mEvents.clear();
		Missions.remove(m);
        deleteSubscribes(Events, m);
        deleteSubscribes(Brodcasts, m);
	}

	private void deleteSubscribes(ConcurrentHashMap Map, MicroService m) {
        Iterator it = Map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            BlockingDeque<MicroService> serviceList = (BlockingDeque<MicroService>)pair.getValue();
            if (serviceList.contains(m))
                serviceList.remove(m);
        }
    }

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		synchronized (this){
			try {
				Queue<Message> listOfEvents = Missions.get(m);
				while (listOfEvents.isEmpty())
					wait();
				Message toReturn = listOfEvents.remove();
				notifyAll();
				return toReturn;
			}
			catch (InterruptedException ie) {
				throw new InterruptedException();
			}
		}
	}
}
