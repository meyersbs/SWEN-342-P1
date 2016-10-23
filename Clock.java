/*
 * @PROJECT: SWEN-342 | Project 1
 *
 * @AUTHOR: Ben Meyers
 * @EMAIL: bsm9339@rit.edu
 *
 * @AUTHOR: Asma Sattar
 * @EMAIL: aas3799rit.edu
 */

public class Clock {
	private static int startTime;
	
	/**
     * 
     * @return - the current time
     */
    public static int getCurrentTime(){
    	return (int) (System.currentTimeMillis() - startTime);
    }
    
    public static int getHours(){
    	int minutes = getCurrentTime()/10;
    	int hours = minutes/60 + 8;
    	return hours;
    }
    public static void startClock(){
        startTime = (int) System.currentTimeMillis();
    }
    public static String getString(){
    	int minutes = getCurrentTime()/10;
    	int hours = minutes/60 + 8; 
    	int leftoverMinutes = minutes%60;
    	int nonMilitaryHours = hours%12;
    	String am_pm;
    	
    	//Special case for 12:00%12
    	if(nonMilitaryHours == 0){
    		nonMilitaryHours = 12;
    	}
    	if(hours < 12){
    		am_pm = "AM";
    	}else{
    		am_pm = "PM";
    	}
    	return String.format("%d:%02d%s", nonMilitaryHours, leftoverMinutes, am_pm ); 
    }
}
