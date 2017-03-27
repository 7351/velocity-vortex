package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

/**
 * Created by leo on 3/25/17.
 */

public class NewEncoderTurn implements Routine {

    private static NewEncoderTurn instance;

    private EncoderTurn turn;
    private DriveTrain driveTrain;
    private StateMachineOpMode opMode;

    /*
    This class is basically a wrapper for EncoderTurn in the new StateMachine format
     */

    public static NewEncoderTurn createTurn(StateMachineOpMode opMode, int degreesToTurn, GyroUtils.Direction direction) {
        if (instance == null) {
            instance = new NewEncoderTurn(opMode, degreesToTurn, direction);
        }
        instance.isCompleted();
        return instance;
    }

    private NewEncoderTurn(StateMachineOpMode opMode, int degreesToTurn, GyroUtils.Direction direction) {
        this.opMode = opMode;
        driveTrain = new DriveTrain(opMode.hardwareMap);
        turn = new EncoderTurn(new DriveTrain(opMode.hardwareMap), degreesToTurn, direction);
    }

    @Override
    public void run() {
        turn.run();
    }

    @Override
    public boolean isCompleted() {
        boolean completed = turn.isCompleted();
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
