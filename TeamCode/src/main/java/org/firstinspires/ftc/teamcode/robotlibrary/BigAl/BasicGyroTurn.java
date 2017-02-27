package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.kauailabs.navx.ftc.AHRS;
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

    public BasicGyroTurn(DriveTrain driveTrain, AHRS navx, double targetDegree) {
        this.driveTrain = driveTrain;
        detail = new GyroUtils.GyroDetail(navx, targetDegree);

    }

    @Override
    public void run() {

        detail.updateData();

        if (Math.round(detail.degreesOff) == lastDegreesOffRounded) { // We think we are stuck
            stuckCounter++;
        } else {
            stuckCounter = 0;
            drivePower = startingDrivePower;
        }

        if (stuckCounter > 500) {
            drivePower = Range.clip(drivePower + 0.0005, 0, 1);
        }

        lastDegreesOffRounded = Math.round(detail.degreesOff);

        double leftPower = (detail.turnDirection.equals(GyroUtils.Direction.COUNTERCLOCKWISE)) ? -drivePower : drivePower;
        driveTrain.powerLeft(leftPower);
        driveTrain.powerRight(-leftPower);

    }

    @Override
    public boolean isCompleted() {
        boolean completed = (detail.degreesOff < 2);
        if (completed) completed();
        return completed;
    }

    @Override
    public void completed() {

        driveTrain.stopRobot();

    }
}
