package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.util.Range;

import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils.Direction.CLOCKWISE;
import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils.Direction.COUNTERCLOCKWISE;

/**
 * Created by Dynamic Signals on 2/19/2017.
 */

public class EncoderGyroTurn implements Routine {

    private DriveTrain driveTrain;
    private AHRS navx;

    private boolean isTurnNeeded = true;

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
        this.navx = navx;

        GyroUtils.GyroDetail detail = new GyroUtils.GyroDetail(navx, targetDegree);

        encoderTurn = new EncoderTurn(driveTrain, detail.degreesOff, detail.turnDirection, true); // Create the EncoderTurn object

        if (encoderTurn.encoderCounts < 5) isTurnNeeded = false; // If the encoder counts is less than 5, we don't need to turn

        DbgLog.msg("Curr: " + detail.movedZero + " Left: " + detail.degreesOff + " Direction: " + detail.turnDirection.toString().toLowerCase() + " Counts: " + encoderTurn.encoderCounts);
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
