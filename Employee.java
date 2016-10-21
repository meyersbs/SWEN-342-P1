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
public class Employee extends Thread {

    private final int ID;
    private final CountDownLatch SIGNAL;

    /**
     *
     */
    public Employee(int id, CountDownLatch signal) {
        this.ID = id;
        this.SIGNAL = signal;
    }

    public void run() {
        try {
            this.SIGNAL.await();
            for(int i = 0; i < 5; i++) {
                System.out.println("EMPLOYEE " + this.getID() + " is working...");
            }
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public int getID() {
        return ID;
    }
}