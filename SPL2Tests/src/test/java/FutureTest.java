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
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                assertEquals(F.get(),result1);
                assertNotNull(F.get());
            }
        };
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                F.resolve(result1);
                assertEquals(F.get(), result1);
                assertNotNull(F.get());
            }
        };
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
        try{
            t1.join();
            t2.join();
        }
        catch (Exception e){

        }
        Runnable r3 = new Runnable() {
            @Override
            public void run() {
                F.resolve(result2);
                assertEquals(F.get(), result2);
                assertNotEquals(F.get(), result1);
                assertNotNull(F.get());
            }
        };
        Thread t3 = new Thread(r3);
        t3.start();
    }


    @Test
    public void resolve() {
        Future<String> F = new Future<String>();
        String garbage = null;
        String result1 = "try1";
        String result2 = "try2";

        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                F.resolve(garbage);
                assertEquals(F.get(), result1);
            }
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                F.resolve(result1);
                assertEquals(F.get(), result1);
            }
        };

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();

        try{
            t1.join();
            t2.join();
        }
        catch (Exception e){

        }

        Runnable r3 = new Runnable() {
            @Override
            public void run() {
                F.resolve(result2);
                assertEquals(F.get(), result2);
                assertNotEquals(F.get(), result1);
            }
        };
        Thread t3 = new Thread(r3);
        t3.start();

    }

    @Test
    public void isDone() {
        Future<String> F = new Future<String>();
        assertFalse(F.isDone());
        String result = "try";
        F.resolve(result);
        assertTrue(F.isDone());

        Future<Integer> G = new Future<Integer>();
        assertFalse(G.isDone());
        Integer num = 0;
        G.resolve(num);
        assertTrue(G.isDone());

    }

    @Test
    public void getTime() {
        Future<String> F = new Future<String>();
        long timeout = 20;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        String result = "try";

        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                assertNull(F.get(timeout, unit),result);
                F.resolve(result);
                assertEquals(F.get(timeout, unit),result);
            }
        };

        Thread t1 = new Thread(r1);
        t1.start();
    }





}
