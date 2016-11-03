package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DynamicAutonomousSelector;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;

/**
 * Created by Dynamic Signals on 11/1/2016.
 */
@Autonomous(name = "RotateTest", group = "Testing")
public class RotateTest extends OpMode {

    DriveTrain driveTrain;
    GyroUtils gyroUtils;
    DynamicAutonomousSelector das;

    double divideNumber;
    int stage = 0;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        das = new DynamicAutonomousSelector();

    }

    @Override
    public void start() {

        gyroUtils.gyro.calibrate();

        if (das.getSelectorChoices().containsKey("rotateDivider")) {
            divideNumber = Double.valueOf(das.getSelectorChoices().get("rotateDivider"));
        }

    }

    @Override
    public void loop() {

        if (stage == 0) {
            if (!gyroUtils.gyro.isCalibrating()) {
                stage++;
            }
        }

        if (stage == 1) {
            //gyroUtils.resetDelta();
            //gyroUtils.gTurn(2, 2);
        } else {
            stage++;
        }


        telemetry.addData("Divider", String.valueOf(divideNumber));
        telemetry.addData("GyroNew", String.valueOf(gyroUtils.i2cGyro.getIntegratedZValue()));
        telemetry.addData("Gyro", String.valueOf(gyroUtils.gyro.getHeading()));
    }
}
