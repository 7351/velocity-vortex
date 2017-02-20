package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

/**
 * Created by Dynamic Signals on 2/18/17.
 */

public class PID {
    public double p, i, d;

    public PID(double p, double i, double d) {
        this.p = p;
        this.i = i;
        this.d = d;
    }

    /**
     * Default PID variables, to be tweaked after the correct variables are found
     */
    public PID() {
        this.p = 0.005;
        this.i = 0;
        this.d = 0;
    }
}
