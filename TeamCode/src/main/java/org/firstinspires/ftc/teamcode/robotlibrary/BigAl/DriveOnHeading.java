package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.navXPIDController;

/**
 * Created by Dynamic Signals on 2/14/2017.
 */

public class DriveOnHeading extends RoutineImpl {

    AHRS navx;
    DriveTrain driveTrain;

    int targetDegree;
    double speed;

    private navXPIDController yawPIDController;

    private final double TOLERANCE_DEGREES = 2.0;
    private final double MIN_MOTOR_OUTPUT_VALUE = -1.0;
    private final double MAX_MOTOR_OUTPUT_VALUE = 1.0;

    //TODO: Find calibration numbers
    private final double YAW_PID_P = 0.001;
    private final double YAW_PID_I = 0.0;
    private final double YAW_PID_D = 0.0;

    int completedCounter = 0;

    private navXPIDController.PIDResult yawPIDResult;

    public DriveOnHeading(AHRS navx, DriveTrain driveTrain, int targetDegree, double speed) {
        this.navx = navx;
        this.driveTrain = driveTrain;
        this.targetDegree = targetDegree;
        this.speed = speed;

                /* Create a PID Controller which uses the Yaw Angle as input. */
        yawPIDController = new navXPIDController(navx,
                navXPIDController.navXTimestampedDataSource.YAW);

        /* Configure the PID controller */
        /* Configure the PID controller */
        yawPIDController.setSetpoint(targetDegree);
        yawPIDController.setContinuous(true);
        yawPIDController.setOutputRange(MIN_MOTOR_OUTPUT_VALUE, MAX_MOTOR_OUTPUT_VALUE);
        yawPIDController.setTolerance(navXPIDController.ToleranceType.ABSOLUTE, TOLERANCE_DEGREES);
        yawPIDController.setPID(YAW_PID_P, YAW_PID_I, YAW_PID_D);
        yawPIDController.enable(true);


        yawPIDResult = new navXPIDController.PIDResult();
    }

    @Override
    public void run() {

        if (yawPIDController.isNewUpdateAvailable(yawPIDResult)) {
            if (yawPIDResult.isOnTarget()) {
                driveTrain.powerLeft(speed);
                driveTrain.powerRight(speed);
            } else {
                double output = yawPIDResult.getOutput();
                driveTrain.powerLeft(speed + output);
                driveTrain.powerRight(speed - output);
            }
        }


    }

    /**
     * This method will never be true, you will have to have a governor for the robot. Like EncoderDrive or prox.
     */
    @Override
    @Deprecated
    public boolean isCompleted() {
        return false;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
    }
}
