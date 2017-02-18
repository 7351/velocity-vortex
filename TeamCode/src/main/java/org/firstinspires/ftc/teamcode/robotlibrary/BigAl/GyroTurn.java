package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.navXPIDController;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Dynamic Signals on 1/16/2017.
 */

public class GyroTurn extends RoutineImpl {

    private final double TOLERANCE_DEGREES = 1.25;
    private final double MIN_MOTOR_OUTPUT_VALUE = -1.0;
    private final double MAX_MOTOR_OUTPUT_VALUE = 1.0;
    AHRS navx;
    DriveTrain driveTrain;
    int targetDegree = 0;
    int completedCounter = 0;
    private navXPIDController yawPIDController;
    private navXPIDController.PIDResult yawPIDResult;

    public GyroTurn(AHRS navx, DriveTrain driveTrain, int targetDegree, PID pid) {
        this.navx = navx;
        this.driveTrain = driveTrain;
        this.targetDegree = targetDegree;

                /* Create a PID Controller which uses the Yaw Angle as input. */
        yawPIDController = new navXPIDController(navx,
                navXPIDController.navXTimestampedDataSource.YAW);

        /* Configure the PID controller */
        yawPIDController.setSetpoint(targetDegree);
        yawPIDController.setContinuous(true);
        yawPIDController.setOutputRange(MIN_MOTOR_OUTPUT_VALUE, MAX_MOTOR_OUTPUT_VALUE);
        yawPIDController.setTolerance(navXPIDController.ToleranceType.ABSOLUTE, TOLERANCE_DEGREES);
        yawPIDController.setPID(pid.p, pid.i, pid.d);
        yawPIDController.enable(true);

        yawPIDResult = new navXPIDController.PIDResult();
    }

    public GyroTurn(AHRS navx, DriveTrain driveTrain, int targetDegree) {
        this(navx, driveTrain, targetDegree, new PID());
    }

    @Override
    public void run() {

        if (yawPIDController.isNewUpdateAvailable(yawPIDResult)) {
            if (yawPIDResult.isOnTarget()) {
                driveTrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                driveTrain.stopRobot();
            } else {
                double output = yawPIDResult.getOutput();
                //driveTrain.powerLeft(output);
                //driveTrain.powerRight(-output);
            }
        }


    }

    @Override
    public boolean isCompleted() {
        boolean onTarget = yawPIDResult.isOnTarget();
        if (onTarget) {
            completedCounter++;
        } else {
            completedCounter = 0;
        }
        if (completedCounter > 25) completed();
        return (completedCounter > 25);
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
        yawPIDController.enable(false);
    }
}
