package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils.Direction.CLOCKWISE;
import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils.Direction.COUNTERCLOCKWISE;

/**
 * Created by Dynamic Signals on 1/16/2017.
 */

public class GyroTurn implements EncoderRoutine {

    GyroUtils gyroUtils;
    DriveTrain driveTrain;

    int targetDegree = 0;
    int degreesOff = 0;
    int lastDegreesOff = 0;
    int stuckCoutner = 0;
    int tolerance = 1;

    double turnPower;

    GyroSensor gyro;

    public GyroTurn(GyroUtils gyroUtils, DriveTrain driveTrain, int targetDegree) {
        this.gyroUtils = gyroUtils;
        this.driveTrain = driveTrain;
        this.gyro = gyroUtils.gyro;
        this.targetDegree = targetDegree;
    }

    @Override
    public void run() {
        int currentSpoofedDegree = gyroUtils.spoofedZero(targetDegree); //An expected 39 gyro value from fake zero
        RobotLog.d("Spoofed: " + currentSpoofedDegree);
        GyroUtils.Direction turnDirection = CLOCKWISE;
        if (currentSpoofedDegree > 0 && currentSpoofedDegree <= 90) { // We need to turn counterclockwise
            degreesOff = Math.abs(-currentSpoofedDegree);
            turnDirection = COUNTERCLOCKWISE;
        }
        if (currentSpoofedDegree >= 270 && currentSpoofedDegree < 360) { // We need to turn clockwise
            degreesOff = Math.abs(90 - (currentSpoofedDegree - 270));
            turnDirection = CLOCKWISE;
        }

        if (degreesOff == lastDegreesOff) {
            stuckCoutner++;
        } else {
            stuckCoutner = 0;
        }

        if (stuckCoutner > 1000) {
            RobotLog.d("Stuck!!" + stuckCoutner);
            turnPower = 0.25;
        } else {
            turnPower = 0.15;
        }

        switch (turnDirection) {
            case CLOCKWISE:
                driveTrain.powerLeft(turnPower);
                driveTrain.powerRight(-turnPower);
                break;
            case COUNTERCLOCKWISE:
                driveTrain.powerLeft(-turnPower);
                driveTrain.powerRight(turnPower);
                break;
        }

        lastDegreesOff = degreesOff;

    }

    @Override
    public boolean isCompleted() {
        return degreesOff < 3;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
    }
}
