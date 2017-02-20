package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.util.RobotLog;

import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils.Direction.CLOCKWISE;
import static org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils.Direction.COUNTERCLOCKWISE;

/**
 * Created by Dynamic Signals on 2/19/2017.
 */

public class EncoderGyroTurn extends RoutineImpl {

    DriveTrain driveTrain;
    AHRS navx;

    double targetDegree = 0;
    double degreesOff = 0;

    double turnPower;

    GyroUtils.Direction turnDirection;

    EncoderTurn encoderTurn;

    public EncoderGyroTurn(AHRS navx, DriveTrain driveTrain, double targetDegree) {
        this.driveTrain = driveTrain;
        this.targetDegree = targetDegree;
        this.navx = navx;

        double degreesOffFlipped = navx.getYaw() - targetDegree;
        degreesOff = Math.abs(degreesOffFlipped);

        if (Math.signum(degreesOffFlipped) == 1) turnDirection = COUNTERCLOCKWISE; // If it's positive, rotate counterclockwise
        if (Math.signum(degreesOffFlipped) == -1) turnDirection = CLOCKWISE; // If we are negative number, rotate clockwise
        if (Math.signum(degreesOffFlipped) == 0) turnDirection = CLOCKWISE; // If it's zero, and it never will be

        encoderTurn = new EncoderTurn(driveTrain, degreesOff, turnDirection, true);

    }

    @Override
    public void run() {

        encoderTurn.run();

    }

    @Override
    public boolean isCompleted() {
        boolean done = encoderTurn.isCompleted();
        if (done) completed();
        return done;
    }

    @Override
    public void completed() {
        driveTrain.stopRobot();
    }
}
