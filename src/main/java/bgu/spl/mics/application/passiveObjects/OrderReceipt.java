package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a receipt that should 
 * be sent to a customer after the completion of a BookOrderEvent.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class OrderReceipt {

	private int orderId;
	private String seller;
	private int customerId;
	private String bookTitle;
	private int price;
	private int issuedTick; //tick in which the receipt was issued
	private int orderTick; //tick in which the customer ordered the book
	private int proccessTick; ////tick in which the selling service started processing the order

	/**
     * Retrieves the orderId of this receipt.
     */
	public int getOrderId() {
		// TODO Implement this
		return 0;
	}
	
	/**
     * Retrieves the name of the selling service which handled the order.
     */
	public String getSeller() {
		// TODO Implement this
		return null;
	}
	
	/**
     * Retrieves the ID of the customer to which this receipt is issued to.
     * <p>
     * @return the ID of the customer
     */
	public int getCustomerId() {
		// TODO Implement this
		return 0;
	}
	
	/**
     * Retrieves the name of the book which was bought.
     */
	public String getBookTitle() {
		// TODO Implement this
		return null;
	}
	
	/**
     * Retrieves the price the customer paid for the book.
     */
	public int getPrice() {
		// TODO Implement this
		return 0;
	}
	
	/**
     * Retrieves the tick in which this receipt was issued.
     */
	public int getIssuedTick() {
		// TODO Implement this
		return 0;
	}
	
	/**
     * Retrieves the tick in which the customer sent the purchase request.
     */
	public int getOrderTick() {
		// TODO Implement this
		return 0;
	}
	
	/**
     * Retrieves the tick in which the treating selling service started 
     * processing the order.
     */
	public int getProcessTick() {
		// TODO Implement this
		return 0;
	}
}
