package bgu.spl.mics;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class FutureTest {

    @org.junit.jupiter.api.Test
    @Test
    void get() {
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


    @org.junit.jupiter.api.Test
    void resolve() {
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

    @org.junit.jupiter.api.Test
    void isDone() {
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

    @org.junit.jupiter.api.Test
    void getTime() {
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