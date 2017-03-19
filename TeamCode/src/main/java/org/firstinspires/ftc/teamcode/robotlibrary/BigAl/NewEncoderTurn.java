package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.DcMotor;

import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils.Direction.CLOCKWISE;
import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils.Direction.COUNTERCLOCKWISE;

/**
 * Created by leo on 3/19/17.
 */

public class NewEncoderTurn implements Routine {

    private static NewEncoderTurn instance;
    private DriveTrain driveTrain;

    private StateMachineOpMode opMode;
    private int stuckCounter;
    private GyroUtils.Direction turnDirection;
    private final double power = 0.45;
    int rawTicks;
    int[] targetPositions; // FL, FR, BL, BR
    int[] lastPositions = {0, 0, 0, 0}; // Same layout as above


    public static NewEncoderTurn createTurn(StateMachineOpMode opMode, int ticks, GyroUtils.Direction turnDirection) {
        if (instance == null) {
            instance = new NewEncoderTurn(opMode, ticks, turnDirection);
        }
        instance.isCompleted();
        return instance;
    }

    private NewEncoderTurn(StateMachineOpMode opMode, int ticks, GyroUtils.Direction turnDirection) {
        driveTrain = new DriveTrain(opMode.hardwareMap);
        this.opMode = opMode;
        this.rawTicks = ticks;
        this.turnDirection = turnDirection;

        driveTrain.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        driveTrain.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int directionSwitcher = (turnDirection.equals(CLOCKWISE)) ? 1 : -1;

        targetPositions = new int[]{
                directionSwitcher * (driveTrain.LeftFrontMotor.getCurrentPosition() + ticks), // 0
                -directionSwitcher * (driveTrain.RightFrontMotor.getCurrentPosition() + ticks), // 1
                directionSwitcher * (driveTrain.LeftBackMotor.getCurrentPosition() + ticks), // 2
                -directionSwitcher * (driveTrain.RightBackMotor.getCurrentPosition() + ticks) // 3
        };

        driveTrain.LeftFrontMotor.setTargetPosition(targetPositions[0]);
        driveTrain.RightFrontMotor.setTargetPosition(targetPositions[1]);
        driveTrain.LeftBackMotor.setTargetPosition(targetPositions[2]);
        driveTrain.RightBackMotor.setTargetPosition(targetPositions[3]);

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

        if (stuckCounter > 50) {
            completed = true;
        }

        switch (turnDirection) {
            case CLOCKWISE:
                if (driveTrain.RightFrontMotor.getCurrentPosition() < -targetPositions[1]) {
                    completed = true;
                }
                break;
            case COUNTERCLOCKWISE:
                if (driveTrain.RightFrontMotor.getCurrentPosition() > targetPositions[1]) {
                    completed = true;
                }
                break;
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
        driveTrain.stopRobot();
        opMode.next();
        instance = null;
    }
}
