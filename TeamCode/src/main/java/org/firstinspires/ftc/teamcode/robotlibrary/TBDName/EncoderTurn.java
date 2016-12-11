package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

/**
 * Created by leoforney on 12/11/16.
 */

public class EncoderTurn {

    int startingLeftPosition;
    int startingRightPosition;

    DriveTrain driveTrain;
    GyroUtils gyroUtils;

    /**
     * Constructor for the EncoderTurn object
     *
     * @param driveTrain - The drive train object that should be initialized
     */
    public EncoderTurn(DriveTrain driveTrain, GyroUtils gyroUtils) {
        this.driveTrain = driveTrain;
        this.gyroUtils = gyroUtils;
        // We use this way instead of resetting the encoders because we can relatively move the robot
        startingLeftPosition = driveTrain.LeftFrontMotor.getCurrentPosition();
        startingRightPosition = driveTrain.RightFrontMotor.getCurrentPosition();
    }

    public void run(int targetDegree) {

    }
}
