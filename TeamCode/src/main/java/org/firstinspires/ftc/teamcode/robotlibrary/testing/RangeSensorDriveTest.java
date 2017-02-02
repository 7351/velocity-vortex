package org.firstinspires.ftc.teamcode.robotlibrary.testing;

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
@Disabled
public class RangeSensorDriveTest extends OpMode {

    DriveTrain driveTrain;
    RangeUtils rangeUtils;
    int stage = 0;
    int targetDistance = 3;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        rangeUtils = new RangeUtils(hardwareMap);

    }

    @Override
    public void loop() {

        double distance = rangeUtils.getDistance(DistanceUnit.INCH);

        if (stage == 0) {

            if (distance < 3) { // If the distance sensor is less than 3 inches
                driveTrain.powerLeft(-0.25); // Drive -0.25
                driveTrain.powerRight(-0.25);
            } else {
                stage++; // Go to the next stage when we are greater than 3 inches
                driveTrain.stopRobot(); // Stop the drive train from moving
            }
        }

        telemetry.addData("Distance", rangeUtils.rangeSensor.getDistance(DistanceUnit.INCH));
        telemetry.addData("Stage", stage);

    }
}
