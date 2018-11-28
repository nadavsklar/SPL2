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

	private ConcurrentHashMap<Event, List<MicroService>> Events;
	private ConcurrentHashMap<Broadcast, List<MicroService>> Brodcasts;
	private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> Missions;

	private static class MessageBusHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl() {

	}

	public static MessageBusImpl getInstance() {
		return MessageBusHolder.instance;
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		Events.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		Brodcasts.get(type).add(m);
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub
		List<MicroService> Services = Events.get(b);
		for (MicroService m : Services){
			//m.something
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		List<MicroService> Services = Events.get(e);
		MicroService m = Services.get(0);
		Missions.get(m).add(e);
		return null;
	}

	@Override
	public void register(MicroService m) {
		Missions.put(m, new ConcurrentLinkedQueue<Message>());
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
		// TODO Auto-generated method stub
		//return null;
		try {
			Queue<Message> listOfEvents = Missions.get(m);
			while(listOfEvents.isEmpty());
			if(Thread.currentThread().isInterrupted())
				throw new InterruptedException();
			Message toReturn =  listOfEvents.peek();
			return toReturn;
		}
		catch (NullPointerException e) {
			throw new IllegalStateException();
		}








	}



}
