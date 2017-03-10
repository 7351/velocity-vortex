package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils.Direction.CLOCKWISE;
import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils.Direction.COUNTERCLOCKWISE;

/**
 * Created by Dynamic Signals on 2/19/2017.
 */

public class EncoderGyroTurn implements Routine {

    private DriveTrain driveTrain;
    private AHRS navx;
    public GyroUtils.GyroDetail detail;

    private final static double TIMEOUT = 3; // Timeout time in secs
    private final static double TOLERANCE = 2; // Tolerance in degrees, goes in both ways

    private EncoderTurn encoderTurn; // Our encoderturn object

    private ElapsedTime creationTime = new ElapsedTime();

    int stage = 0;

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

        detail = new GyroUtils.GyroDetail(navx, targetDegree);

        creationTime.reset();

        DbgLog.msg("Curr: " + detail.movedZero + " Left: " + detail.degreesOff + " Direction: " + detail.turnDirection.toString().toLowerCase() + " Counts: " + encoderTurn.encoderCounts);
    }

    @Override
    public void run() {
        detail.updateData();

        if (stage == 0) { // Do our action
            if (encoderTurn == null) {
                // Do the math here for the turn
                double counts = 0;
                encoderTurn = new EncoderTurn(driveTrain, counts, CLOCKWISE, true); // Entering raw counts
                encoderTurn.run();
            }
            if (encoderTurn.isCompleted()) {
                driveTrain.stopRobot();
                stage++;
                encoderTurn = null;
            }

        }
        if (stage == 1) {
            // Check ourselves and recalculate
            if (detail.degreesOff > TOLERANCE) stage = 0; // Recalculate if we aren't in tolerance
            if (detail.degreesOff < TOLERANCE)
                stage++; // Finish through and let isCompleted do its job
        }

    }

    @Override
    public boolean isCompleted() {
        boolean done = false;

        if (stage == 2) {
            done = true;
        }

        if (creationTime.time() > TIMEOUT) {
            done = true;
        }

        if (done) {
            completed(); // Stop robot when it's done
        } else {
            run();
        }
        return done;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
    }
}