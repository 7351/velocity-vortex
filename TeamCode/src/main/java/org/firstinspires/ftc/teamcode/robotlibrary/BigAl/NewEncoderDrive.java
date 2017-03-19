package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by leo on 3/18/17.
 */

public class NewEncoderDrive implements Routine {

    private static NewEncoderDrive instance;

    private StateMachineOpMode opmode;
    private DriveTrain driveTrain;
    private DriveMode mode;

    int targetPosition;
    int[] startingPositions; // FL, FR, BL, BR
    int[] lastPositions = {0, 0, 0, 0}; // Same layout as above
    double power;
    int stuckCounter;


    public static NewEncoderDrive createDrive(StateMachineOpMode opmode, DriveMode mode, int targetPosition, double power) {
        if (instance == null) {
            instance = new NewEncoderDrive(opmode, mode, targetPosition, power);
        }
        instance.isCompleted();
        return instance;
    }

    /**

     */
    private NewEncoderDrive(StateMachineOpMode opmode, DriveMode mode, int targetPosition, double power) {
        driveTrain = new DriveTrain(opmode.hardwareMap);
        this.opmode = opmode;
        this.mode = mode;
        this.targetPosition = targetPosition;
        this.power = power;

        driveTrain.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        driveTrain.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        startingPositions = new int[]{
                driveTrain.LeftFrontMotor.getCurrentPosition(), // 0
                driveTrain.RightFrontMotor.getCurrentPosition(), // 1
                driveTrain.LeftBackMotor.getCurrentPosition(), // 2
                driveTrain.RightBackMotor.getCurrentPosition() // 3
        };

        this.targetPosition = targetPosition;

        switch (mode) {
            case HOLONOMIC_X:
                // TODO write holonomic left/right code
                break;
            case HOLONOMIC_Z:
                driveTrain.LeftFrontMotor.setTargetPosition(targetPosition + startingPositions[0]);
                driveTrain.RightFrontMotor.setTargetPosition(targetPosition + startingPositions[1]);
                driveTrain.LeftBackMotor.setTargetPosition(targetPosition + startingPositions[2]);
                driveTrain.RightBackMotor.setTargetPosition(targetPosition + startingPositions[3]);
                break;
        }


    }

    public enum DriveMode {
        HOLONOMIC_X, // This driving left and right with mecanum wheels positive = right, negative = left
        HOLONOMIC_Z // This is a standard driving for drivetrain forward and backwards
    }

    @Override
    public void run() {

        switch (mode) {
            case HOLONOMIC_X:
                // TODO write holonomic left/right power statements
                break;
            case HOLONOMIC_Z:
                // Set the power for all 4 motors
                driveTrain.powerLeft((power > 0 && targetPosition < 0) ? -1 * power : power);
                driveTrain.powerRight((power > 0 && targetPosition < 0) ? -1 * power : power);
                // We do this funky equation to make sure that we will eventually reach the target and it won't run forever.
                // If the target is negative, and you specified positive power, it will change it to negative power
                break;
        }

    }

    @Override
    public boolean isCompleted() {
        boolean completed = false;

        if (driveTrain.LeftFrontMotor.getCurrentPosition() == lastPositions[0] ||
                driveTrain.RightFrontMotor.getCurrentPosition() == lastPositions[1] ||
                driveTrain.LeftBackMotor.getCurrentPosition() == lastPositions[2] ||
                driveTrain.RightBackMotor.getCurrentPosition() == lastPositions[3]) {
            stuckCounter++;
        } else {
            stuckCounter = 0;
        }

        lastPositions[0] = driveTrain.LeftFrontMotor.getCurrentPosition();
        lastPositions[1] = driveTrain.RightFrontMotor.getCurrentPosition();
        lastPositions[2] = driveTrain.LeftBackMotor.getCurrentPosition();
        lastPositions[3] = driveTrain.RightBackMotor.getCurrentPosition();

        switch (mode) {
            case HOLONOMIC_X:
                break;
            case HOLONOMIC_Z:
                if (Math.signum(targetPosition) == 1) { // If the target is positive
                    if (driveTrain.LeftFrontMotor.getCurrentPosition() >= targetPosition + startingPositions[0] ||
                            driveTrain.RightFrontMotor.getCurrentPosition() >= targetPosition + startingPositions[1] ||
                            driveTrain.LeftBackMotor.getCurrentPosition() >= targetPosition + startingPositions[2] ||
                            driveTrain.RightBackMotor.getCurrentPosition() >= targetPosition + startingPositions[3]
                            ) {
                        completed = true;
                    }
                } else { // If it is negative
                    if (driveTrain.LeftFrontMotor.getCurrentPosition() <= targetPosition + startingPositions[0] ||
                            driveTrain.RightFrontMotor.getCurrentPosition() <= targetPosition + startingPositions[1] ||
                            driveTrain.LeftBackMotor.getCurrentPosition() <= targetPosition + startingPositions[2] ||
                            driveTrain.RightBackMotor.getCurrentPosition() <= targetPosition + startingPositions[3]) {
                        completed = true;
                    }
                }
                break;
        }
        // TODO write completed statements

        if (stuckCounter > 50) {
            completed = true;
        }

        if (completed) {
            completed();
        } else {
            run();
        }
        return completed;
    }

    @Override
    public void completed() {
        opmode.next();
        instance = null;
        driveTrain.stopRobot();
    }
}
