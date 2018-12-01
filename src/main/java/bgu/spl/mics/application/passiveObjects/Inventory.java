package bgu.spl.mics.application.passiveObjects;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Passive data-object representing the store inventory.
 * It holds a collection of {@link BookInventoryInfo} for all the
 * books in the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {

    private HashMap<String, Integer> amountsInInventory;
    private HashMap<String, Integer> prices;

	/**
     * Retrieves the single instance of this class.
     */
	private static class InventoryHolder {
	    private static Inventory instance = new Inventory();
    }

    private Inventory() {

    }

	public static Inventory getInstance() {
        return InventoryHolder.instance;
	}
	
	/**
     * Initializes the store inventory. This method adds all the items given to the store
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (BookInventoryInfo[ ] inventory ) {
        this.amountsInInventory = new HashMap<>();
        this.prices = new HashMap<>();
        for (int i = 0; i < inventory.length; i++) {
            this.prices.put(inventory[i].getBookTitle(), inventory[i].getPrice());
            this.amountsInInventory.put(inventory[i].getBookTitle(), inventory[i].getAmountInInventory());
        }
    }
	
	/**
     * Attempts to take one book from the store.
     * <p>
     * @param book 		Name of the book to take from the store
     * @return 	an {@link Enum} with options NOT_IN_STOCK and SUCCESSFULLY_TAKEN.
     * 			The first should not change the state of the inventory while the 
     * 			second should reduce by one the number of books of the desired type.
     */
	public OrderResult take (String book) {
	    try {
            if (amountsInInventory.get(book) > 0) {
                int oldAmount = amountsInInventory.get(book);
                amountsInInventory.replace(book, oldAmount, oldAmount - 1);
                return OrderResult.SUCCESSFULLY_TAKEN;
            } else
                return OrderResult.NOT_IN_STOCK;
        }
        catch (Exception e) {
	        return null;
        }
	}
	
	
	
	/**
     * Checks if a certain book is available in the inventory.
     * <p>
     * @param book 		Name of the book.
     * @return the price of the book if it is available, -1 otherwise.
     */
	public int checkAvailabiltyAndGetPrice(String book) {
        try {
            if (amountsInInventory.get(book) > 0)
                return prices.get(book);
            return -1;
        }
        catch (Exception e) {
            return -1;
        }
    }
	
	/**
     * 
     * <p>
     * Prints to a file name @filename a serialized object HashMap<String,Integer> which is a Map of all the books in the inventory. The keys of the Map (type {@link String})
     * should be the titles of the books while the values (type {@link Integer}) should be
     * their respective available amount in the inventory. 
     * This method is called by the main method in order to generate the output.
     */
	public void printInventoryToFile(String filename){
		try {
            FileOutputStream outputStream = new FileOutputStream(filename + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(amountsInInventory);
            out.close();
            outputStream.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Error printing file");
        }
    }
}
