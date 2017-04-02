package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by leo on 3/25/17.
 */

public class GyroTurn implements Routine {

    private static GyroTurn instance;

    private DriveTrain driveTrain;
    private AHRS navx;

    private StateMachineOpMode opMode;
    public GyroUtils.GyroDetail detail;

    private ElapsedTime creationTime = new ElapsedTime(); // Used for timeout failsafe
    private ElapsedTime waitTime = new ElapsedTime(); // Used for wait in between to catch up gyro
    private final static double TIMEOUT = 2.5; // Timeout time in secs
    private final static double TOLERANCE = 3;
    private final static double MinMotor = 0.2, MaxMotor = 0.5;

    private int stage = 0;

    /*
    This class is basically a wrapper for EncoderTurn in the new StateMachine format
     */

    public static GyroTurn createTurn(StateMachineOpMode opMode, double targetDegree) {
        if (instance == null) {
            instance = new GyroTurn(opMode, targetDegree);
        }
        instance.isCompleted();
        return instance;
    }

    private GyroTurn(StateMachineOpMode opMode, double targetDegree) {
        this.opMode = opMode;
        this.navx = AHRS.getInstance(opMode.hardwareMap, 20);

        driveTrain = new DriveTrain(opMode.hardwareMap);
        detail = new GyroUtils.GyroDetail(navx, targetDegree);

        driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        creationTime.reset();
    }

    @Override
    public void run() {

        detail.updateData();

        if (stage == 0) {
            if (detail.degreesOff > TOLERANCE) {
                if (detail.turnDirection.equals(detail.initialTurnDirection)) { // If we were initially going in the right direction
                    double rawPower = Range.clip(detail.percentComplete / 100 * 0.45, MinMotor, MaxMotor);
                    driveTrain.rotate(detail.turnDirection, rawPower);
                } else { // If we already passed it
                    stage++;
                    waitTime.reset();
                }
            } else {
                stage++;
                waitTime.reset();
            }

        }

        if (stage == 1) {
            if (waitTime.time() > 0.5) { // Wait a little bit to catch up gyro
                stage++;
            }
        }

        if (stage == 2) { // Turn back stage
            if (detail.degreesOff > TOLERANCE) { // We're out of tolerance
                if (!detail.turnDirection.equals(detail.initialTurnDirection)) { // If we are turning back
                    driveTrain.rotate(detail.turnDirection, 0.15); // Turn really really slow
                } else { // If we messed it up
                    stage++;
                }
            } else { // We are within tolerance
                stage++;
            }
        }
    }

    @Override
    public boolean isCompleted() {
        boolean completed = false;
        if (creationTime.time() > TIMEOUT) { // 3 second timeout
            completed = true;
        }
        if (stage == 3) { // Has completed all of its stages
            completed = true;
        }

        if (completed) {
            completed();
        } else {
            run();
        }
        return completed;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
        opMode.next();
        instance = null;
    }
}
