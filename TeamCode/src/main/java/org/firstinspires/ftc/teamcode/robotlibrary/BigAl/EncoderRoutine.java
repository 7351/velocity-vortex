package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

/**
 * Created by Dynamic Signals on 12/29/2016.
 */

public interface EncoderRoutine {
    double GearRatio = 40;
    double SprocketRatio = 1.5;

    void run();
    boolean isCompleted();
    void completed();
}
