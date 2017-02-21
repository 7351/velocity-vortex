package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.ftccommon.DbgLog;

import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils.Direction.CLOCKWISE;
import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils.Direction.COUNTERCLOCKWISE;

/**
 * Created by Dynamic Signals on 2/19/2017.
 */

public class EncoderGyroTurn implements Routine {

    private DriveTrain driveTrain;
    private AHRS navx;

    private double targetDegree = 0;
    private double degreesOff = 0;

    private boolean isTurnNeeded = true;

    private GyroUtils.Direction turnDirection;

    private EncoderTurn encoderTurn;

    /**
     * Gyro Assisted and calculated encoder turn, it calculates how much counts to complete the turn
     *
     * @param navx         the AHRS instance
     * @param driveTrain   instance of driveTrain
     * @param targetDegree the degree you want to turn to
     */

    public EncoderGyroTurn(AHRS navx, DriveTrain driveTrain, double targetDegree) {
        this.driveTrain = driveTrain;
        this.targetDegree = targetDegree;
        this.navx = navx;

        double movedZero = GyroUtils.temporaryZero(navx, targetDegree); // This is basically spoofedZero, it is used to cross over -180 & 180

        if (movedZero > 0 && movedZero < 180) { // We need to turn counterclockwise
            degreesOff = movedZero; // Its just the straight degrees
            turnDirection = COUNTERCLOCKWISE;
        }

        if (movedZero >= 180 && movedZero < 360) { // We need to turn clockwise
            degreesOff = Math.abs(90 - (movedZero - 270)); // We need to find abs of 90 - (degree - 270) to get the degrees off
            turnDirection = CLOCKWISE;
        }

        encoderTurn = new EncoderTurn(driveTrain, degreesOff, turnDirection, true); // Create the EncoderTurn object

        if (encoderTurn.encoderCounts < 5) isTurnNeeded = false; // If the encoder counts is less than 5, we don't need to turn

        DbgLog.msg("Curr: " + movedZero + " Left: " + degreesOff + " Direction: " + turnDirection.toString().toLowerCase() + " Counts: " + encoderTurn.encoderCounts);
    }

    @Override
    public void run() {

        encoderTurn.run(); // Simply run the encoderTurn

    }

    @Override
    public boolean isCompleted() {
        boolean done = encoderTurn.isCompleted();
        if (!isTurnNeeded) done = true; // If it isn't worth turning
        if (done) completed(); // Stop robot when it's done
        return done;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
    }
}
