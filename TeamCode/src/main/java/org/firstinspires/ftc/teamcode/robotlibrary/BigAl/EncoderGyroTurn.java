package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import android.util.Log;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils.Direction.CLOCKWISE;
import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils.Direction.COUNTERCLOCKWISE;

/**
 * Created by Dynamic Signals on 2/19/2017.
 */

public class EncoderGyroTurn implements Routine {

    private DriveTrain driveTrain; // The drive train object
    public GyroUtils.GyroDetail detail; // Our data from the navx

    private static EncoderGyroTurn instance;

    private StateMachine stateMachine; // Our opmode that implements StateMachine
    private final static double TIMEOUT = 2.5; // Timeout time in secs

    private EncoderTurn encoderTurn; // Our encoderturn object

    private ElapsedTime creationTime = new ElapsedTime(); // Used for timeout failsafe

    private int stage = 0;

    /**
     * This static constructor is mostly for testing, use the other one for ease.
     * @param stateMachine class implementing StateMachine
     * @param navx the AHRS/NavX instance
     * @param driveTrain drive train class instance
     * @param targetDegree Degree that you want to turn to. Range (-180 - 180)
     * @return The created instance of the class.
     */
    public static EncoderGyroTurn createTurn(StateMachine stateMachine, AHRS navx, DriveTrain driveTrain, double targetDegree) {
        if (instance == null) {
            instance = new EncoderGyroTurn(stateMachine, navx, driveTrain, targetDegree);
        }
        instance.isCompleted();
        return instance;
    }

    /**
     * Standard EncoderGyroTurn. This is a one line, two argument solution for fast, accurate turns to
     * a degree using encoders (gyro assisted). THis is the recommended constructor for a turn. It is a
     * modified singleton to fit with an FTC style OpMode. It will automatically go to the next stage, so
     * there is no need to woryy about boolean statements and other lines. Just wrap this function in a
     * stage bracket and you're good!
     * @param opMode if you just type "this" it will work, its just the connection to hardwaremap, and statemachine
     * @param targetDegree Degree that you want to turn to. Range (-180 - 180)
     * @return The created instance of the class.
     */
    public static EncoderGyroTurn createTurn(StateMachineOpMode opMode, double targetDegree) {
        return createTurn(opMode, AHRS.getInstance(opMode.hardwareMap), new DriveTrain(opMode.hardwareMap), targetDegree);
    }

    private EncoderGyroTurn(StateMachine stateMachine, AHRS navx, DriveTrain driveTrain, double targetDegree) {
        this.driveTrain = driveTrain;
        this.stateMachine = stateMachine;

        detail = new GyroUtils.GyroDetail(navx);

        detail.setData(targetDegree);

        Log.d("7351", "Initial degrees off " + detail.degreesOff);

        creationTime.reset();
    }

    @Override
    public void run() {

        detail.updateData(); // Grab the latest data from our gyro detail

        if (stage == 0) { // Calculate and create EncoderTurn class
            if (encoderTurn == null) {
                double counts = Range.clip((10.76609 * (detail.degreesOff)) - 37.8814, 0, 1000000); // We never want negative counts
                Log.d("7351", "Calculated counts " + counts + " direction " +  detail.turnDirection);
                encoderTurn = new EncoderTurn(driveTrain, counts, detail.turnDirection, true); // Entering the calculated counts based on data
                encoderTurn.setPower(0.65); // We want to go fast!
                encoderTurn.run();
            }
            if (encoderTurn.isCompleted()) {
                driveTrain.stopRobot();
                stage++;
                encoderTurn = null;
            }

        }


    }

    @Override
    public boolean isCompleted() {
        boolean done = false;

        if (stage == 1) {
            done = true;
        }

        if (creationTime.time() > TIMEOUT) { // We have a timeout for a fail safe
            done = true;
        }

        if (done) {
            completed(); // Stop robot when it's done
        } else {
            run(); // Keep running otherwise
        }
        return done;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot(); // Stop robot from moving
        stateMachine.next(); // Go to the next stage
        instance = null; // Delete the instance so we can start over
    }
}