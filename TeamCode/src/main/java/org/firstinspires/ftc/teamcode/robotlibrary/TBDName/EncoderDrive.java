package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.DcMotor;

public class EncoderDrive {
    int startingLeftPosition;
    int startingRightPosition;

    DriveTrain driveTrain;

    /**
     * Constructor for the EncoderDrive object
     *
     * @param driveTrain - The drive train object that should be initialized
     */
    public EncoderDrive(DriveTrain driveTrain) {
        this.driveTrain = driveTrain;
        // We use this way instead of resetting the encoders because we can relatively move the robot
        startingLeftPosition = driveTrain.LeftFrontMotor.getCurrentPosition();
        startingRightPosition = driveTrain.RightFrontMotor.getCurrentPosition();
    }

    /**
     * Run function - The moving of the whole EncoderDrive object
     *
     * @param targetPosition - The target position that you want both sides to rotate (relative to where they
     *                       are to when you initialize the object)
     * @param power          - The power that you want the drive train to drive at
     */
    public void run(int targetPosition, double power) {
        // Set the target position for front motors
        driveTrain.LeftFrontMotor.setTargetPosition(targetPosition + startingLeftPosition);
        driveTrain.RightFrontMotor.setTargetPosition(targetPosition + startingRightPosition);

        // Set the run mode for only the front motors
        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set the power for all 4 motors
        driveTrain.powerLeft((power > 0 && targetPosition < 0) ? -1 * power : power);
        driveTrain.powerRight((power > 0 && targetPosition < 0) ? -1 * power : power);
        // We do this funky equation to make sure that we will eventually reach the target and it won't run forever.
        // If the target is negative, and you specified positive power, it will change it to negative power


        /* The following should be moved to the next stage after initializing the object
        if (!driveTrain.isBusy()) {
                driveTrain.stopRobot();
                stage++;
            }
         */

    }

    /**
     * Run without a power specified
     *
     * @param targetPosition This defaults to one power and uses the other method
     */
    public void run(int targetPosition) {
        run(targetPosition, 1);
    }


}