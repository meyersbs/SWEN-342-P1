/*
 * @PROJECT: SWEN-342 | Project 1
 *
 * @AUTHOR: Ben Meyers
 * @EMAIL: bsm9339@rit.edu
 *
 * @AUTHOR: Asma Sattar
 * @EMAIL: aas3799rit.edu
 */

import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class Manager extends Thread {

    private final CountDownLatch SIGNAL;

    /**
     *
     */
    public Manager(CountDownLatch signal) {
        this.SIGNAL = signal;
    }

    public void run() {
        try {
            this.SIGNAL.await();

            for(int i = 0; i < 5; i++) {
                System.out.println("MANAGER THREAD RUNNING...");
            }
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}