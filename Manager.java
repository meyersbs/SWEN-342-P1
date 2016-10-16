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
public class Manager extends Thread {

    public final Team[] TEAMS;

    /**
     *
     * @param teams
     */
    public Manager(Team[] teams) {
        this.TEAMS = teams;
    }
}