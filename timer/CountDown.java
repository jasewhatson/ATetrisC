//TODO: Make a start method the timer, rather than starting it on constructor
//TODO: Javadoc comment
//TODO: Make an array of listners rather than just one
//TODO: A restart method
//TODO: Unregistar listner method

/*******************************************************************************
|	Copyright 2006	Jason Whatson. All rights reserved.                    |
*******************************************************************************/

/*
 * Author: Jason Whatson
 * File: Timer.java
 * Purpose: Count a timer down from a defined time to 0. Once a timer is 0 
 * notify all registered listeners. Allow a count down to be paused and continued.
 * Also the time left can be retrived.
 * Created on: 6 December 2006, 16:32
 * Version: 1.0;
 */
package timer;

import java.util.Calendar;
import java.util.Date;

public class CountDown{
       
    //Holds the time that it will be when the time runs out
    private Calendar TimeToGetTo;
    
    //Holds a timer instance, used to get pause time
    private Timer pt;
    //Holds the ammount of time the countdown has been paused
    private long PauseTime = 0;
    //Holds the amount of time to go until times up
    private long TimeToGo = 0;
    //Holds the pause state of the count down
    private boolean Paused;
    
    /*Ideally an arraylist but we can get away with it because this application
    only ever uses one instance of the count down timer. If a application needs
    more instances it will need to be changed to some kind of list*/
    private Alarmable countDownListners;
    
    /** Creates a new instance of CountDown */
    public CountDown(int hours, int minutes, int seconds,int milisecs) {
 
        //Sets the 'Time to get to' as the current time
        TimeToGetTo = Calendar.getInstance();
        
        /*Sets the time until alarm by getting the hours, mins etc and adding it to the user supplied values
         im preaty sure the following needs to be in this order*/
        TimeToGetTo.set(Calendar.HOUR_OF_DAY,TimeToGetTo.get(Calendar.HOUR_OF_DAY) + hours); //Hours until alarm
        TimeToGetTo.set(Calendar.MINUTE,TimeToGetTo.get(Calendar.MINUTE) + minutes); //Minutes until alarm
        TimeToGetTo.set(Calendar.SECOND,TimeToGetTo.get(Calendar.SECOND) + seconds); //Seconds until alarm
        TimeToGetTo.set(Calendar.MILLISECOND,TimeToGetTo.get(Calendar.MILLISECOND) + milisecs); //mili seconds until alarm
    }
    
    //Return the amount of time until the times up
    public long getTimeToGo(){
        /*If the count down isnt paused work out the amount of time left by 
        taking the current time away from the time to get to.*/
        if (this.Paused == false){
            this.TimeToGo = (TimeToGetTo.getTime().getTime() + this.PauseTime) - System.currentTimeMillis();
            //If there is no time left, fire the 'no time left event to eveyone listening'
            if (this.TimeToGo <= 0){
                this.fireNoTimeLeftEvent();
            }
        }
        //Return the amount of time to go
        return this.TimeToGo;
    }
    
    /*Takes a long value representing a date and formats it into 
     *hours,minutes,seconds,miliseconds*/
    public String formatAsDate(long date){
        return Timer.formatAsDate(date);
    }
    
    //Gets the pause state of the timer
    public boolean getPausedState(){return this.Paused;}
    
    //Pauses or resumes a timer
    public void pause(boolean paused){
        if (paused == true){
            pt = new Timer();
        }else{
            //If we are unpausing the timer. Get how long it was paused for
            //and add it to the total pause time
            this.PauseTime += pt.getTimePassed();
        }
        this.Paused = paused;
    }    
    
    //Loops (should) through all the registered listeners and creates and gives
    //them a new TimerEvent
    private void fireNoTimeLeftEvent(){   
        this.countDownListners.timesUp(new TimerEvent(this));
    }
    
    //Class's that want to listen for TimerEvents need to register here
    public synchronized void addTimesUpListener(Alarmable a) {
        this.countDownListners = a;
    }
}
