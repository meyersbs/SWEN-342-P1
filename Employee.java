/*
 * @PROJECT: SWEN-342 | Project 1
 *
 * @AUTHOR: Ben Meyers
 * @EMAIL: bsm9339@rit.edu
 *
 * @AUTHOR: Asma Sattar
 * @EMAIL: aas3799rit.edu
 */

/**
 *
 */
public class Employee extends Thread {

    public final int TEAM_NUMBER;
    public final boolean IS_TEAM_LEADER;
    public final Manager MANAGER;

    /**
     *
     * @param team
     * @param status
     * @param manager
     */
    public Employee(int team, boolean status, Manager manager) {
        this.TEAM_NUMBER = team;
        this.IS_TEAM_LEADER = status;
        this.MANAGER = manager;
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
    public int isLeader() {
        return this.IS_TEAM_LEADER;
    }

    /**
     *
     * @return
     */
    public Manager managedBy() {
        return this.MANAGER;
    }
}