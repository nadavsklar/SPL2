package bgu.spl.mics.application.passiveObjects;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

import static org.junit.Assert.*;

public class InventoryTest {

    Inventory inventory;

    @Before
    public void setUp() throws Exception {
        inventory = Inventory.getInstance();
        BookInventoryInfo[] Books = new BookInventoryInfo[3];
        Books[0] = new BookInventoryInfo("Harry Poter", 50, 0);
        Books[1] = new BookInventoryInfo("Game Of Thrones", 50000, 100);
        Books[2] = new BookInventoryInfo("The Hunger Games", 150, 280);
        inventory.load(Books);
    }

    @Test
    public void getInstance() {
        assertNotNull(Inventory.getInstance());
    }

    @Test
    public void load() {
        assertNotEquals(-1, inventory.checkAvailabiltyAndGetPrice("Game Of Thrones"));
        assertNotEquals(-1, inventory.checkAvailabiltyAndGetPrice("The Hunger Games"));
    }

    @Test
    public void take() {
        assertEquals(OrderResult.SUCCESSFULLY_TAKEN, inventory.take("Game Of Thrones"));
        assertEquals(OrderResult.NOT_IN_STOCK, inventory.take("Harry Poter"));
    }

    @Test
    public void checkAvailabiltyAndGetPrice() {
        assertEquals(150, inventory.checkAvailabiltyAndGetPrice("The Hunger Games"));
        assertEquals(50000, inventory.checkAvailabiltyAndGetPrice("Game Of Thrones"));
        assertEquals(-1, inventory.checkAvailabiltyAndGetPrice("Harry Poter"));
    }

    @Test
    public void printInventoryToFile() {
        String filename = "filename";
        inventory.printInventoryToFile(filename);
        try {
            FileInputStream myFileStrem = new FileInputStream(filename + ".ser");
            ObjectInputStream in = new ObjectInputStream(myFileStrem);
            HashMap<String, Integer> out = (HashMap<String,Integer>) in.readObject();
            assertTrue(out.containsKey("Harry Poter"));
            assertTrue(out.containsKey("Game Of Thrones"));
            assertEquals(0, out.get("Harry Poter").intValue());
            assertEquals(100, out.get("Game Of Thrones").intValue());
        }
        catch (Exception e) {
            throw new RuntimeException("ERROR Test printInventoryToFIle");
        }
    }
}