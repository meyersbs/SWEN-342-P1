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

/**
 *
 */
public class Employee extends Thread {

    private final int ID;
    private final CountDownLatch LEAD_MEETING_SIGNAL, STATUS_MEETING_SIGNAL;
    private Team my_team;
	private Semaphore conference_room;
	private int delay = (int) (Math.random() * 300); //8:00-8:30PM (0 to 30mins, which is 300mill, which is  0.30s)
	private int lunch = (int) (Math.random() * (300 - delay)) + 300;


    /**
     *
     */
    public Employee(int id, CountDownLatch signal, Semaphore conference_room, CountDownLatch statusMeeting_signal) {
        this.ID = id;
        this.LEAD_MEETING_SIGNAL = signal;
        this.conference_room = conference_room;
        this.STATUS_MEETING_SIGNAL = statusMeeting_signal;
        
    }

    public void run() {

        try {
        Thread.sleep(delay);
        System.out.println("Arrives at " + Clock.getString());
        System.out.println("Employee " + this.getID() + " shows up at the office.");
        /* If the Employee is a team lead */
        if (my_team.getTeamLeader() == this){
            /* On arrival they knock on Mangers door for a 15mins meeting */
            System.out.println("Team Lead " + this.getID() + " knocks on Managers door.");
            LEAD_MEETING_SIGNAL.countDown();
            LEAD_MEETING_SIGNAL.await();
            /*  one minute = 10ms so 15mins = (10ms * 15 ) = 150ms or 0.15 seconds */
            Thread.sleep(150); 
            System.out.println("Team Lead " + this.getID() + " leaves meeting with Manager.");
         }
      
        //Team lead(1) & Members(3) meetings
        //After the morning mangers meeting, the team leads wait for all the members of their team to arrive.
        //waiting for all members to arrive before meeting starts.
        System.out.println("Team memebr " + this.getID() + " is waiting for all members.");
        my_team.getTeamSignal().countDown();
        my_team.getTeamSignal().await();
        System.out.println("Team memeber " + this.getID() + " arrives at conference room.");
      
        if(my_team.getTeamLeader() == this){
        	//try to get Semaphore
        	conference_room.acquire();
         	//Enters the room with all team members
        	System.out.println("Team Lead " +this.getID() +" Enters the room with all team members");
            my_team.getConferenceRoomSignal().countDown();
        	 /*  one minute = 10ms so 15mins = (10ms * 15 ) = 150ms or 0.15 seconds */
            Thread.sleep(150); 
            //release theSemaphore
        	System.out.println("Team Lead " +this.getID() +" leaves conference room.");
            conference_room.release(); 
        }else{
        	my_team.getConferenceRoomSignal().await();
	    	System.out.println("Team memeber " +this.getID() +" Enters the room");
	        /*  one minute = 10ms so 15mins = (10ms * 15 ) = 150ms or 0.15 seconds */
	        Thread.sleep(150); 
	        System.out.println("Team memebr " + this.getID() + " leaves meeting in conference room.");
        } 
        /*busy waiting*/
        while(Clock.getHours() < 12){
        	/**
        	 * When not asking questions, at meetings, or at lunch, 
        	 * team leads and developers are hard at work designing, coding and testing.
        	 */
        }
        //12:00AM LUNCH TIME
        System.out.println("Employee " + this.getID() + " starts lunch at: " + Clock.getString());
        Thread.sleep(lunch); //at least 30mins but no more than 1hrs
        System.out.println("Employee " + this.getID() + " finishes lunch at: " + Clock.getString());
        
        
        /**
         * TODO: either working, asking a question or waiting to get their question answered.
         */
        while(Clock.getHours() < 16){
        	//either working, asking a question or waiting to get their question answered.
        	/**
        	 * When not asking questions, at meetings, or at lunch, 
        	 * team leads and developers are hard at work designing, coding and testing.
        	 */
        }
        //Status update meeting
        System.out.println("Employee " + this.getID() + " arrives at the Status meeting at " + Clock.getString());
        STATUS_MEETING_SIGNAL.countDown();
        STATUS_MEETING_SIGNAL.await();
        /**
         * TODO:
         *  If a Employee is stuck asking a question they need to give up in order to be at the meeting by 4:15PM
         *  a.k.a : "allowing 15 minutes to clean up any work in progress. "
         *  */
        
        Thread.sleep(150); //15 minute meeting
        System.out.println("Employee " + this.getID() + " leaves the Status meeting at "  + Clock.getString());
        
        
        /**
         * TODO: either working, asking a question or waiting to get their question answered.
         */
        while(Clock.getCurrentTime() < (4800 + delay +lunch)){
        	//either working, asking a question or waiting to get their question answered.
        	/**
        	 * When not asking questions, at meetings, or at lunch, 
        	 * team leads and developers are hard at work designing, coding and testing.
        	 */
        }
        System.out.println("Employee " + this.getID() + " leaves at " + Clock.getString()+ " to go home!");
        
        
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
 
    public void setTeam(Team myTeam){
       this.my_team = myTeam;
    }
    /**
     *
     * @return
     */
    public int getID() {
        return ID;
    }
}