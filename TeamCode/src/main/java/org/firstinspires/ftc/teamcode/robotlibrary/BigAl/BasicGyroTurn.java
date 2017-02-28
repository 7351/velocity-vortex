package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Dynamic Signals on 2/26/2017.
 */

public class BasicGyroTurn implements Routine {


    private static final double tolerancePercent = 3;
    private static double startingDrivePower = 0.25;
    public GyroUtils.GyroDetail detail;
    private int stuckCounter = 0;
    private double drivePower;
    private double lastDegreesOffRounded = 0;

    private double tolerance;
    private DriveTrain driveTrain;

    public BasicGyroTurn(AHRS navx, DriveTrain driveTrain, double targetDegree, double power) {
        this.driveTrain = driveTrain;
        this.startingDrivePower = power;
        detail = new GyroUtils.GyroDetail(navx, targetDegree);

        tolerance = Range.clip(detail.degreesOff * tolerancePercent / 100, 1, 5);

    }

    public BasicGyroTurn(AHRS navx, DriveTrain driveTrain, double targetDegree) {
        this(navx, driveTrain, targetDegree, BasicGyroTurn.startingDrivePower);
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setPower(double power) {
        this.startingDrivePower = power;
    }

    @Override
    public void run() {

        detail.updateData();

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
        boolean completed = (detail.degreesOff < tolerance);
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

    }

    public void cleanup() {

    }
}
