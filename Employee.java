/*
 * @PROJECT: SWEN-342 | Project 1
 *
 * @AUTHOR: Ben Meyers
 * @EMAIL: bsm9339@rit.edu
 *
 * @AUTHOR: Asma Sattar
 * @EMAIL: aas3799@rit.edu
 */

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.Random;

/**
 *
 */
public class Employee extends Thread {

    private final int ID;
    private final CountDownLatch LEAD_MEETING_SIGNAL, STATUS_MEETING_SIGNAL;
    public Team my_team;
	private Semaphore conference_room;
	private int delay = (int) (Math.random() * 300); //8:00-8:30PM (0 to 30mins, which is 300mill, which is  0.30s)
	private int lunch = (int) (Math.random() * (300 - delay)) + 300;
    private boolean atLunch = false;
    private boolean inMeeting = false;
    private boolean isWorking = false;
    private Map<String, Integer> timeclock = new HashMap<String, Integer>();
    private String startTime;
    private String endTime;

    /**
     *
     * @param id - the employee ID for this employee.
     * @param signal - the CountDownLatch for the LEAD Meeting.
     * @param conference_room - the lock on the Conference Room.
     * @param statusMeeting_signal - the CountDownLatch for the STATUS Meeting.
     */
    public Employee(int id, CountDownLatch signal, Semaphore conference_room, CountDownLatch statusMeeting_signal) {
        this.ID = id;
        this.LEAD_MEETING_SIGNAL = signal;
        this.conference_room = conference_room;
        this.STATUS_MEETING_SIGNAL = statusMeeting_signal;

        // Setup the timeclock for logging of hours.
        timeclock.put("LUNCH", 0);
        timeclock.put("MEETING", 0);
        timeclock.put("WAITING", 0);
    }

    /**
     * Handles all of the daily activities for Employees and Team Leads. Basic schedule:
     *      8:00 - 8:30     Employees arrive.
     *      8:30 - 8:45     Team Leads meet with Manager.
     *      8:45 - 12:00    Team Leads work and answer Employee questions.
     *                      Employees work and ask questions.
     *      12:00 - 1:00    Employees and Team Leads eat lunch.
     *      1:00 - 4:00     Team Leads work and answer Employee questions.
     *                      Employees work and ask questions.
     *      4:00 - 4:15     Employees and Team Leads finish up what they're doing.
     *      4:15 - 4:30     All Employees meet with Manager for Status Meeting.
     *      4:30 - 5:00     Employees leave.
     */
    public void run() {

        try {
            Thread.sleep(delay);
            System.out.println(Clock.getString() + "\tEmployee " + this.my_team.getTeamNumber() + "-" + this.getID() + " shows up at the office.");
            startTime = Clock.getString();
            /* If the Employee is a team lead */
            if (my_team.getTeamLeader() == this){
                /* On arrival they knock on Mangers door for a 15mins meeting */
                System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.getID() + " knocks on Manager's door.");
                this.inMeeting = true;
                LEAD_MEETING_SIGNAL.countDown();
                LEAD_MEETING_SIGNAL.await();
                /*  one minute = 10ms so 15mins = (10ms * 15 ) = 150ms or 0.15 seconds */
                Thread.sleep(150);
                this.timeclock.put("MEETING", this.timeclock.get("MEETING") + 150);
                System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.getID() + " leaves meeting with Manager.");
                this.inMeeting = false;
             }

            //Team lead(1) & Members(3) meetings
            //After the morning mangers meeting, the team leads wait for all the members of their team to arrive.
            //waiting for all members to arrive before meeting starts.
            System.out.println(Clock.getString() + "\tEmployee " + this.my_team.getTeamNumber() + "-" + this.getID() + " is waiting for all members of Team " + this.my_team.getTeamNumber() + ".");
            my_team.getTeamSignal().countDown();
            my_team.getTeamSignal().await();
            System.out.println(Clock.getString() + "\tEmployee " + this.my_team.getTeamNumber() + "-" + this.getID() + " arrives at conference room.");

            if(my_team.getTeamLeader() == this){
                //try to get Semaphore
                conference_room.acquire();
                //Enters the room with all team members
                System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.getID() + " enters conference room with all members of Team " + this.my_team.getTeamNumber() + ".");
                this.inMeeting = true;
                my_team.getConferenceRoomSignal().countDown();
                 /*  one minute = 10ms so 15mins = (10ms * 15 ) = 150ms or 0.15 seconds */
                Thread.sleep(150);
                this.timeclock.put("MEETING", this.timeclock.get("MEETING") + 150);
                //release theSemaphore
                System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.getID() + " leaves conference room.");
                this.inMeeting = false;
                conference_room.release();
            }else{
                my_team.getConferenceRoomSignal().await();
                System.out.println(Clock.getString() + "\tEmployee " + this.my_team.getTeamNumber() + "-" + this.getID() + " enters conference room.");
                this.inMeeting = true;
                /*  one minute = 10ms so 15mins = (10ms * 15 ) = 150ms or 0.15 seconds */
                Thread.sleep(150);
                this.timeclock.put("MEETING", this.timeclock.get("MEETING") + 150);
                System.out.println(Clock.getString() + "\tEmployee " + this.my_team.getTeamNumber() + "-" + this.getID() + " leaves meeting in conference room.");
                this.inMeeting = false;
            }

            // Resume normal daily activities.
            while(Clock.getHours() < 12){
                if(!this.atLunch && !this.inMeeting && !this.my_team.getTeamLeader().atLunch && !this.my_team.getTeamLeader().inMeeting) {
                    /**
                     * Allowing Employees to ask too many questions dramatically increases the output of the simulation,
                     *      making it hard to analyze. The value of 150000 is entirely arbitrary, but we believe it
                     *      limits the number of questions that can be asked to a reasonable, realistic number.
                     * The value of 7 is also arbitrary, any integer between 0 and 150000 will do.
                     */
                    if(new Random().nextInt(150000) == 7) {
                        this.askQuestion();
                        this.yield();
                    }
                    this.yield();
                }else{
                    Thread.sleep(100);
                }
            }
            //12:00AM LUNCH TIME
            if(my_team.getTeamLeader() == this){
                System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.getID() + " starts lunch.");
                this.atLunch = true;
                Thread.sleep(lunch); //at least 30mins but no more than 1hrs
                this.timeclock.put("LUNCH", this.timeclock.get("LUNCH") + lunch);
                System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.getID() + " finishes lunch.");
                this.atLunch = false;
            }else {
                System.out.println(Clock.getString() + "\tEmployee " + this.my_team.getTeamNumber() + "-" + this.getID() + " starts lunch.");
                this.atLunch = true;
                Thread.sleep(lunch); //at least 30mins but no more than 1hrs
                this.timeclock.put("LUNCH", this.timeclock.get("LUNCH") + lunch);
                System.out.println(Clock.getString() + "\tEmployee " + this.my_team.getTeamNumber() + "-" + this.getID() + " finishes lunch.");
                this.atLunch = false;
            }

            while(Clock.getHours() < 16){
                if(!this.atLunch && !this.inMeeting && !this.my_team.getTeamLeader().atLunch && !this.my_team.getTeamLeader().inMeeting) {
                    /**
                     * Allowing Employees to ask too many questions dramatically increases the output of the simulation,
                     *      making it hard to analyze. The value of 150000 is entirely arbitrary, but we believe it
                     *      limits the number of questions that can be asked to a reasonable, realistic number.
                     * The value of 7 is also arbitrary, any integer between 0 and 150000 will do.
                     */
                    if(new Random().nextInt(150000) == 7) {
                        this.askQuestion();
                        Thread.yield();
                    }
                    Thread.yield();
                }else{
                    Thread.sleep(100);
                }
            }

            // Stop answering/asking questions 15 minutes before the Status meeting and clean up.
            if(this.my_team.getTeamLeader() == this) {
                if(this.inMeeting) {
                    System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.getID() + " stops answering questions and cleans up.");
                    this.inMeeting = false;
                    // Spend a random amount of time between 1 and 15 minutes cleaning up.
                    Thread.sleep(new Random().nextInt((15 - 1 + 1)*10));
                }else {
                    System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.getID() + " cleans up before Status Meeting.");
                    // Spend a random amount of time between 1 and 15 minutes cleaning up.
                    Thread.sleep(new Random().nextInt((15 - 1 + 1)*10));
                }
            }else {
                if(this.inMeeting) {
                    System.out.println(Clock.getString() + "\tEmployee " + this.my_team.getTeamNumber() + "-" + this.getID() + " stops asking questions and cleans up.");
                    this.inMeeting = false;
                    // Spend a random amount of time between 1 and 15 minutes cleaning up.
                    Thread.sleep(new Random().nextInt((15 - 1 + 1)*10));
                }else {
                    System.out.println(Clock.getString() + "\tEmployee " + this.my_team.getTeamNumber() + "-" + this.getID() + " cleans up before Status Meeting.");
                    // Spend a random amount of time between 1 and 15 minutes cleaning up.
                    Thread.sleep(new Random().nextInt((15 - 1 + 1)*10));
                }
            }
            Thread.yield();

            //Status update meeting
            if(this.my_team.getTeamLeader() == this) {
                System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.getID() + " arrives at Status Meeting.");
            }else {
                System.out.println(Clock.getString() + "\tEmployee " + this.my_team.getTeamNumber() + "-" + this.getID() + " arrives at Status Meeting.");
            }
            this.inMeeting = true;
            STATUS_MEETING_SIGNAL.countDown();
            STATUS_MEETING_SIGNAL.await();

            Thread.sleep(150); //15 minute meeting
            this.timeclock.put("MEETING", this.timeclock.get("MEETING") + 150);
            if(this.my_team.getTeamLeader() == this) {
                System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.getID() + " leaves Status Meeting.");
            }else {
                System.out.println(Clock.getString() + "\tEmployee " + this.my_team.getTeamNumber() + "-" + this.getID() + " leaves Status Meeting.");
            }
            this.inMeeting = false;

            // Employees wait for the 8 hour mark.
            while(Clock.getCurrentTime() < (4800 + delay +lunch)){
                if(this.my_team.getTeamLeader() == this) {
                    if(!this.isWorking) {
                        System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.getID() + " ties up loose ends.");
                        this.isWorking = true;
                    }else {
                        // Wait another minute before trying to go home.
                        Thread.sleep(10);
                    }
                }else {
                    if(!this.isWorking) {
                        System.out.println(Clock.getString() + "\tEmployee " + this.my_team.getTeamNumber() + "-" + this.getID() + " ties up loose ends.");
                        this.isWorking = true;
                    }else {
                        // Wait another minute before trying to go home.
                        Thread.sleep(10);
                    }
                }
            }

            // Employees go home!
            if(this.my_team.getTeamLeader() == this) {
                System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.getID() + " leaves to go home!");
                endTime = Clock.getString();
            }else {
                System.out.println(Clock.getString() + "\tEmployee " + this.my_team.getTeamNumber() + "-" + this.getID() + " leaves to go home!");
                endTime = Clock.getString();
            }
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param myTeam - the team to assign this Employee to.
     */
    public void setTeam(Team myTeam){ this.my_team = myTeam; }

    /**
     * @return this Employees ID.
     */
    public int getID() { return ID; }

    /**
     * @return the time that this Employee arrived.
     */
    public String getStartTime() { return startTime; }

    /**
     * @return the time that this Employee went home.
     */
    public String getEndTime() { return endTime; }

    /**
     * @return the number of minutes this Employee spent eating lunch.
     */
    public int getLunchTime() { return this.timeclock.get("LUNCH"); }

    /**
     * @return the number of minutes this Employee spent in meetings.
     */
    public int getMeetingTime() { return this.timeclock.get("MEETING"); }

    /**
     * @return the number of minutes this Employee spent waiting for the Manager.
     */
    public int getWaitingTime() { return this.timeclock.get("WAITING"); }

    /**
     * @param b - the meeting status to set for this Employee.
     */
    public void setInMeeting(boolean b) { this.inMeeting = b; }

    /**
     * @param key - the timeclock entry to update.
     * @param val - the new value for key.
     */
    public void log(String key, int val) {
        this.timeclock.put(key, this.timeclock.get(key) + val);
    }

    /**
     * Team Leads and Employees can ask questions. If an Employee asks their Team Lead a question, there is a 50%
     *      chance that the Team Lead knows the answer. If the Team Lead does not know the answer, the Team Lead and
     *      the Employee go to the Manager and ask the question.
     */
    public synchronized void askQuestion() {
        if(my_team.getTeamLeader() == this) {
            System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.getID() + " has a question for the Manager.");
            System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.my_team.getTeamLeader().getID() + " goes to Manager's office to ask question.");
            if(!Manager.inMeeting() && !Manager.isAtLunch()) {
                Manager.answerQuestion(this);
            }else{
                try {
                    Thread.sleep(100);
                    this.timeclock.put("WAITING", this.timeclock.get("WAITING") + 100);
                }catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else {
            System.out.println(Clock.getString() + "\tEmployee " + this.my_team.getTeamNumber() + "-" + this.getID() + " asks their Team Lead a question.");
            // 50% chance that the team lead knows the answer.
            if(new Random().nextBoolean()) {
                System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.my_team.getTeamLeader().getID() + " knew answer to question from Employee " + this.my_team.getTeamNumber() + "-" + this.getID() + ".");
            }else {
                System.out.println(Clock.getString() + "\tTeam Lead " + this.my_team.getTeamNumber() + "-" + this.my_team.getTeamLeader().getID() + " and Employee " + this.my_team.getTeamNumber() + "-" + this.getID() + " go to Manager's office to ask question.");
                if(!Manager.inMeeting() && !Manager.isAtLunch()) {
                    Manager.answerQuestion(this.my_team.getTeamLeader(), this);
                }else{
                    try {
                        Thread.sleep(100);
                        this.timeclock.put("WAITING", this.timeclock.get("WAITING") + 100);
                    }catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}