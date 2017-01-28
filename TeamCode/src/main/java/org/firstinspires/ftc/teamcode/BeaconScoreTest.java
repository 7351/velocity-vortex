package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.BeaconUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.RangeUtils;

import static org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.INCH;

/**
 * Created by Dynamic Signals on 1/21/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "BeaconScoreTest", group = "Testing")
public class BeaconScoreTest extends OpMode {

    DriveTrain driveTrain;
    BeaconUtils beaconUtils;
    ColorUtils colorUtils;
    RangeUtils rangeUtils;
    String alliance;
    int stage = 17;
    boolean withRange = false;
    ElapsedTime time;

    @Override
    public void init() {

        colorUtils = new ColorUtils(hardwareMap);
        driveTrain = new DriveTrain(hardwareMap);
        beaconUtils = new BeaconUtils(hardwareMap, colorUtils);
        time = new ElapsedTime();
        alliance = beaconUtils.getAlliance();

    }

    @Override
    public void init_loop() {

        telemetry.addData("Alliance", "Left: Red, Right: Blue");
        if (gamepad1.dpad_left) alliance = "Red";
        if (gamepad1.dpad_right) alliance = "Blue";
        beaconUtils.setAlliance(alliance);
        telemetry.addData("Selection", beaconUtils.getAlliance());

    }

    @Override
    public void start() {
        time.reset();
    }

    @Override
    public void loop() {

        if (stage == 17) { // Drive until we're 1 inch away
            if (rangeUtils.getDistance(DistanceUnit.INCH) < 1) {
                driveTrain.powerLeft(0.4);
                driveTrain.powerRight(0.4);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 18) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }
        if (stage == 19) { // Drive forward with time for 1 second with activating beacon
            beaconUtils.actOnBeaconWithColorSensor();
            if (time.time() < 2) {
                driveTrain.powerLeft(0.25);
                driveTrain.powerRight(0.25);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.time();
            }
        }
        if (stage == 20) { // Wait regular plus 0.5 sec
            if (time.time() > AutonomousUtils.WAITTIME + 0.5) {
                stage++;
                time.reset();
                beaconUtils.rotateServo(BeaconUtils.ServoPosition.CENTER);
            }
        }
        if (stage == 21) { // Drive backwards 8 inches
            if (rangeUtils.getDistance(DistanceUnit.INCH) < 4) {
                driveTrain.powerLeft(-0.4);
                driveTrain.powerRight(-0.4);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }


        if (withRange) telemetry.addData("Distance", rangeUtils.getDistance(INCH));
        telemetry.addData("Beacon Color", colorUtils.beaconColor().toString());


    }
}
