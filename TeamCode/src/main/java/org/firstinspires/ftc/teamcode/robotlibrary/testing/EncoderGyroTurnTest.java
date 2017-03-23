package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderGyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.StateMachine;

/**
 * Created by Dynamic Signals on 2/19/2017.
 */

@Autonomous(name = "EncoderGyroTest", group = "Testing")
public class EncoderGyroTurnTest extends OpMode implements StateMachine {

    int stage = 0;
    AHRS navx;
    DriveTrain driveTrain;

    EncoderGyroTurn encoderGyroTurn;
    ElapsedTime time = new ElapsedTime();
    double completedTime = 0;
    double completedYaw = 0;

    boolean calibrated = false;

    @Override
    public void init() {

        navx = AHRS.getInstance(hardwareMap);
        driveTrain = new DriveTrain(hardwareMap);

    }

    @Override
    public void start() {
        time.reset();
    }

    @Override
    public void loop() {

        if (stage == 0) {
            if (!calibrated) {
                if (!navx.isCalibrating()) {
                    navx.zeroYaw();
                    calibrated = true;
                }
            }
            if (time.time() > 1) { // This won't matter in a real autonomous because we always drive out
                time.reset();
                stage++;
            }
        }

        if (stage == 1) {
            EncoderGyroTurn.createTurn(this, navx, driveTrain, 90); // That's it!
        }

        if (stage == 2) {
            if (completedTime == 0 || completedYaw == 0) {
                completedTime = time.time();
                completedYaw = navx.getYaw();
            }
            telemetry.addData("Yaw", completedYaw);
            telemetry.addData("Time", completedTime);
        }

        if (stage != 2) {
            // Just some debug stuff
            telemetry.addData("Stage", stage);
            telemetry.addData("Yaw", AutonomousUtils.df.format(navx.getYaw()));
            if (encoderGyroTurn != null && encoderGyroTurn.detail != null) {
                telemetry.addData("Degrees left", encoderGyroTurn.detail.degreesOff);
                telemetry.addData("Direction", encoderGyroTurn.detail.turnDirection.toString().toLowerCase());
            }
        }

    }

    @Override
    public void next() {
        stage++;
    }
}
