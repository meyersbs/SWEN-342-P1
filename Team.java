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

public class Team {

    public final int TEAM_NUMBER;

    // Team Leader
    public final Employee TEAM_LEADER;

    // Team Members
    ArrayList<Employee> TEAM_MEMBERS = new ArrayList<Employee>();

    /**
     *
     * @param team_number
     * @param members
     */
    public Team(int team_number, ArrayList<Employee> members) {
        this.TEAM_NUMBER = team_number;
        // Index 0 (Member #1) must be the Team Leader
        TEAM_MEMBERS.add(members.get(0));
        members.remove(0);
        TEAM_MEMBERS.add(members.get(0));
        members.remove(0);
        TEAM_MEMBERS.add(members.get(0));
        members.remove(0);
        TEAM_MEMBERS.add(members.get(0));
        members.remove(0);

        this.TEAM_LEADER = this.TEAM_MEMBERS.get(0);
    }

    /**
     *
     * @return
     */
    public int getTeamNumber() {
        return this.TEAM_NUMBER;
    }

    /**
     *
     * @return
     */
    public Employee getTeamLeader() {
        return this.TEAM_LEADER;
    }

    /**
     *
     * @return
     */
    public ArrayList<Employee> getTeamMembers() {
        return this.TEAM_MEMBERS;
    }
}