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
import java.util.concurrent.Semaphore;
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
        //Start Clock
        Clock.startClock();
        
        // Create a 'lock' to ensure everyone starts working at the same time.
        // THE LEAD MEETING - Signal to ensure that the Lead meeting starts when everyone gets there.
        CountDownLatch leadMeeting_signal = new CountDownLatch(3);
        
        // THE PROJECT STATUS UPDATE meeting in conference room.
        CountDownLatch statusMeeting_signal = new CountDownLatch(13);
             
        // Create manager.
        Manager MANAGER = new Manager(leadMeeting_signal, statusMeeting_signal);
        MANAGER.start();

    	/* Only one team can go into the conference room at a time */
    	final Semaphore conference_room = new Semaphore(1);
    	
        // Create employees.
        for(int i = 0; i < NUM_EMPLOYEES; i++) {
            // Create a new employee with a psuedorandom ID number.
            Employee temp = new Employee(generateID.nextInt(Integer.MAX_VALUE), leadMeeting_signal, conference_room, statusMeeting_signal);
            EMPLOYEES.add(temp);
        }

        // Set up teams.
        for(int i = 0; i < NUM_EMPLOYEES; i+=4) {
            ArrayList<Employee> tempMembers = new ArrayList<Employee>(EMPLOYEES.subList(i, i+4));
            if(!tempMembers.isEmpty()) {
                /* create new instance of Team*/
                Team team = new Team(i%4, tempMembers);
                /* Adding each team to TEAMS*/
                TEAMS.add(team);
                /* looping over the Employees and adding them to a team - aka this is the team youre on*/
                for (Employee employee: tempMembers){
                    employee.setTeam(team);
                    employee.start();
                }
            }
        }

        System.out.println("\n"+ TEAMS+ "\n");
    }
}