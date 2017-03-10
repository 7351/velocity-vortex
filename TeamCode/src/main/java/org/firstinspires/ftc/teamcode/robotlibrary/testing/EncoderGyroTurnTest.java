package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderGyroTurn;

/**
 * Created by Dynamic Signals on 2/19/2017.
 */

@Autonomous(name = "EncoderGyroTest", group = "Testing")
public class EncoderGyroTurnTest extends OpMode {

    int stage = 0;
    AHRS navx;
    DriveTrain driveTrain;

    EncoderGyroTurn encoderGyroTurn;

    @Override
    public void init() {

        navx = AHRS.getInstance(hardwareMap);
        driveTrain = new DriveTrain(hardwareMap);

    }

    @Override
    public void loop() {

        if (stage == 0) {
            if (!navx.isCalibrating()) {
                stage++;
                navx.zeroYaw();
            }
        }

        if (stage == 1) {
            if (encoderGyroTurn == null)
                encoderGyroTurn = new EncoderGyroTurn(navx, driveTrain, 90); // We give it what we want here
            if (encoderGyroTurn.isCompleted()) stage++; // It will run the code in this statement
        }

        // Just some debug stuff
        telemetry.addData("Stage", stage);
        telemetry.addData("Yaw", AutonomousUtils.df.format(navx.getYaw()));
        telemetry.addData("Degrees left", encoderGyroTurn.detail.degreesOff);
        telemetry.addData("Direction", encoderGyroTurn.detail.turnDirection.toString().toLowerCase());

    }
}
