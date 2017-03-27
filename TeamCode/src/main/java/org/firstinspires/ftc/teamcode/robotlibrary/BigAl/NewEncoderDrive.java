package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by leo on 3/25/17.
 */

public class NewEncoderDrive implements Routine {

    private static NewEncoderDrive instance;

    private EncoderDrive drive;
    private DriveTrain driveTrain;
    private StateMachineOpMode opMode;

    /*
    This class is basically a wrapper for EncoderDrive in the new StateMachine format
     */

    public static NewEncoderDrive createDrive(StateMachineOpMode opMode, int targetPosition) {
        return createDrive(opMode, targetPosition, 0.5);
    }

    public static NewEncoderDrive createDrive(StateMachineOpMode opMode, int targetPosition, double power) {
        if (instance == null) {
            instance = new NewEncoderDrive(opMode, targetPosition, power);
        }
        instance.isCompleted();
        return instance;
    }

    private NewEncoderDrive(StateMachineOpMode opMode, int targetPosition, double power) {
        this.opMode = opMode;
        driveTrain = new DriveTrain(opMode.hardwareMap);
        drive = new EncoderDrive(new DriveTrain(opMode.hardwareMap), targetPosition, power);
    }

    @Override
    public void run() {
        drive.run();
    }

    @Override
    public boolean isCompleted() {
        boolean completed = drive.isCompleted();
        if (completed) {
            completed();
        } else {
            run();
        }
        return completed;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
        opMode.next();
        instance = null;
    }

    public static void teardown() {
        instance = null;
    }
}
