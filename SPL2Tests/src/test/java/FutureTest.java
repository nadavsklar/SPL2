import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;



@RunWith(Arquillian.class)
public class FutureTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(bgu.spl.mics.Future.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    public FutureTest(){
    }

    @Test
    public void get() {
        Future<String> F = new Future<String>();
        String result1 = "try1";
        String result2 = "try2";
        assertEquals(F.get(),result1);
        assertNotEquals(F.get(), null);
        F.resolve(result1);
        assertEquals(F.get(), result1);
        assertNotEquals(F.get(), null);
        F.resolve(result2);
        assertEquals(F.get(), result2);
        assertNotEquals(F.get(), result1);
        assertNotEquals(F.get(), null);

        Future<Integer> G = new Future<Integer>();
        Integer num1 = 0;
        Integer num2 = 1;
        assertEquals(G.get(),num1);
        assertNotEquals(G.get(), null);
        G.resolve(num1);
        assertEquals(G.get(), num1);
        assertNotEquals(G.get(), null);
        G.resolve(num2);
        assertEquals(G.get(), num2);
        assertNotEquals(G.get(), num1);
        assertNotEquals(G.get(), null);
    }


    @Test
    public void resolve() {
        Future<String> F = new Future<String>();
        String result1 = "try1";
        String result2 = "try2";
        F.resolve(result1);
        assertEquals(F.get(), result1);
        F.resolve(result2);
        assertNotEquals(F.get(), result1);
        assertEquals(F.get(), result2);

        Future<Integer> G = new Future<Integer>();
        Integer num1 = 1;
        Integer num2 = 2;
        G.resolve(num1);
        assertEquals(G.get(), num1);
        G.resolve(num2);
        assertNotEquals(G.get(), num2);
        assertEquals(G.get(), num2);
    }

    @Test
    public void isDone() {
        Future<String> F = new Future<String>();
        assertEquals(F.isDone(), false);
        String result = "try";
        F.resolve(result);
        assertEquals(F.isDone(), true);

        Future<Integer> G = new Future<Integer>();
        assertEquals(G.isDone(), false);
        Integer num = 0;
        G.resolve(num);
        assertEquals(G.isDone(), true);

    }

    @Test
    public void getTime() {
        Future<String> F = new Future<String>();
        long timeout = 20;
        TimeUnit unit = TimeUnit.SECONDS;
        F.get(timeout, unit);
        assertEquals(F.get(timeout, unit), "try");
        F.resolve("try");

    }





}
