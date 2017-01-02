package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.DcMotor;

public class EncoderDrive implements EncoderRoutine {

    DriveTrain driveTrain;
    double power;
    public int targetPosition;

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

        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set the target position for front motors (Right only)
        driveTrain.LeftFrontMotor.setTargetPosition(targetPosition);
        driveTrain.RightFrontMotor.setTargetPosition(targetPosition);

        // Set the run mode for only the front motors (Right only)
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public EncoderDrive(DriveTrain driveTrain, int targetPosition) {
        this.driveTrain = driveTrain;
        this.targetPosition = targetPosition;
        this.power = 0.75;

        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set the target position for front motors (Right only)
        driveTrain.RightFrontMotor.setTargetPosition(targetPosition);
        driveTrain.LeftFrontMotor.setTargetPosition(targetPosition);

        // Set the run mode for only the front motors (Right only)
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }


    @Override
    public boolean isCompleted() {
        if (Math.signum(targetPosition) == 1) { // If the target is positive
            if (driveTrain.RightFrontMotor.getCurrentPosition() > targetPosition) {
                return true;
            }
        } else { // If it is negative
            if (driveTrain.RightFrontMotor.getCurrentPosition() < targetPosition) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    @Override
    public void run() {


        // Set the power for all 4 motors
        driveTrain.powerLeft((power > 0 && targetPosition < 0) ? -1 * power : power);
        driveTrain.powerRight((power > 0 && targetPosition < 0) ? -1 * power : power);
        // We do this funky equation to make sure that we will eventually reach the target and it won't run forever.
        // If the target is negative, and you specified positive power, it will change it to negative power
    }
}