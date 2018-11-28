package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a information about a certain book in the inventory.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class BookInventoryInfo {

    private String BookTitle;
    private int price;
    private int amountInInventory;

	public BookInventoryInfo(String BookTitle, int price) {
	    this.BookTitle = BookTitle;
	    this.price = price;
    }

    public BookInventoryInfo(String BookTitle, int price, int amountInInventory) {
        this.BookTitle = BookTitle;
        this.price = price;
        this.amountInInventory = amountInInventory;
    }
	/**
     * Retrieves the title of this book.
     * <p>
     * @return The title of this book.   
     */
	public String getBookTitle() {
		return BookTitle;
	}

	/**
     * Retrieves the amount of books of this type in the inventory.
     * <p>
     * @return amount of available books.      
     */
	public int getAmountInInventory() {
		return amountInInventory;
	}

	/**
     * Retrieves the price for  book.
     * <p>
     * @return the price of the book.
     */
	public int getPrice() {
		return price;
	}
	
	

	
}
