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

    private static EncoderGyroTurn instance;

    private StateMachine stateMachine;
    private final static double TIMEOUT = 3; // Timeout time in secs
    private final static double TOLERANCE = 2; // Tolerance in degrees, goes in both ways
    private int completedCounter = 0;

    private EncoderTurn encoderTurn; // Our encoderturn object

    private ElapsedTime creationTime = new ElapsedTime();

    int stage = 0;

    public static EncoderGyroTurn createTurn(StateMachine stateMachine, AHRS navx, DriveTrain driveTrain, double targetDegree) {
        if (instance == null) {
            instance = new EncoderGyroTurn(stateMachine, navx, driveTrain, targetDegree);
        }
        instance.isCompleted();
        return instance;
    }

    /**
     * Gyro Assisted and calculated encoder turn, it calculates how much counts to complete the turn
     *
     * @param navx         the AHRS instance
     * @param driveTrain   instance of driveTrain
     * @param targetDegree the degree you want to turn to
     */

    private EncoderGyroTurn(StateMachine stateMachine, AHRS navx, DriveTrain driveTrain, double targetDegree) {
        this.driveTrain = driveTrain;
        this.navx = navx;
        this.stateMachine = stateMachine;

        detail = new GyroUtils.GyroDetail(navx, targetDegree); // FIXME: 3/20/17 

        DbgLog.msg("Initial degrees off " + detail.degreesOff);

        creationTime.reset();
    }

    @Override
    public void run() {
        detail.updateData();
        
        // TODO Get more data and make a better curve

        if (stage == 0) { // Do our action
            if (encoderTurn == null) {
                double counts = 0;
                double cpd = ((-0.0009780068)*detail.degreesOff) + (0.1097506219*detail.degreesOff) + 9.094355452;
                counts = detail.degreesOff * cpd;
                encoderTurn = new EncoderTurn(driveTrain, counts, detail.turnDirection, true); // Entering raw counts
                encoderTurn.setPower(0.65);
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
            if (detail.degreesOff > TOLERANCE) {
                stage = 0; // Recalculate if we aren't in tolerance
                completedCounter = 0;
            }
            if (detail.degreesOff < TOLERANCE) {
                completedCounter++;
                if (completedCounter > 200) {
                    stage++; // Finish through and let isCompleted do its job
                }

            }
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
        stateMachine.next();
        instance = null;
    }
}