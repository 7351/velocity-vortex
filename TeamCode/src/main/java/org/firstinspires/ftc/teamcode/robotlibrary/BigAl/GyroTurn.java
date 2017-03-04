package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import android.support.annotation.Nullable;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.navXPIDController;
import com.qualcomm.ftccommon.DbgLog;
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

    int timesRan = 0;

    PID pid;

    boolean PCalculated = false;

    private double MinMotor = 0.0925, MaxMotor = 0.25;

    // 3/1/17 ---
    /*
    90 - 0.25 max is the best 0.52% error
    45 - 0.2 max is adequate 2.90% error
     */

    public GyroTurn(AHRS navx, DriveTrain driveTrain, int targetDegree, @Nullable PID pid) {
        this.navx = navx;
        this.driveTrain = driveTrain;
        this.targetDegree = targetDegree;
        this.pid = pid;

        /* Create a PID Controller which uses the Yaw Angle as input. */
        yawPIDController = new navXPIDController(navx,
                navXPIDController.navXTimestampedDataSource.YAW);

        /* Configure the PID controller */
        yawPIDController.setSetpoint(targetDegree);
        yawPIDController.setContinuous(true);
        yawPIDController.setOutputRange(MIN_MOTOR_OUTPUT_VALUE, MAX_MOTOR_OUTPUT_VALUE);
        yawPIDController.setTolerance(navXPIDController.ToleranceType.ABSOLUTE, TOLERANCE_DEGREES);
        if (pid == null) {
            yawPIDController.setPID(0.005, 0, 0);
        } else {
            yawPIDController.setPID(pid.p, pid.i, pid.d);
        }

        yawPIDController.enable(true);

        driveTrain.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        yawPIDResult = new navXPIDController.PIDResult();

    }

    public GyroTurn(AHRS navx, DriveTrain driveTrain, int targetDegree) {
        this(navx, driveTrain, targetDegree, null);
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

        timesRan++;

        if (timesRan > 25 && !PCalculated && pid != null) {
            double degreesOff = Math.round(yawPIDController.getError());

            pid.p = (degreesOff * 0.0000355) + 0.0030521;

            yawPIDController.setPID(pid.p, pid.i, pid.d);

            DbgLog.msg("Degrees off! : " + String.valueOf(degreesOff));

            PCalculated = true;
        }


    }

    public void setPBasedOnDegreesLeft() {
        double degreesOff = Math.round(yawPIDController.getError());
        pid.p = (degreesOff * 0.0000355) + 0.0030521;

        yawPIDController.setPID(pid.p, pid.i, pid.d);
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
        driveTrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        driveTrain.stopRobot();
        yawPIDController.enable(false);
    }
}
