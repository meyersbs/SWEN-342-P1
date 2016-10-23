/*
 * @PROJECT: SWEN-342 | Project 1
 *
 * @AUTHOR: Ben Meyers
 * @EMAIL: bsm9339@rit.edu
 *
 * @AUTHOR: Asma Sattar
 * @EMAIL: aas3799rit.edu
 */
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * A Team is really just a list of Employees associated with a team number.
 */
public class Team {

    public final int TEAM_NUMBER;

    // Team Leader
    public final Employee TEAM_LEADER;
    public final CountDownLatch TEAM_SIGNAL, CONFERENCE_ROOM;

    // Team Members
    ArrayList<Employee> TEAM_MEMBERS = new ArrayList<Employee>();

    /**
     *
     * @param team_number - the number of this team (1-3)
     * @param members - the members of this team (1-4)
     */
    public Team(int team_number, ArrayList<Employee> members) {
        this.TEAM_NUMBER = team_number;
        // Index 0 (Member #1) must be the Team Leader
        TEAM_MEMBERS.add(members.get(0));
        TEAM_MEMBERS.add(members.get(1));
        TEAM_MEMBERS.add(members.get(2));
        TEAM_MEMBERS.add(members.get(3));
        
        /* Team Lead(1) and Members(3)latch*/
        this.TEAM_SIGNAL = new CountDownLatch(4);
        /* Team Lead(1) enters conference room*/
        this.CONFERENCE_ROOM = new CountDownLatch(1);
        this.TEAM_LEADER = this.TEAM_MEMBERS.get(0);
    }

    public CountDownLatch getConferenceRoomSignal(){ return this.CONFERENCE_ROOM; }

    public CountDownLatch getTeamSignal(){ return this.TEAM_SIGNAL; }

    /**
     *
     * @return the number of this team.
     */
    public int getTeamNumber() { return this.TEAM_NUMBER; }

    /**
     *
     * @return the leader of this team.
     */
    public Employee getTeamLeader() { return this.TEAM_LEADER; }

    /**
     *
     * @return a list of the members in this team.
     */
    public ArrayList<Employee> getTeamMembers() { return this.TEAM_MEMBERS; }
}