package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.Future;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */

public class ResourcesHolder {

	private ConcurrentHashMap<DeliveryVehicle, Boolean> Vehicles;
	private Semaphore sem;

	/**
     * Retrieves the single instance of this class.
     */
	private static class ResourcesHolderInstance {
		private static ResourcesHolder instance = new ResourcesHolder();
	}

	private ResourcesHolder() {
		Vehicles = new ConcurrentHashMap<>();
	}


	public static ResourcesHolder getInstance() {
		return ResourcesHolderInstance.instance;
	}
	
	/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	public Future<DeliveryVehicle> acquireVehicle() {
		/*Future<DeliveryVehicle> dvF = new Future<>();
		if (sem.tryAcquire()) {
			Iterator<DeliveryVehicle> iter = Vehicles.keySet().iterator();
			Boolean found = false;
			while (iter.hasNext() & !found) {
				DeliveryVehicle dv = iter.next();
				if (Vehicles.get(dv)) {
					Vehicles.replace(dv, false);
					found = true;
					dvF.resolve(dv);
				}
			}
		}
		else {
			dvF.get(Vehicles.get())
		}*/
		return null;
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle(DeliveryVehicle vehicle) {
		sem.release(1);
		Vehicles.replace(vehicle, false);
	}
	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles) {
		for(int i = 0; i < vehicles.length; i++)
			Vehicles.put(vehicles[i], true);
		sem = new Semaphore(vehicles.length);
	}

}
