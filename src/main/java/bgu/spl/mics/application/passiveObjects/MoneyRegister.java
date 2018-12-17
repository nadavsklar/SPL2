package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.application.Printer;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Passive object representing the store finance management. 
 * It should hold a list of receipts issued by the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class MoneyRegister implements Serializable {

	private List<OrderReceipt> Receipts; //All receipts in the store
	private int totalEarnings; //total earnings of the store

	/**
     * Retrieves the single instance of this class.
     */
	private static class MoneyRegisterInstance {
		private static MoneyRegister instance = new MoneyRegister();
	}

	private MoneyRegister() {
		Receipts = new Vector<>();
		totalEarnings = 0;
	}


	public static MoneyRegister getInstance() {
		return MoneyRegisterInstance.instance;
	}
	
	/**
     * Saves an order receipt in the money register.
     * <p>   
     * @param r		The receipt to save in the money register.
     */
	public synchronized void file (OrderReceipt r) {
		Receipts.add(r);
		totalEarnings = totalEarnings + r.getPrice();
	}
	
	/**
     * Retrieves the current total earnings of the store.  
     */
	public int getTotalEarnings() {
		return totalEarnings;
	}
	
	/**
     * Charges the credit card of the customer a certain amount of money.
     * <p>
     * @param amount 	amount to charge
     */
	public void chargeCreditCard(Customer c, int amount) {
		c.setAvailableAmountInCreditCard(amount);
	}
	
	/**
     * Prints to a file named @filename a serialized object List<OrderReceipt> which holds all the order receipts 
     * currently in the MoneyRegister
     * This method is called by the main method in order to generate the output.. 
     */
	public void printOrderReceipts(String filename) {
		Printer.SerializablePrinter(Receipts, filename);
	}

	public List<OrderReceipt> getReceipts() { return this.Receipts; }
}
