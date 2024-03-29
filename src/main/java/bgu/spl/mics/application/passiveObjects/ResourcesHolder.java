package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.Future;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
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

	private ConcurrentLinkedQueue<DeliveryVehicle> availableVehicles; //All available vehicles
	private Semaphore sem; //Semaphore
	private ConcurrentLinkedQueue<Future> notResolved; //All not resolved results

	/**
     * Retrieves the single instance of this class.
     */
	private static class ResourcesHolderInstance {
		private static ResourcesHolder instance = new ResourcesHolder();
	}

	private ResourcesHolder() {
		availableVehicles = new ConcurrentLinkedQueue<>();
		notResolved = new ConcurrentLinkedQueue<>();
	}


	public static ResourcesHolder getInstance() {
		return ResourcesHolderInstance.instance;
	}
	
	/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>

	 turn 	{@link Future<DeliveryVehicle>} object which will resolve to a
     * 			{@link DeliveryVehicle} when completed.   
     */
	public Future<DeliveryVehicle> acquireVehicle() {
        Future<DeliveryVehicle> dv = new Future<>();
        if (sem.tryAcquire())
            dv.resolve(availableVehicles.poll());
        else
            notResolved.add(dv);
        return dv;
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle(DeliveryVehicle vehicle) {
        if (!notResolved.isEmpty()) {
            Future<DeliveryVehicle> dv = notResolved.poll();
            dv.resolve(vehicle);
        }
        else {
            sem.release();
            availableVehicles.add(vehicle);
        }
	}
	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles) {
	    if (vehicles != null) {
            availableVehicles.clear();
            for (int i = 0; i < vehicles.length; i++)
                availableVehicles.add(vehicles[i]);
            sem = new Semaphore(vehicles.length);
        }
        else {
            while (!notResolved.isEmpty())
            	if (notResolved.peek() != null)
                	notResolved.poll().resolve(OrderResult.NOT_IN_STOCK);
            	else
            		notResolved.poll();
        }
	}

}
