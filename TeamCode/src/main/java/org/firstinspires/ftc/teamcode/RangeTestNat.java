package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;

/**
 * Created by Leo on 1/15/2017.
 */

@Autonomous(name = "WORKGODDAMMIT")
public class RangeTestNat extends OpMode {

    DriveTrain driveTrain;
    ElapsedTime time = new ElapsedTime();
    public ModernRoboticsI2cRangeSensor rangeSensor;
    int stage = 0;
    double targetDistance = 6.5;
    double fixDistance = 15;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "beaconRange");

    }

    @Override
    public void loop() {

        double distance = rangeSensor.getDistance(DistanceUnit.INCH);
        double distance2 = rangeSensor.getDistance(DistanceUnit.CM);

        if (stage == 0) {

            if (distance > targetDistance) { // If the distance sensor is greater than 12 centimeters
                driveTrain.powerLeft(0.13); // Drive 0.13
                driveTrain.powerRight(0.13);
            } else {
                stage++; // Go to the next stage when we are greater than 3 inches
                driveTrain.stopRobot(); // Stop the drive train from moving
            }
        }
        if (stage == 1){
            if (time.time() > 3)
                stage++;
        }
        if (stage == 2){
            if (distance2 < fixDistance){
                driveTrain.powerLeft(-0.13);
                driveTrain.powerRight(-0.13);
            }
            else {
                stage++;
                driveTrain.stopRobot();
            }
        }

        telemetry.addData("DistanceINCH", rangeSensor.getDistance(DistanceUnit.INCH));
        telemetry.addData("DistanceCM", rangeSensor.getDistance(DistanceUnit.CM));
        telemetry.addData("Stage", stage);

    }
}
