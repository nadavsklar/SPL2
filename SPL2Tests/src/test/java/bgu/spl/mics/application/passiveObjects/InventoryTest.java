package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    Inventory inventory = Inventory.getInstance();

    @Test
    void getInstance() {
        assertNotNull(Inventory.getInstance());
    }

    @Test
    void load() {
        checkAvailabiltyAndGetPrice();
    }

    @Test
    void take() {
        BookInventoryInfo[] Books = new BookInventoryInfo[2];
        Books[0] = new BookInventoryInfo("Harry Poter", 50, 0);
        Books[1] = new BookInventoryInfo("Game Of Thrones", 50000, 2);
        inventory.load(Books);
        assertEquals(OrderResult.SUCCESSFULLY_TAKEN, inventory.take("Game Of Thrones"));
        assertEquals(OrderResult.NOT_IN_STOCK, inventory.take("Harry Poter"));
    }

    @Test
    void checkAvailabiltyAndGetPrice() {
        BookInventoryInfo[] Books = new BookInventoryInfo[2];
        Books[0] = new BookInventoryInfo("Harry Poter", 50, 1);
        Books[1] = new BookInventoryInfo("Game Of Thrones", 50000, 1);
        inventory.load(Books);
        assertEquals(50, inventory.checkAvailabiltyAndGetPrice("Harry Poter"));
        assertEquals(50000, inventory.checkAvailabiltyAndGetPrice("Game Of Thrones"));
        assertEquals(-1, inventory.checkAvailabiltyAndGetPrice("Titanic"));

    }

    @Test
    void printInventoryToFile() {
        BookInventoryInfo[] Books = new BookInventoryInfo[2];
        Books[0] = new BookInventoryInfo("Harry Poter", 50, 100);
        Books[1] = new BookInventoryInfo("Game Of Thrones", 50000, 500);
        inventory.load(Books);
        String filename = "filename";
        inventory.printInventoryToFile(filename);
        try {
            FileInputStream myFileStrem = new FileInputStream(filename + ".ser");
            ObjectInputStream in = new ObjectInputStream(myFileStrem);
            HashMap<String, Integer> out = (HashMap<String,Integer>) in.readObject();
            assertTrue(out.containsKey("Harry Poter"));
            assertTrue(out.containsKey("Game Of Thrones"));
            assertEquals(100, out.get("Harry Poter").intValue());
            assertEquals(500, out.get("Game Of Thrones").intValue());
        }
        catch (Exception e) {
            throw new RuntimeException("ERROR Test printInventoryToFIle");
        }
    }
}