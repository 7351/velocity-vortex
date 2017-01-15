package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.RangeUtils;

/**
 * Created by Leo on 1/15/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "RangeSensorDriveTest", group = "Testing")
public class RangeSensorDriveTest extends OpMode {

    DriveTrain driveTrain;
    RangeUtils rangeUtils;
    int stage = 0;
    int targetDistance = 3;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        rangeUtils = new RangeUtils(hardwareMap);

        rangeUtils.rangeSensor.resetDeviceConfigurationForOpMode();

    }

    @Override
    public void loop() {

        double distance = rangeUtils.getDistance(DistanceUnit.INCH);

        if (stage == 0) {

            if (distance < targetDistance) {
                double power = -Range.clip(((targetDistance - distance) / targetDistance), 0, 1);
                driveTrain.powerLeft(power);
                driveTrain.powerRight(power);
            } else {
                RobotLog.d("Condition satisfied with " + String.valueOf(distance));
                driveTrain.stopRobot();
                stage++;
            }
        }

        telemetry.addData("Distance", rangeUtils.rangeSensor.getDistance(DistanceUnit.INCH));
        telemetry.addData("Stage", stage);

    }
}
