import bgu.spl.mics.Future;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;



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
    }

    @Test
    public void resolve() {
    }

    @Test
    public void isDone() {
    }

    @Test
    public void get1() {
    }



}
