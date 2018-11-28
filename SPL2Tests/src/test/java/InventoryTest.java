import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.OrderResult;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class InventoryTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(bgu.spl.mics.application.passiveObjects.Inventory.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    Inventory inventory = Inventory.getInstance();

    @Test
    public void getInstance() {
    }

    @Test
    public void load() {
        checkAvailabiltyAndGetPrice();
    }

    @Test
    public void take() {
        BookInventoryInfo[] Books = new BookInventoryInfo[2];
        Books[0] = new BookInventoryInfo("Harry Poter", 50, 0);
        Books[1] = new BookInventoryInfo("Game Of Thrones", 50000, 2);
        inventory.load(Books);
        assertEquals(OrderResult.SUCCESSFULLY_TAKEN, inventory.take("Game Of Thrones"));
        assertEquals(OrderResult.NOT_IN_STOCK, inventory.take("Harry Poter"));
    }

    @Test
    public void checkAvailabiltyAndGetPrice() {
        BookInventoryInfo[] Books = new BookInventoryInfo[2];
        Books[0] = new BookInventoryInfo("Harry Poter", 50);
        Books[1] = new BookInventoryInfo("Game Of Thrones", 50000);
        inventory.load(Books);
        assertEquals(50, inventory.checkAvailabiltyAndGetPrice("Harry Poter"));
        assertEquals(50000, inventory.checkAvailabiltyAndGetPrice("Game Of Thrones"));
        try {
            assertEquals(100, inventory.checkAvailabiltyAndGetPrice("Titanic"));
        }
        catch (Exception e) {
            //Do nothing
        }
    }

    @Test
    public void printInventoryToFile() {
        BookInventoryInfo[] Books = new BookInventoryInfo[2];
        Books[0] = new BookInventoryInfo("Harry Poter", 50);
        Books[1] = new BookInventoryInfo("Game Of Thrones", 50000);
        inventory.load(Books);
        String filename = "/SPL2Tests/src/test/java/Inventory.ser";
        inventory.printInventoryToFile(filename);
        try {
            FileInputStream myFileStrem = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(myFileStrem);
            HashMap<String, Integer> out = (HashMap<String,Integer>) in.readObject();
            assertTrue(out.containsKey("Harry Poter"));
            assertTrue(out.containsKey("Game Of Thrones"));
            assertEquals(50, out.get("Harry Poter").intValue());
            assertEquals(50000, out.get("Game Of Thrones").intValue());

        }
        catch (Exception e) {
            throw new RuntimeException("ERROR Test printInventoryToFIle");
        }
    }

}
