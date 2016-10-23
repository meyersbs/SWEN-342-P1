/*
 * @PROJECT: SWEN-342 | Project 1
 *
 * @AUTHOR: Ben Meyers
 * @EMAIL: bsm9339@rit.edu
 *
 * @AUTHOR: Asma Sattar
 * @EMAIL: aas3799rit.edu
 */
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

/**
 * The Manager has the ability to start & end meetings, and answer questions.
 */
public class Manager extends Thread {

    private final CountDownLatch LEAD_MEETING_SIGNAL;
    private final CountDownLatch STATUS_MEETING_SIGNAL;
    private static boolean atLunch = false;
    private static boolean inMeeting = false;
    private static boolean isWorking = false;
    private static Queue<Employee> managerWaitList = new LinkedList<Employee>();
    private static Map<String, Integer> timeclock = new HashMap<String, Integer>();
    private String startTime;
    private String endTime;

    /**
     *
     * @param LeadMeeting_signal - the CountDownLatch for the LEAD Meeting.
     * @param statusMeeting_signal - the CountDownLatch for the STATUS Meeting.
     */
    public Manager(CountDownLatch LeadMeeting_signal, CountDownLatch statusMeeting_signal) {
        this.LEAD_MEETING_SIGNAL = LeadMeeting_signal;
        this.STATUS_MEETING_SIGNAL = statusMeeting_signal;

        // Setup the timeclock for logging of hours.
        timeclock.put("LUNCH", 0);
        timeclock.put("MEETING", 0);
    }

    /**
     * Basic schedule:
     *      8:00            Manager arrives.
     *      8:00 - 8:30     Manager waits for all Team Leads to arrive.
     *      8:30 - 8:45     Manager meets with Team Leads.
     *      8:45 - 10:00    Manager works and answers questions.
     *      10:00 - 11:00   Manager meets with Executives.
     *      11:00 - 12:00   Manager works and answers questions.
     *      12:00 - 1:00    Manager eats lunch.
     *      1:00 - 2:00     Manager works and answers questions.
     *      2:00 - 3:00     Manager meets with Executives.
     *      3:00 - 4:00     Manager works and answers questions.
     *      4:00 - 4:15     Manager cleans up before Status meeting.
     *      4:15 - 4:30     Manager holds Status meeting with all Employees.
     *      4:30 - 5:00     Manager ties up loose ends.
     *      5:00            Manager leaves.
     */
    public void run() {
        try { 
            /* Arrives at 8:00AM engages in daily activity */
            System.out.println(Clock.getString() + "\tManager arrives and waits for all Team Leads to arrive at office.");
            startTime = Clock.getString();
            LEAD_MEETING_SIGNAL.await();
            /*  one minute = 10ms so 15mins = (10ms * 15 ) = 150ms or 0.15 seconds */
            System.out.println(Clock.getString() + "\tAll Leads arrived and Manager starts Morning Meeting.");
            inMeeting = true;
            Thread.sleep(150); //15 minute meeting
            timeclock.put("MEETING", timeclock.get("MEETING") + 150);
            System.out.println(Clock.getString() + "\tManager finishes Morning Meeting.");
            inMeeting = false;

            /*busy waiting*/
            while(Clock.getHours() < 10){
                if(!isWorking) {
                    if(!inMeeting) {
                        System.out.println(Clock.getString() + "\tManager is browsing r/sysadmin.");
                        isWorking = true;
                    }
                }
            }

            //10:00AM executive meetings
            System.out.println(Clock.getString() + "\tManager arrives at Executive Meeting");
            inMeeting = true;
            Thread.sleep(600); //1hr executive meeting
            timeclock.put("MEETING", timeclock.get("MEETING") + 600);
            System.out.println(Clock.getString() + "\tManager leaves Executive Meeting.");
            inMeeting = false;
            
            /*busy waiting*/
            while(Clock.getHours() < 11.9){
                if(!isWorking) {
                    if(!inMeeting) {
                        System.out.println(Clock.getString() + "\tManager is planning world domination.");
                        isWorking = true;
                    }
                }
            }

            // Stop answering questions before lunch.
            if(inMeeting) {
                System.out.println(Clock.getString() + "\tManager stops answering questions before lunch.");
                inMeeting = false;
            }
            Thread.yield();
            
            //12:00AM LUNCH
            System.out.println(Clock.getString() + "\tManager starts lunch.");
            atLunch = true;
            Thread.sleep(600); //1hr executive meeting
            timeclock.put("LUNCH", timeclock.get("LUNCH") + 600);
            System.out.println(Clock.getString() + "\tManager finishes lunch.");
            atLunch = false;
            
            /*busy waiting*/
            while(Clock.getHours() < 14){
                if(!isWorking) {
                    if(!inMeeting) {
                        System.out.println(Clock.getString() + "\tManager is cooking the books.");
                        isWorking = true;
                    }
                }
            }
            //2:00PM executive meetings
            System.out.println(Clock.getString() + "\tManager arrives at Executive Meeting");
            inMeeting = true;
            Thread.sleep(600); //1hr executive meeting
            timeclock.put("MEETING", timeclock.get("MEETING") + 600);
            System.out.println(Clock.getString() + "\tManager leaves Executive Meeting.");
            inMeeting = false;
            
            /*busy waiting*/
            while(Clock.getHours() < 16){
                if(!isWorking) {
                    if(!inMeeting) {
                        System.out.println(Clock.getString() + "\tManager is reading trashy romance novels.");
                        isWorking = true;
                    }
                }
            }

            // Stop answering questions 15 minutes before the Status meeting and clean up.
            if(inMeeting) {
                System.out.println(Clock.getString() + "\tManager stops answering questions and cleans up.");
                inMeeting = false;
            }else {
                System.out.println(Clock.getString() + "\tManager cleans up before Status Meeting.");
            }
            Thread.yield();

            //Status update meeting
            System.out.println(Clock.getString() + "\tManager arrives at Status Meeting");
            STATUS_MEETING_SIGNAL.countDown();
            STATUS_MEETING_SIGNAL.await();

            System.out.println(Clock.getString() + "\tManager starts Status Meeting.");
            inMeeting = true;
            Thread.sleep(150); //15 minute meeting
            timeclock.put("MEETING", timeclock.get("MEETING") + 150);
            System.out.println(Clock.getString() + "\tManager leaves Status Meeting.");
            inMeeting = false;
            
            /*busy waiting*/
            while(Clock.getHours() < 17){
                if(!isWorking) {
                    if(!inMeeting) {
                        System.out.println(Clock.getString() + "\tManager is stealing supplies from the office.");
                        isWorking = true;
                    }
                }
            }
            System.out.println(Clock.getString() + "\tManager leaves work and goes home!");
            endTime = Clock.getString();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return true if the Manager is currently in a meeting.
     */
    public static boolean inMeeting() { return inMeeting; }

    /**
     * @return true if the Manager is currently eating lunch.
     */
    public static boolean isAtLunch() { return atLunch; }

    /**
     * @return the time that the Manager arrived.
     */
    public String getStartTime() { return startTime; }

    /**
     * @return the time that the Manager went home.
     */
    public String getEndTime() { return endTime; }

    /**
     * @return the number of minutes the Manager spent eating lunch.
     */
    public int getLunchTime() { return timeclock.get("LUNCH"); }

    /**
     * @return the number of minutes the Manager spent in meetings.
     */
    public int getMeetingTime() { return timeclock.get("MEETING"); }

    /**
     * The Team Lead and question Asker wait in line for the Manager to answer their question. If the Manager is on
     *      lunch or in a meeting, the Team Lead and Asker wait for the Manager to return.
     * @param lead - the Team Lead asking the question.
     */
    public static synchronized void answerQuestion(Employee lead) {
        lead.setInMeeting(true);
        managerWaitList.add(lead);
        // Team Lead waits in line for the Manager.
        while(lead != managerWaitList.peek()) {
            try {
                lead.sleep(50);
                lead.log("WAITING", 50);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Team Lead waits for the Manager to return.
        if(inMeeting || atLunch) {
            System.out.println(Clock.getString() + "\tTeam Lead " + lead.my_team.getTeamNumber() + "-" + lead.getID() + " waits for Manager to return.");
            try {
                lead.sleep(50);
                lead.log("WAITING", 50);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Team Lead asks question.
        System.out.println(Clock.getString() + "\tTeam Lead " + lead.my_team.getTeamNumber() + "-" + lead.getID() + " ask the Manager a question.");
        inMeeting = true;
        isWorking = false;
        try {
            // Manager answers question.
            Thread.sleep(100);
            lead.log("MEETING", 100);
            timeclock.put("MEETING", timeclock.get("MEETING") + 100);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Clock.getString() + "\tTeam Lead " + lead.my_team.getTeamNumber() + "-" + lead.getID() + " gets answer from Manager.");
        inMeeting = false;
        managerWaitList.remove();
        lead.setInMeeting(false);
    }

    /**
     * The Team Lead and question Asker wait in line for the Manager to answer their question. If the Manager is on
     *      lunch or in a meeting, the Team Lead and Asker wait for the Manager to return.
     * @param lead - the Team Lead asking the question.
     * @param asker - the original asker of the question.
     */
    public static synchronized void answerQuestion(Employee lead, Employee asker) {
        lead.setInMeeting(true);
        asker.setInMeeting(true);
        managerWaitList.add(lead);
        // Team Lead waits in line for the Manager.
        while(lead != managerWaitList.peek()) {
            try {
                lead.sleep(50);
                lead.log("WAITING", 50);
                asker.sleep(50);
                asker.log("WAITING", 50);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Team Lead waits for the Manager to return.
        if(inMeeting || atLunch) {
            System.out.println(Clock.getString() + "\tTeam Lead " + lead.my_team.getTeamNumber() + "-" + lead.getID() + " waits for Manager to return.");
            try {
                lead.sleep(50);
                lead.log("WAITING", 50);
                asker.sleep(30);
                asker.log("WAITING", 50);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Team Lead asks question.
        System.out.println(Clock.getString() + "\tTeam Lead " + lead.my_team.getTeamNumber() + "-" + lead.getID() + " asks the Manager a question.");
        inMeeting = true;
        isWorking = false;
        try {
            // Manager answers question.
            Thread.sleep(100);
            lead.log("MEETING", 100);
            asker.log("MEETING", 100);
            timeclock.put("MEETING", timeclock.get("MEETING") + 100);
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Clock.getString() + "\tTeam Lead " + lead.my_team.getTeamNumber() + "-" + lead.getID() + " and Employee " + asker.my_team.getTeamNumber() + "-" + asker.getID() + " get answer from Manager.");
        inMeeting = false;
        managerWaitList.remove();
        lead.setInMeeting(false);
        asker.setInMeeting(false);
    }
}
