package com.example.poo.utils;

/**
 * Class to implement a timer
 */
public class    Clock {
    private java.time.Clock time;
    private long start;
    private long end;
    private double timeToPass;

    /**
     * Constructor of the clock class
     * Automatically start the clock
     * @param timestep Time before the clock end
     */
    public Clock(double timestep)
    {
        this.time = java.time.Clock.systemDefaultZone();
        this.startClock();
        this.end = 0;
        this.timeToPass = timestep;
    }

    /**
     * Gets the duration of the clock
     * @return Int with the time
     */
    public double getTimeToPass() { return this.timeToPass;}

    /**
     * Restart the clock
     */
    public void startClock()
    {
        this.start = this.time.millis();
    }

    /**
     * Gets the time at which the clock started
     * @return A long with the starting time
     */
    public long getStart() { return this.start;}

    /**
     * Get the last time the clock ended
     * @return a long with the last ending time
     */
    public long getEnd() {return this.end;}

    /**
     * Gets the time elapsed between the start of the current clock and the call to this method
     * @return A long with the time elapsed from the begining
     */
    public long getElapsedTime()
    {
        this.end = this.time.millis();
        return this.end - this.start;
    }

    /**
     * Check if the clock is complete and restart it if it is
     * This is the method that needs to be used when using a clock
     * @return A boolean that is true if the clock is completed, false if it isn't
     */
    public boolean  isComplete()
    {
        if(this.getElapsedTime() >= this.timeToPass)
        {
            this.startClock();
            return true;
        } else
            return false;
    }
}
