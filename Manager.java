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

    private final CountDownLatch LEAD_MEETING_SIGNAL;
    private final CountDownLatch STATUS_MEETING_SIGNAL;

    /**
     *
     */
    public Manager(CountDownLatch LeadMeeting_signal, CountDownLatch statusMeeting_signal) {
        this.LEAD_MEETING_SIGNAL = LeadMeeting_signal;
        this.STATUS_MEETING_SIGNAL = statusMeeting_signal;
    }

    public void run() {
        try { 
            /* Arrives at 8:00AM engages in daily activity */
            System.out.println("The Manager arrives at "+ Clock.getString() + " and engages in daily activity.");
            System.out.println("The Manager waits for all team Leads to arrive at the office.");
            LEAD_MEETING_SIGNAL.await();
            /*  one minute = 10ms so 15mins = (10ms * 15 ) = 150ms or 0.15 seconds */
            System.out.println("All leads arrived and Manager starts the meeting at " + Clock.getString());
            Thread.sleep(150); //15 minute meeting
            System.out.println("The Manager finishes the Lead Morning Meeting at " + Clock.getString());
            
            /**
             * OVERALL TODO: If in the middle of answering a question when a meeting or lunch begins, 
             * the manager finishes answering the question and then goes to the meeting or lunch. 
             * Any other teams with questions simply wait for the manager to return.
             */
            
            /*busy waiting*/
            while(Clock.getHours() < 10){
            	/**
            	 * When not answering questions, at meetings, or at lunch, 
            	 * the manager does whatever it is managers do (looking for deals on Woot!, 
            	 * reading blogs, thinking of ways to make the developers' lives miserable, etc.)
            	 */
            }
            //10:00AM executive meetings
            System.out.println("Manager arrives at " + Clock.getString() + " to executive meeting");
            Thread.sleep(600); //1hr executive meeting
            System.out.println("Manager leaves at " + Clock.getString() + " from executive meeting");
            
            /*busy waiting*/
            while(Clock.getHours() < 12){
            	/**
            	 * When not answering questions, at meetings, or at lunch, 
            	 * the manager does whatever it is managers do (looking for deals on Woot!, 
            	 * reading blogs, thinking of ways to make the developers' lives miserable, etc.)
            	 */
            }
            
            //12:00AM LUNCH
            System.out.println("Manager starts lunch at:  " + Clock.getString());
            Thread.sleep(600); //1hr executive meeting
            System.out.println("Manager finishes lunch at: " + Clock.getString());
            
            
            /*busy waiting*/
            while(Clock.getHours() < 14){
            	/**
            	 * When not answering questions, at meetings, or at lunch, 
            	 * the manager does whatever it is managers do (looking for deals on Woot!, 
            	 * reading blogs, thinking of ways to make the developers' lives miserable, etc.)
            	 */
            }
            //2:00PM executive meetings
            System.out.println("Manager arrives at " + Clock.getString() + " to executive meeting");
            Thread.sleep(600); //1hr executive meeting
            System.out.println("Manager leaves at " + Clock.getString() + " from executive meeting");
            
            /*busy waiting*/
            while(Clock.getHours() < 16){
            	/**
            	 * When not answering questions, at meetings, or at lunch, 
            	 * the manager does whatever it is managers do (looking for deals on Woot!, 
            	 * reading blogs, thinking of ways to make the developers' lives miserable, etc.)
            	 */
            }
            //Status update meeting
            System.out.println("Manager arrives at the Status meeting");
            STATUS_MEETING_SIGNAL.countDown();
            STATUS_MEETING_SIGNAL.await();
            /**
             * 1) TODO:
             *  If the Manger is stuck helping an Employee with a question they need to give up in order to be at the meeting by 4:15PM
             *  a.k.a : "allowing 15 minutes to clean up any work in progress. "
             *  */
            System.out.println("Manager starts the Status meeting at "  + Clock.getString());
            Thread.sleep(150); //15 minute meeting
            System.out.println("Manager leaves the Status meeting at " + Clock.getString());
            
            /*busy waiting*/
            while(Clock.getHours() < 17){
            	/**
            	 * When not answering questions, at meetings, or at lunch, 
            	 * the manager does whatever it is managers do (looking for deals on Woot!, 
            	 * reading blogs, thinking of ways to make the developers' lives miserable, etc.)
            	 */
            }
            System.out.println("Manager leaves work at "+ Clock.getString() +" and goes home!");
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
