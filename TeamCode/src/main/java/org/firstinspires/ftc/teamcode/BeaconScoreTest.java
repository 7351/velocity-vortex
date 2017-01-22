package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
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
    int stage = 0;
    boolean withRange = true;
    ElapsedTime time;

    @Override
    public void init() {

        colorUtils = new ColorUtils(hardwareMap);
        driveTrain = new DriveTrain(hardwareMap);
        beaconUtils = new BeaconUtils(hardwareMap, colorUtils);
        time = new ElapsedTime();

    }

    @Override
    public void init_loop() {

        telemetry.addData("", "Left D-Pad: Red alliance, Right D-Pad: Blue alliance");
        if (gamepad1.dpad_left) alliance = "Red";
        if (gamepad1.dpad_right) alliance = "Blue";
        beaconUtils.setAlliance(alliance);

    }

    @Override
    public void start() {
        time.reset();
    }

    @Override
    public void loop() {

        if (stage == 0) {
            beaconUtils.actOnBeaconWithColorSensor();
            if (withRange) {
                if (rangeUtils.getDistance(INCH) > 1) {
                    driveTrain.powerLeft(0.2);
                    driveTrain.powerRight(0.2);
                } else {
                    driveTrain.stopRobot();
                    stage++;
                    time.reset();
                }
            } else {
                stage++;
                time.reset();
            }
        }

        if (stage == 1) {
            beaconUtils.actOnBeaconWithColorSensor();
            if (time.time() > 0.5) stage++;

        }

        if (stage == 2) {
            if (rangeUtils.getDistance(INCH) < 12) {
                driveTrain.powerLeft(-0.2);
                driveTrain.powerRight(-0.2);
            } else {
                driveTrain.stopRobot();
                stage++;
            }
        }

        telemetry.addData("Distance", rangeUtils.getDistance(INCH));
        telemetry.addData("Beacon Color", colorUtils.beaconColor().toString());


    }
}
