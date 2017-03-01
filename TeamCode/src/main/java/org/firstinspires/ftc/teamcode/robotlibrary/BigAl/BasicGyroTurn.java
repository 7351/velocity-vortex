package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Dynamic Signals on 2/26/2017.
 */

public class BasicGyroTurn implements Routine {

    private static double startingDrivePower = 0.095;
    public double percentComplete;
    public GyroUtils.GyroDetail detail;
    private int stuckCounter = 0;
    private double initalDegreesOff;
    private double drivePower;
    private double lastDegreesOffRounded = 0;

    private double tolerance = 3;
    private DriveTrain driveTrain;

    public BasicGyroTurn(AHRS navx, DriveTrain driveTrain, double targetDegree, double power) {
        this.driveTrain = driveTrain;
        startingDrivePower = power;
        detail = new GyroUtils.GyroDetail(navx, targetDegree);

        initalDegreesOff = detail.degreesOff;

        driveTrain.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public BasicGyroTurn(AHRS navx, DriveTrain driveTrain, double targetDegree) {
        this(navx, driveTrain, targetDegree, BasicGyroTurn.startingDrivePower);
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public void setPower(double power) {
        startingDrivePower = power;
    }

    @Override
    public void run() {

        detail.updateData();

        percentComplete = Range.clip(100 - ((detail.degreesOff / initalDegreesOff) * 100), 0, 100);

        if (percentComplete < 75) {
            percentComplete *= 0.8;
        }

        if (Math.round(detail.degreesOff) == lastDegreesOffRounded) { // We think we are stuck
            stuckCounter++; // Lets increment it
        } else {
            stuckCounter = 0; // We are nowhere close to being stuck
            drivePower = startingDrivePower; // Default power
        }

        if (stuckCounter > 750) { // We are definitely stuck (for about 5 seconds)
            drivePower = Range.clip(drivePower + 0.000075, 0, 1); // Slowly speed up the turn
        }

        lastDegreesOffRounded = Math.round(detail.degreesOff); // Set the last degree
        double leftPower = (detail.turnDirection.equals(GyroUtils.Direction.COUNTERCLOCKWISE)) ? -drivePower : drivePower;


        driveTrain.powerLeft(leftPower);
        driveTrain.powerRight(-leftPower);

    }

    /**
     * Is the turn completed? If it isn't it will keep running, if it is, it will stop the robot
     *
     * @return if the turn is completed or not
     */
    @Override
    public boolean isCompleted() {
        boolean completed = (percentComplete > 95);
        if (completed) {
            completed(); // We automatically stop the robot
        } else {
            run();
        }
        return completed;
    }

    @Override
    public void completed() {

        driveTrain.stopRobot();
        driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

}
