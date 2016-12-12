package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by leoforney on 12/11/16.
 */

public class EncoderTurn {

    int startingLeftPosition;
    int startingRightPosition;
    int oneDegreeTurn = 40;

    DriveTrain driveTrain;

    /**
     * Constructor for the EncoderTurn object
     *
     * @param driveTrain - The drive train object that should be initialized
     */
    public EncoderTurn(DriveTrain driveTrain) {
        this.driveTrain = driveTrain;
        // We use this way instead of resetting the encoders because we can relatively move the robot
        startingLeftPosition = driveTrain.LeftFrontMotor.getCurrentPosition();
        startingRightPosition = driveTrain.RightFrontMotor.getCurrentPosition();

    }

    public void run(int targetAngle, double power) {
        int turnThisMuch = targetAngle * oneDegreeTurn;

        // Switch for the direction based on target angle and power
        Direction direction = (Math.signum((float) targetAngle) == 1) ? Direction.CLOCKWISE : Direction.COUNTERCLOCKWISE;

        power = Math.abs(power);

        switch (direction) {
            case CLOCKWISE:
                driveTrain.LeftFrontMotor.setTargetPosition(startingLeftPosition + turnThisMuch);
                driveTrain.RightFrontMotor.setTargetPosition(startingRightPosition - turnThisMuch);

                driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                driveTrain.powerLeft(power);
                driveTrain.powerRight(-power);
                break;
            case COUNTERCLOCKWISE:
                driveTrain.LeftFrontMotor.setTargetPosition(startingLeftPosition - turnThisMuch);
                driveTrain.RightFrontMotor.setTargetPosition(startingRightPosition + turnThisMuch);

                driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                driveTrain.powerLeft(-power);
                driveTrain.powerRight(power);
                break;
        }

    }

    enum Direction {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }
}
