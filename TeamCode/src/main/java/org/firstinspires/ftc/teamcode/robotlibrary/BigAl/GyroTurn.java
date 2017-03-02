package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.navXPIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Dynamic Signals on 1/16/2017.
 */

public class GyroTurn implements Routine {

    private final double TOLERANCE_DEGREES = 5;
    private final double MIN_MOTOR_OUTPUT_VALUE = -1.0;
    private final double MAX_MOTOR_OUTPUT_VALUE = 1.0;
    public navXPIDController yawPIDController;
    AHRS navx;
    DriveTrain driveTrain;
    int targetDegree = 0;
    int completedCounter = 0;
    private navXPIDController.PIDResult yawPIDResult;

    private double MinMotor = 0.0925, MaxMotor = 0.25;

    // 3/1/17 ---
    /*
    90 - 0.25 max is the best 0.52% error
    45 - 0.2 max is adequate 2.90% error
     */

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

        driveTrain.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public GyroTurn(AHRS navx, DriveTrain driveTrain, int targetDegree) {
        this(navx, driveTrain, targetDegree, new PID());
    }

    @Override
    public void run() {

        if (yawPIDController.isNewUpdateAvailable(yawPIDResult)) {
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

    @Override
    public boolean isCompleted() {
        boolean onTarget = yawPIDResult.isOnTarget();
        if (onTarget) {
            completedCounter++;
        } else {
            completedCounter = 0;
        }
        if (completedCounter > 25) {
            completed();
        } else {
            run();
        }
        return (completedCounter > 25);
    }

    @Override
    public void completed() {
        DcMotor.ZeroPowerBehavior before = driveTrain.RightFrontMotor.getZeroPowerBehavior();
        driveTrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        driveTrain.stopRobot();
        yawPIDController.enable(false);
    }
}
