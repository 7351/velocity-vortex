package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.navXPIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Dynamic Signals on 1/16/2017.
 */

public class GyroTurn extends RoutineImpl {

    private final double TOLERANCE_DEGREES = 5;
    private final double MIN_MOTOR_OUTPUT_VALUE = -1.0;
    private final double MAX_MOTOR_OUTPUT_VALUE = 1.0;
    AHRS navx;
    DriveTrain driveTrain;
    int targetDegree = 0;
    int completedCounter = 0;
    public navXPIDController yawPIDController;
    private navXPIDController.PIDResult yawPIDResult;

    private double MinMotor = 0.2, MaxMotor = 0.4;

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
                double power = 0; // Temp value
                if (Math.signum(output) == 1) {
                    power = Range.clip(output, MinMotor, MaxMotor);
                }
                if (Math.signum(output) == -1) {
                    power = Range.clip(output, -MaxMotor, -MinMotor);
                }
                driveTrain.powerLeft(power);
                driveTrain.powerRight(-power);
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
