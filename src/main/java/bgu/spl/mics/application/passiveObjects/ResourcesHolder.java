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

	private ConcurrentLinkedQueue<DeliveryVehicle> availableVehicles;
	private Semaphore sem;
	private ConcurrentLinkedQueue<Future> notResolved;

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
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	public Future<DeliveryVehicle> acquireVehicle() {
        /*Future<DeliveryVehicle> dv = new Future<>();
        if (sem.tryAcquire())  // there is ready car
            dv.resolve(availableVehicles.poll());
        else
            notResolved.add(dv);
        return dv;*/
        /*try {
            sem.acquire();
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        System.out.println("Resource holder aquired");
        Future<DeliveryVehicle> dv = new Future<>();
        dv.resolve(availableVehicles.poll());
        return dv;*/

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
        /*if (!notResolved.isEmpty()) {
            Future<DeliveryVehicle> dv = notResolved.poll();
            dv.resolve(vehicle);
        }
        else {
            sem.release();
            availableVehicles.add(vehicle);
        }*/
        //System.out.println("Resource holder realesed");
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
        availableVehicles.clear();
		for (int i = 0; i < vehicles.length; i++)
            availableVehicles.add(vehicles[i]);
		sem = new Semaphore(vehicles.length);
	}

}
