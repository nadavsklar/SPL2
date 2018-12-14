package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.*;

public class MainHelper {

    public static MicroService[] initiateWorkers(SellingService[] SellingServices, InventoryService[] InventoryServices, LogisticsService[] LogisticServices, ResourceService[] ResourceServices, APIService[] apiServices) {
        int numOfWorkers = SellingServices.length;
        numOfWorkers += InventoryServices.length;
        numOfWorkers += LogisticServices.length;
        numOfWorkers += ResourceServices.length;
        numOfWorkers += apiServices.length;

        MicroService[] Workers = new MicroService[numOfWorkers];
        int currentIndex = 0;
        for (int i = 0; i < SellingServices.length; i++) {
            Workers[currentIndex] = SellingServices[i];
            currentIndex++;
        }
        for (int i = 0; i < InventoryServices.length; i++) {
            Workers[currentIndex] = InventoryServices[i];
            currentIndex++;
        }
        for (int i = 0; i < LogisticServices.length; i++) {
            Workers[currentIndex] = LogisticServices[i];
            currentIndex++;
        }
        for (int i = 0; i < ResourceServices.length; i++) {
            Workers[currentIndex] = ResourceServices[i];
            currentIndex++;
        }
        for (int i = 0; i < apiServices.length; i++) {
            Workers[currentIndex] = apiServices[i];
            currentIndex++;
        }

        return Workers;
    }

    public static Thread[] initiateThreads(MicroService[] Workers) {
        Thread[] WorkersThreads = new Thread[Workers.length];
        for (int i = 0; i < WorkersThreads.length; i++)
            WorkersThreads[i] = new Thread(Workers[i]);
        return WorkersThreads;
    }
}
