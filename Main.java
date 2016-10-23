/*
 * @PROJECT: SWEN-342 | Project 1
 *
 * @AUTHOR: Ben Meyers
 * @EMAIL: bsm9339@rit.edu
 *
 * @AUTHOR: Asma Sattar
 * @EMAIL: aas3799@rit.edu
 */

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;

/**
 * This class contains the main function that gets run when the program is executed.
 */
public class Main {

    private static final int NUM_EMPLOYEES =12;
    private static ArrayList<Team> TEAMS = new ArrayList<Team>();
    private static ArrayList<Employee> EMPLOYEES = new ArrayList<Employee>();
    private static Manager MANAGER;

    /**
     * Create the Manager, Employees, and Teams. Start all necessary threads.
     * @param args unused
     */
    public static void main(String[] args) {
        //Start Clock
        Clock.startClock();
        
        // Create a 'lock' to ensure everyone starts working at the same time.
        // THE LEAD MEETING - Signal to ensure that the Lead meeting starts when everyone gets there.
        CountDownLatch leadMeeting_signal = new CountDownLatch(3);
        
        // THE PROJECT STATUS UPDATE meeting in conference room.
        CountDownLatch statusMeeting_signal = new CountDownLatch(13);
             
        // Create manager.
        MANAGER = new Manager(leadMeeting_signal, statusMeeting_signal);
        MANAGER.start();

    	/* Only one team can go into the conference room at a time */
    	final Semaphore conference_room = new Semaphore(1);
    	
        // Create employees.
        for(int i = 0; i < NUM_EMPLOYEES; i++) {
            // Create a new employee with a psuedorandom ID number.
            //Employee temp = new Employee(generateID.nextInt(Integer.MAX_VALUE), leadMeeting_signal, conference_room, statusMeeting_signal);
            Employee temp = new Employee(i%4+1, leadMeeting_signal, conference_room, statusMeeting_signal);
            EMPLOYEES.add(temp);
        }

        // Set up teams.
        int j = 1;
        for(int i = 0; i < NUM_EMPLOYEES; i+=4) {
            ArrayList<Employee> tempMembers = new ArrayList<Employee>(EMPLOYEES.subList(i, i+4));
            if(!tempMembers.isEmpty()) {
                /* create new instance of Team*/
                Team team = new Team(j, tempMembers);
                /* Adding each team to TEAMS*/
                TEAMS.add(team);
                /* looping over the Employees and adding them to a team - aka this is the team you're on*/
                for (Employee employee: tempMembers){
                    employee.setTeam(team);
                    employee.start();
                }
            }
            j++;
        }

        boolean simulation_running = true;
        while(simulation_running) {
            for(Employee employee: EMPLOYEES) {
                if(employee.isAlive()) {
                    break;
                }else {
                    if(MANAGER.isAlive()) {
                        break;
                    }else {
                        simulation_running = false;
                    }
                }
            }
        }
        reportHours();
    }

    public static void reportHours() {
        for(Team t: TEAMS) {
            System.out.println("\n\n===== TEAM " + t.getTeamNumber() + " TIMECLOCK =====");
            for(Employee m: t.getTeamMembers()) {
                System.out.println("\tEmployee ID: " + m.my_team.getTeamNumber() + "-" + m.getID());
                System.out.println("\t\t- arrived at " + m.getStartTime() + " and went home at " + m.getEndTime() + ".");
                System.out.println("\t\t- took a " + (double)(m.getLunchTime() / 10) + " minute lunch.");
                System.out.println("\t\t- spent " + (double)(m.getMeetingTime() / 10) + " minutes in meetings.");
                System.out.println("\t\t- spent " + (double)(m.getWaitingTime() / 10) + " minutes waiting for the Manager.");
            }
        }
        System.out.println("\n\n===== MANAGER TIMECLOCK =====");
        System.out.println("\t- arrived at " + MANAGER.getStartTime() + " and went home at " + MANAGER.getEndTime() + ".");
        System.out.println("\t- took a " + (double)(MANAGER.getLunchTime() / 10) + " minute lunch.");
        System.out.println("\t- spent " + (double)(MANAGER.getMeetingTime() / 10) + " minutes in meetings.");
    }
}