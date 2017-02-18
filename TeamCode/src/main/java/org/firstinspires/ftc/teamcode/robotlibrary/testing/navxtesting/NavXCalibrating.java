package org.firstinspires.ftc.teamcode.robotlibrary.testing.navxtesting;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Dynamic Signals on 2/17/2017.
 */

@TeleOp(name = "NavXYaw")
public class NavXCalibrating extends OpMode {

    AHRS navx;

    @Override
    public void init() {

        navx = AHRS.getInstance(hardwareMap);

    }

    public void start() {
        navx.zeroYaw();
    }

    @Override
    public void loop() {

        telemetry.addData("Yaw", navx.getYaw());
        telemetry.addData("Pitch", navx.getPitch());
        telemetry.addData("Roll", navx.getRoll());
        telemetry.addData("Compass", navx.getCompassHeading());

    }

    public void stop() {
        navx.close();
    }
}
