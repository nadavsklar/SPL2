package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;

/**
 * Passive data-object representing a receipt that should 
 * be sent to a customer after the completion of a BookOrderEvent.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class OrderReceipt implements Serializable {

	private int orderId; //id of the order
	private String seller; //name of the service which created the receipt
	private int customerId; //id of the customer
	private String bookTitle; //book title
	private int price; //total price
	private int issuedTick; //tick in which the receipt was issued
	private int orderTick; //tick in which the customer ordered the book
	private int proccesTick; ////tick in which the selling service started processing the order

	public OrderReceipt(){}
	/**
     * Retrieves the orderId of this receipt.
     */
	public int getOrderId() {
		return orderId;
	}
	
	/**
     * Retrieves the name of the selling service which handled the order.
     */
	public String getSeller() {
		return seller;
	}
	
	/**
     * Retrieves the ID of the customer to which this receipt is issued to.
     * <p>
     * @return the ID of the customer
     */
	public int getCustomerId() {
		return customerId;
	}
	
	/**
     * Retrieves the name of the book which was bought.
     */
	public String getBookTitle() {
		return bookTitle;
	}
	
	/**
     * Retrieves the price the customer paid for the book.
     */
	public int getPrice() {
		return price;
	}
	
	/**
     * Retrieves the tick in which this receipt was issued.
     */
	public int getIssuedTick() {
		return issuedTick;
	}
	
	/**
     * Retrieves the tick in which the customer sent the purchase request.
     */
	public int getOrderTick() {
		return orderTick;
	}
	
	/**
     * Retrieves the tick in which the treating selling service started 
     * processing the order.
     */
	public int getProccessTick() {
		return proccesTick;
	}

	//Setters
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public void setIssuedTick(int issuedTick) {
		this.issuedTick = issuedTick;
	}
	public void setOrderTick(int orderTick) {
		this.orderTick = orderTick;
	}
	public void setProccesTick(int proccesTick) {
		this.proccesTick = proccesTick;
	}
}
