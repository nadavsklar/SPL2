package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Passive data-object representing a customer of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Customer implements Serializable {

	private int id; //id of the customer
	private String name; //name of the customer
	private String address; //address of the customer
	private int distance; //distance between the address of the customer and the store
	private Vector<OrderReceipt> Receipts; //Receipts which the customer made
	private int creditCard; //credit card number of the customer
	private int availableAmountInCreditCard; //money of the customer

	/**
	 * Constructor
	 * @param id
	 * @param name
	 * @param address
	 * @param distance
	 * @param Receipts
	 * @param creditCard
	 * @param availableAmountInCreditCard
	 */
	public Customer(int id, String name, String address, int distance, Vector<OrderReceipt> Receipts, int creditCard, int availableAmountInCreditCard){
		this.id = id;
		this.name = name;
		this.address = address;
		this.distance = distance;
		this.Receipts = Receipts;
		this.creditCard = creditCard;
		this.availableAmountInCreditCard = availableAmountInCreditCard;
	}
	/**
     * Retrieves the name of the customer.
     */
	public String getName() {
		return name;
	}

	/**
     * Retrieves the ID of the customer  . 
     */
	public int getId() {
		return id;
	}
	
	/**
     * Retrieves the address of the customer.  
     */
	public String getAddress() {
		return address;
	}
	
	/**
     * Retrieves the distance of the customer from the store.  
     */
	public int getDistance() {
		return distance;
	}

	
	/**
     * Retrieves a list of receipts for the purchases this customer has made.
     * <p>
     * @return A list of receipts.
     */
	public Vector<OrderReceipt> getCustomerReceiptList() {
		return Receipts;
	}
	
	/**
     * Retrieves the amount of money left on this customers credit card.
     * <p>
     * @return Amount of money left.   
     */
	public int getAvailableCreditAmount() {
		return availableAmountInCreditCard;
	}
	
	/**
     * Retrieves this customers credit card serial number.    
     */
	public int getCreditNumber() {
		return creditCard;
	}

	public void setAvailableAmountInCreditCard(int toCharge){
		availableAmountInCreditCard = availableAmountInCreditCard - toCharge;
	}

	public void addOrderReceipt(Vector<OrderReceipt> Orders){
		for (int i = 0; i < Orders.size(); i++)
			if (!Receipts.contains(Orders.get(i)))
				Receipts.add(Orders.get(i));
	}
	
}
