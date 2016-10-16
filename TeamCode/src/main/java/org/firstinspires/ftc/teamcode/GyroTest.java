package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;

/**
 * Created by Leo on 10/13/2016.
 */

@Autonomous(name = "GyroTesting", group = "Testing")
public class GyroTest extends OpMode {

    GyroUtils gyroUtils;
    DriveTrain driveTrain;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain);
        gyroUtils.gyro.calibrate();

    }

    @Override
    public void start() {
        gyroUtils.gyro.calibrate();
    }

    @Override
    public void loop() {

        driveTrain.powerLeft(0.4);
        driveTrain.powerRight(0.4);

        telemetry.addData("Calibrate", String.valueOf(gyroUtils.gyro.isCalibrating()));

    }
}
