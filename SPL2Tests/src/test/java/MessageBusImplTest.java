import bgu.spl.mics.Event;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.APIService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.core.impl.EventImpl;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import bgu.spl.mics.example.messages.ExampleEvent;


@RunWith(Arquillian.class)
public class MessageBusImplTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(MessageBusImpl.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    public MessageBusImplTest(){
    }

    @Test
    public void subscribeEvent() {

     }

    @Test
    public void subscribeBroadcast() {
    }

    @Test
    public void complete() {
    }

    @Test
    public void sendBroadcast() {
    }

    @Test
    public void sendEvent() {
    }

    @Test
    public void register() {
    }

    @Test
    public void unregister() {
    }

    @Test
    public void awaitMessage() {
    }
}
