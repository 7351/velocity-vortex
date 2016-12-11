package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by leoforney on 12/11/16.
 */

public class EncoderTurn {

    int startingLeftPosition;
    int startingRightPosition;
    int oneDegreeTurn = 40;

    DriveTrain driveTrain;
    GyroUtils gyroUtils;
    GyroSensor gyroSensor;

    /**
     * Constructor for the EncoderTurn object
     *
     * @param driveTrain - The drive train object that should be initialized
     */
    public EncoderTurn(DriveTrain driveTrain/*, GyroUtils gyroUtils, GyroSensor initialgyro*/) {
        this.driveTrain = driveTrain;
        /*this.gyroUtils = gyroUtils;
        gyroSensor = initialgyro;*/
        // We use this way instead of resetting the encoders because we can relatively move the robot
        startingLeftPosition = driveTrain.LeftFrontMotor.getCurrentPosition();
        startingRightPosition = driveTrain.RightFrontMotor.getCurrentPosition();

    }

    public void run(int targetAngle, double power, boolean turnRight) {
        //int currentPos = gyroSensor.getHeading();
        int turnThisMuch = targetAngle * oneDegreeTurn;

        if (turnRight)
        {
            driveTrain.LeftFrontMotor.setTargetPosition(turnThisMuch + startingLeftPosition);
            driveTrain.RightFrontMotor.setTargetPosition(startingRightPosition - turnThisMuch);

            driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveTrain.LeftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveTrain.RightBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

    }
}
