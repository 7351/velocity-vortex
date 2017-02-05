package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class EncoderDrive extends RoutineImpl {

    DriveTrain driveTrain;
    double power;
    public int targetPosition;
    private int startingPositionLeft;
    private int startingPositionRight;

    // We only want to use the FrontRightMotor encoder

    /**
     * Constructor for the EncoderDrive object
     *
     * @param driveTrain - The drive train object that should be initialized
     */
    public EncoderDrive(DriveTrain driveTrain, int targetPosition, double power) {
        this.driveTrain = driveTrain;
        this.targetPosition = targetPosition;
        this.power = power;

        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        startingPositionLeft = driveTrain.LeftFrontMotor.getCurrentPosition();
        startingPositionRight = driveTrain.RightFrontMotor.getCurrentPosition();

        driveTrain.LeftFrontMotor.setTargetPosition(targetPosition + startingPositionLeft);
        driveTrain.RightFrontMotor.setTargetPosition(targetPosition + startingPositionRight);

    }

    @Override
    public boolean isCompleted() {
        if (Math.signum(targetPosition) == 1) { // If the target is positive
            if (driveTrain.LeftFrontMotor.getCurrentPosition() > targetPosition + startingPositionLeft && driveTrain.RightFrontMotor.getCurrentPosition() > targetPosition + startingPositionRight) {
                return true;
            }
        } else { // If it is negative
            if (driveTrain.LeftFrontMotor.getCurrentPosition() < targetPosition + startingPositionLeft && driveTrain.RightFrontMotor.getCurrentPosition() < targetPosition + startingPositionRight) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
    }


    @Override
    public void run() {

        // Set the power for all 4 motors
        driveTrain.powerLeft((power > 0 && targetPosition < 0) ? -1 * power : power);
        driveTrain.powerRight((power > 0 && targetPosition < 0) ? -1 * power : power);
        // We do this funky equation to make sure that we will eventually reach the target and it won't run forever.
        // If the target is negative, and you specified positive power, it will change it to negative power
    }

    public void runWithDecrementPower(double subtractivePower) {
        power = Range.clip(power - subtractivePower, 0, 1);
        run();
    }
}