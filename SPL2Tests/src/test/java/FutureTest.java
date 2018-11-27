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
        String result = "";
        F.resolve(result);
        assertEquals(F.get(), result);
    }

    @Test
    public void resolve() {
        Future<String> F = new Future<String>();
        String result = "";
        F.resolve(result);
        assertEquals(F.get(), result);
    }

    @Test
    public void isDone() {
        Future<String> F = new Future<String>();
        assertEquals(F.isDone(), false);
        String result = "";
        F.resolve(result);
        assertEquals(F.isDone(), true);
    }

    @Test
    public void get1() {
        Future<String> F = new Future<String>();
        long timeout = 20;
        TimeUnit unit = TimeUnit.SECONDS;
        F.get(timeout, unit);
        assertEquals(F.get(timeout, unit), "");
        F.resolve("");


    }





}
