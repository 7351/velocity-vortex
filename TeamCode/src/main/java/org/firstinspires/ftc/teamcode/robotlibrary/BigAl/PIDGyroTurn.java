package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import android.support.annotation.Nullable;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.navXPIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Dynamic Signals on 1/16/2017.
 */

public class PIDGyroTurn implements Routine {

    private static PIDGyroTurn instance;
    private final double TOLERANCE_DEGREES = 5;
    private final double TIMEOUT = 2;

    public navXPIDController yawPIDController;
    public GyroUtils.GyroDetail detail;

    double targetDegree = 0;
    int completedCounter = 0;

    private ElapsedTime creationTime = new ElapsedTime(); // Used for timeout failsafe
    private DriveTrain driveTrain;
    private AHRS navx;
    private StateMachineOpMode opMode;
    private navXPIDController.PIDResult yawPIDResult;
    private double MinMotor = 0.0925, MaxMotor = 0.25;

    /**
     * Static constructor for a GyroTurn if you want to specify the PID for
     * @param pid The PID instance of the class that has the P, I, and D coefficients.
     * @return the GyroTurn instance
     */
    public static PIDGyroTurn createTurn(StateMachineOpMode opMode, double targetDegree, PID pid) {
        if (instance == null) {
            instance = new PIDGyroTurn(opMode, targetDegree, pid);
        }
        instance.isCompleted();
        return instance;
    }

    /**
     * Static constructor for an OpMode style class with just the degree and opmode
     * @param opMode the OpMode that implements StateMachineOpMode, usually just type "this"
     * @param targetDegree the degree that you want to turn to (0-360)
     * @return the instance of the GyroTurn class. You can check the progress using the GyroDetail detail for percentage complete.
     */
    public static PIDGyroTurn createTurn(StateMachineOpMode opMode, double targetDegree) {
        return createTurn(opMode, targetDegree);
    }

    private PIDGyroTurn(StateMachineOpMode opMode, double targetDegree, @Nullable PID pid) {
        this.opMode = opMode;
        this.navx = AHRS.getInstance(opMode.hardwareMap, 20);
        this.targetDegree = targetDegree;

        driveTrain = new DriveTrain(opMode.hardwareMap);

        creationTime.reset();

        /* Create a PID Controller which uses the Yaw Angle as input. */
        yawPIDController = new navXPIDController(navx,
                navXPIDController.navXTimestampedDataSource.FUSED_HEADING);

        /* Configure the PID controller */
        yawPIDController.setSetpoint(targetDegree);
        yawPIDController.setContinuous(true);
        yawPIDController.setOutputRange(-1, 1);
        yawPIDController.setTolerance(navXPIDController.ToleranceType.ABSOLUTE, TOLERANCE_DEGREES);

        if (pid == null) {
            yawPIDController.setPID(0.005, 0, 0);
        } else {
            yawPIDController.setPID(pid.p, pid.i, pid.d);
        }

        yawPIDController.enable(true);

        /* We want to use the RUN_USING_ENCODER run mode to get the most accurate turning power */
        driveTrain.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        /* Start tracking data */
        yawPIDResult = new navXPIDController.PIDResult();
        detail = new GyroUtils.GyroDetail(navx, targetDegree);

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
        if (completedCounter > 25 || creationTime.time() > TIMEOUT) {
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
        driveTrain.stopRobot();
        opMode.next();
        instance = null;
    }

    public static void teardown() {
        instance = null;
    }
}