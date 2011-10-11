/*******************************************************************************
|	Copyright 2006	Jason Whatson. All rights reserved.                    |
*******************************************************************************/

/*
 * Author: Jason Whatson
 * File: Timer.java
 * Purpose: Be able to time things with a instance of this timer.
 * multiple timer instance can be created to time mulitiple things.
 * a timer can be paused, stoped and formatted (limited)
 * Created on: 6 December 2006, 10:58
 */

package timer;

import java.util.Calendar;
import java.util.Date;

public class Timer {
    
    //Holds reference to the time when the timer has started
    private long CurrentTime; 
    //Hold the ammount of time passed since the timer has started
    private long TimePassed;
    //Creates a new calendar instance, needed for time formating
    private static Calendar cn = Calendar.getInstance(); //Tempary for formatAsDate()
    //Holds wheather the timer is stoped
    private boolean Stopped;
    //Holds wheather the timer is paused
    private boolean Paused = false;
    //Creates a new instance of its self :o so we can calculate the pause time
    private Timer t1;
    //Holds the ammount of time the timer has been paused.
    private long PauseTime = 0;

    /** Creates a new instance of Timer 
     gets the time the timer was created (start time) and sets it to variable
     currentTime*/
    public Timer() {CurrentTime = System.currentTimeMillis();}
    
    //Sets the timer to stopped
    public void stop(){Stopped = true;}
    
    //Pauses or resumes a timer
    public void pause(boolean paused){
        //If we are pausing a timer start timeing the pause
        if (paused == true){
            t1 = new Timer();
        }else{
            //If we are unpausing the timer. Get hold long it was paused for
            //and add it to the total pause time
            this.PauseTime += t1.getTimePassed();
        }
        this.Paused = paused;
    }
    
    //Gets the total ammout of time the timer has been paused for
    public long getPauseTime(){ return this.PauseTime;}

    //Gets the pause state of the timer
    public boolean getPausedState(){return this.Paused;}
    
    //Calculates and returns the ammount of time the timer has been running for 
    public long getTimePassed(){
       //Only do the calculation if we are not stoped or paused
       if (this.Stopped == false && this.Paused == false){
            TimePassed = System.currentTimeMillis() - this.CurrentTime;
            //This cant be here because the offset is deducted to much being here
            //- java.util.TimeZone.getDefault().getRawOffset(); 
       }
       return TimePassed;
    }
    
    /*Takes a long value representing a date and formats it into 
     *hours,minutes,seconds,miliseconds*/
    public static String formatAsDate(long date){
        cn.setTime(new Date(date));
        
        return cn.get(Calendar.HOUR_OF_DAY) + ":" + cn.get(Calendar.MINUTE) + 
        ":" + cn.get(Calendar.SECOND)
        + ":" + cn.get(Calendar.MILLISECOND);
    }
}
