package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils;

/**
 * Created by Dynamic Signals on 1/16/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "GyroTurnTest", group = "Testing")
public class GyroTurnTest extends OpMode {

    DriveTrain driveTrain;
    GyroUtils gyroUtils;
    GyroTurn gyroTurn;
    int stage = 0;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);

    }

    @Override
    public void start() {
        gyroUtils.gyro.calibrate();
    }

    @Override
    public void loop() {
        if (stage == 0) {
            if (!gyroUtils.gyro.isCalibrating()) {
                gyroTurn = new GyroTurn(gyroUtils, driveTrain, 50);
                stage++;
            }
        }
        if (stage == 1) {
            gyroTurn.run();
            if (gyroTurn.isCompleted()) {
                gyroTurn.completed();
                stage++;
            }
        }

        telemetry.addData("Stage", stage);
        telemetry.addData("Gyro", gyroUtils.gyro.getHeading());
    }
}
