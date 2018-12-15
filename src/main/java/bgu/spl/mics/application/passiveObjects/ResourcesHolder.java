package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.Future;
import java.util.Vector;
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

	private Vector<DeliveryVehicle> availableVehicles;
	private Semaphore sem;

	/**
     * Retrieves the single instance of this class.
     */
	private static class ResourcesHolderInstance {
		private static ResourcesHolder instance = new ResourcesHolder();
	}

	private ResourcesHolder() {
		availableVehicles = new Vector<>();
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
	    try {
            sem.acquire();
        }
        catch (InterruptedException ie) {
	        ie.printStackTrace();
        }
        Future<DeliveryVehicle> dv = new Future<>();
	    dv.resolve(availableVehicles.get(0));
	    availableVehicles.remove(availableVehicles.get(0));
		return dv;
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle(DeliveryVehicle vehicle) {
        sem.release();
        availableVehicles.add(vehicle);
	}
	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles) {
        availableVehicles.clear();
		for (int i = 0; i < vehicles.length; i++)
            availableVehicles.add(vehicles[i]);
		sem = new Semaphore(vehicles.length);
	}

}
