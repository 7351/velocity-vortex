package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by leoforney on 12/11/16.
 */

public class EncoderTurn implements Routine {

    private double turnPerDegree = (((GearRatio * SprocketRatio) * 28) / 360) * 180 / 121.5;
    private double turnPerDegreeFixed = (turnPerDegree * 90) / 114; // Farther - decrease, shorter - increase

    private DriveTrain driveTrain;
    private final double power = 0.45;
    public int encoderCounts;
    private int startingPositionLeft;
    private int startingPositionRight;
    public GyroUtils.Direction turnDirection;

    public EncoderTurn(DriveTrain driveTrain, double degreesToTurn, GyroUtils.Direction turnDirection) {
        this(driveTrain, degreesToTurn, turnDirection, false); // Assume old function/legacy code
    }

    /**
     * Constructor for the EncoderTurn object
     *
     * @param driveTrain    - The drive train object that should be initialized
     * @param degreesToTurn
     */
    public EncoderTurn(DriveTrain driveTrain, double degreesToTurn, GyroUtils.Direction turnDirection, boolean rawCounts) {
        this.driveTrain = driveTrain;
        this.turnDirection = turnDirection;

        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        startingPositionLeft = driveTrain.LeftFrontMotor.getCurrentPosition();
        startingPositionRight = driveTrain.RightFrontMotor.getCurrentPosition();

        if (!rawCounts) { // If we are using legacy code
            encoderCounts = (int) (degreesToTurn * turnPerDegreeFixed);
        } else { // This is a raw degrees so that we can do the math elsewhere
            encoderCounts = (int) degreesToTurn;
        }


        switch (turnDirection) {
            case CLOCKWISE:
                driveTrain.LeftFrontMotor.setTargetPosition(startingPositionLeft + encoderCounts);
                driveTrain.RightFrontMotor.setTargetPosition(startingPositionRight - encoderCounts);

                driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                break;
            case COUNTERCLOCKWISE:
                driveTrain.LeftFrontMotor.setTargetPosition(startingPositionLeft - encoderCounts);
                driveTrain.RightFrontMotor.setTargetPosition(startingPositionRight + encoderCounts);

                driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                break;
        }

        // Set the run mode for only the front motors (Right only)
        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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
                    return true;
                }
                break;
            case COUNTERCLOCKWISE:
                if (driveTrain.RightFrontMotor.getCurrentPosition() > encoderCounts) {
                    return true;
                }
                break;
        }

        return false;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}