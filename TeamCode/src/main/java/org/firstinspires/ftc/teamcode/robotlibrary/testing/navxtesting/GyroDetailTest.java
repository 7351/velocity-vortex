package org.firstinspires.ftc.teamcode.robotlibrary.testing.navxtesting;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.StateMachineOpMode;

/**
 * Created by Dynamic Signals on 6/4/2017.
 */

@Autonomous(name = "GyroDetailTest", group = "Testing")
public class GyroDetailTest extends StateMachineOpMode {

    DriveTrain driveTrain;
    AHRS navx;
    GyroUtils.GyroDetail detail;

    @Override
    public void init() {

        //driveTrain = new DriveTrain(hardwareMap);
        navx = AHRS.getInstance(hardwareMap);

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {


        if (stage == 0) {
            if (!navx.isCalibrating()) {
                navx.zeroYaw();
                next();
            }
            telemetry.addData("Calibrating", navx.isCalibrating());
        }
        if (stage == 1) {
            detail = new GyroUtils.GyroDetail(navx, -90);
            next();
        }
        if (stage == 2) {
            detail.updateData();
            telemetry.addData("Degrees left", detail.degreesOff);
            telemetry.addData("Direction", detail.turnDirection);
            telemetry.addData("Percentage complete", detail.percentComplete);
        }

        telemetry.addData("Stage", stage);
        telemetry.addData("Yaw", navx.getYaw());
        //telemetry.addData("Voltage", driveTrain.getVoltage());
        telemetry.addData("Connected", navx.isConnected());

    }

    @Override
    public void next() {
        stage++;
    }
}
