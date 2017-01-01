package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by leoforney on 12/11/16.
 */

public class EncoderTurn implements EncoderRoutine {

    private int turnPerDegree = (int) Math.round(((GearRatio * SprocketRatio) * 28)/360);
    private DriveTrain driveTrain;
    private GyroUtils gyroUtils;
    private final double power = 0.55;
    int turnDegree;
    private int encoderCounts;
    public GyroUtils.Direction turnDirection;

    /**
     * Constructor for the EncoderTurn object
     *
     * @param driveTrain - The drive train object that should be initialized
     */
    public EncoderTurn(DriveTrain driveTrain, GyroUtils gyroUtils, int degree) {
        this.driveTrain = driveTrain;
        this.gyroUtils = gyroUtils;

        turnDegree = degree;

        int currentDegree = gyroUtils.spoofedZero(degree);

        if (currentDegree > 0 && currentDegree <= 90) { // We need to turn counterclockwise
            int error_degrees = Math.abs(0 - currentDegree);
            turnDirection = GyroUtils.Direction.COUNTERCLOCKWISE;
            encoderCounts = error_degrees * turnPerDegree;
        }

        if (currentDegree >= 270 && currentDegree < 360) { // We need to turn clockwise
            int error_degrees = Math.abs(90 - (currentDegree - 270));
            turnDirection = GyroUtils.Direction.CLOCKWISE;
            encoderCounts = error_degrees * turnPerDegree;
        }

        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        switch (turnDirection) {
            case CLOCKWISE:
                driveTrain.RightFrontMotor.setTargetPosition(-encoderCounts);

                driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                break;
            case COUNTERCLOCKWISE:
                driveTrain.RightFrontMotor.setTargetPosition(encoderCounts);

                driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                break;
        }

        // Set the run mode for only the front motors (Right only)
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    @Override
    public void run() {
        switch (turnDirection) {
            case CLOCKWISE:
                driveTrain.powerLeft(power);
                driveTrain.powerRight(-power);
                break;
            case COUNTERCLOCKWISE:
                driveTrain.powerLeft(-power);
                driveTrain.powerRight(power);
                break;
        }
    }

    @Override
    public boolean isCompleted() {
        switch (turnDirection) {
            case CLOCKWISE:
                if (driveTrain.RightFrontMotor.getCurrentPosition() < -encoderCounts) {
                    if (gyroUtils.isGyroInTolerance(turnDegree, 3)) {
                        return true;
                    } else {
                        return false;
                    }
                }
                break;
            case COUNTERCLOCKWISE:
                if (driveTrain.RightFrontMotor.getCurrentPosition() > encoderCounts) {
                    if (gyroUtils.isGyroInTolerance(turnDegree, 3)) {
                        return true;
                    } else {
                        return false;
                    }
                }
                break;
        }

        return false;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
