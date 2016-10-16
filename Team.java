/*
 * @PROJECT: SWEN-342 | Project 1
 *
 * @AUTHOR: Ben Meyers
 * @EMAIL: bsm9339@rit.edu
 *
 * @AUTHOR: Asma Sattar
 * @EMAIL: aas3799rit.edu
 */

public class Team {

    public final int TEAM_NUMBER;

    // Team Leader
    public final Employee TEAM_LEADER;

    // Team Members
    public final Employee[] TEAM_MEMBERS;

    /**
     *
     * @param team_number
     * @param members
     */
    public Team(int team_number Employee lead, Employee[] members) {
        this.TEAM_NUMBER = team_number;
        this.TEAM_LEADER = lead;
        // Index 0 (Member #1) must be the Team Leader
        this.TEAM_MEMBERS = [TEAM_LEADER, members[0], members[1], members[2]]
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
    public Employee[] getTeamMembers() {
        return this.TEAM_MEMBERS;
    }
}