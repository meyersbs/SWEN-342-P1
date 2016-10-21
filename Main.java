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
import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class Main {

    public static Random generateID = new Random();
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Initializing Simulation...");
        final int NUM_EMPLOYEES;
        ArrayList<Team> TEAMS = new ArrayList<Team>();
        ArrayList<Employee> EMPLOYEES = new ArrayList<Employee>();

        // Determine how many employees to create.
        if(args.length >= 1) {
            // Create the number of employees specified by the user.
            NUM_EMPLOYEES = Integer.parseInt(args[0]);
        }
        else {
            // Default to 12 employees if not specified.
            NUM_EMPLOYEES = 12;
        }

        // Create a 'lock' to ensure everyone starts working at the same time.
        CountDownLatch signal = new CountDownLatch(1);

        // Create manager.
        Manager MANAGER = new Manager(signal);
        MANAGER.start();

        // Create employees.
        for(int i = 0; i < NUM_EMPLOYEES; i++) {
            // Create a new employee with a psuedorandom ID number.
            Employee temp = new Employee(generateID.nextInt(Integer.MAX_VALUE), signal);
            EMPLOYEES.add(temp);
            temp.start();
        }

        // Set up teams.
        for(int i = 0; i < NUM_EMPLOYEES; i+=4) {
            ArrayList<Employee> tempMembers = new ArrayList<Employee>(EMPLOYEES.subList(i, i+4));
            if(!tempMembers.isEmpty()) {
                TEAMS.add(new Team(i%4, tempMembers));
            }
        }

        System.out.println(TEAMS);

        signal.countDown();
    }
}