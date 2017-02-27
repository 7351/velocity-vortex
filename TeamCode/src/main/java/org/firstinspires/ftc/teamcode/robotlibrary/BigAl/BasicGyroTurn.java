package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Dynamic Signals on 2/26/2017.
 */

public class BasicGyroTurn implements Routine {

    public GyroUtils.GyroDetail detail;
    private DriveTrain driveTrain;

    int stuckCounter = 0;
    double drivePower;
    double startingDrivePower = 0.25;
    double lastDegreesOffRounded = 0;

    public BasicGyroTurn(AHRS navx, DriveTrain driveTrain, double targetDegree) {
        this.driveTrain = driveTrain;
        detail = new GyroUtils.GyroDetail(navx, targetDegree);

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

        if (stuckCounter > 750) { // We are definitely stuck
            drivePower = Range.clip(drivePower + 0.0001, 0, 1); // Slowly speed up the turn
        }

        lastDegreesOffRounded = Math.round(detail.degreesOff); // Set the last degree

        double leftPower = (detail.turnDirection.equals(GyroUtils.Direction.COUNTERCLOCKWISE)) ? -drivePower : drivePower;
        driveTrain.powerLeft(leftPower);
        driveTrain.powerRight(-leftPower);

    }

    @Override
    public boolean isCompleted() {
        boolean completed = (detail.degreesOff < 2);
        if (completed) completed(); // We automatically stop the robot
        return completed;
    }

    @Override
    public void completed() {

        driveTrain.stopRobot();

    }
}
