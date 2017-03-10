package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.BeaconUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.RangeUtils;

/**
 * Created by Leo on 1/15/2017.
 */

@Autonomous(name = "RangeTestNat")
@Disabled
public class RangeTestNat extends OpMode {

    DriveTrain driveTrain;
    ColorUtils colorUtils;
    BeaconUtils beaconUtils;
    ElapsedTime time = new ElapsedTime();
    int stage = 0;
    double targetDistance = 7;
    double fixDistance = 4;
    double backUpDistance = 7;
    EncoderDrive drive = null;
    EncoderTurn turn = null;
    RangeUtils rangeUtils;
    ColorUtils.Color actedColor;
    String alliance = "Red";

    double drivePower = 0.13;

    int selectedDistance = 0;
    double[] lookupTargetDistance = {5, 15, 20, 45, 90};

    double distance;

    String preferredMethod = "combined";

    boolean A = false;
    boolean Y = false;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        rangeUtils = new RangeUtils(hardwareMap);
        beaconUtils = new BeaconUtils(hardwareMap, colorUtils, alliance);

    }

    @Override
    public void init_loop() {
        if (gamepad1.a) {
            if (!A) {
                selectedDistance = Range.clip(selectedDistance - 1, 0, lookupTargetDistance.length - 1);
                A = true;
            }
        } else {
            A = false;
        }
        if (gamepad1.y) {
            if (!Y) {
                selectedDistance = Range.clip(selectedDistance + 1, 0, lookupTargetDistance.length - 1);
                Y = true;
            }
        } else {
            Y = false;
        }

        telemetry.addData("Distance selected", lookupTargetDistance[selectedDistance]);

        if (gamepad1.dpad_down) {
            preferredMethod = "combined";
        }
        if (gamepad1.dpad_right) {
            preferredMethod = "optical";
        }
        if (gamepad1.dpad_left) {
            preferredMethod = "ultrasonic";
        }

        telemetry.addData("Method", preferredMethod);
    }

    @Override
    public void loop() {

        double optical = rangeUtils.rangeSensor.cmOptical();
        double ultrasonic = rangeUtils.rangeSensor.cmUltrasonic();
        double combined = rangeUtils.rangeSensor.getDistance(DistanceUnit.CM);

        if (preferredMethod.equals("combined")) distance = combined;
        if (preferredMethod.equals("ultrasonic")) distance = ultrasonic;
        if (preferredMethod.equals("optical")) distance = optical;

        if (stage == 0) {

            if (distance > lookupTargetDistance[selectedDistance]) { // If the distance sensor is greater than 12 centimeters
                driveTrain.powerLeft(0.13); // Drive 0.13
                driveTrain.powerRight(0.13);
            } else {
                stage++; // Go to the next stage when we are greater than 3 inches
                driveTrain.stopRobot(); // Stop the drive train from moving
                time.reset();
            }
        }
        /*if (stage == 3){
            if (time.time() > 5)
                stage++;
        }
        if (stage == 4) { // Act on beacon with color sensor/Flip the sunglasses
            if (!colorUtils.beaconColor().equals(ColorUtils.Color.NONE)) {
                actedColor = beaconUtils.actOnBeaconWithColorSensor();
                stage++;
                time.reset();
            } else {
                stage = AutonomousUtils.DEADBEEF;
                AutonomousUtils.failSafeError(hardwareMap);
                time.reset();
            }
        }

        if (stage == 5) { // Get the range to the wall in cm + 120 ticks more, set encoders and drive to the wall
            if (time.time() > 2.5) {
                driveTrain.powerLeft(.5);
                driveTrain.powerRight(.5);
            }
            else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 6) { // Make a 6 degree turn (wiggle) to make sure we hit the button for beacon 1
            if (turn == null) {
                GyroUtils.Direction turnDirection = (beaconUtils.getCurrentPosition().equals(BeaconUtils.ServoPosition.TRIGGER_LEFT)) ?
                        GyroUtils.Direction.CLOCKWISE : GyroUtils.Direction.COUNTERCLOCKWISE;
                turn = new EncoderTurn(driveTrain, 6, turnDirection);
                turn.run();
            }
            if (turn.isCompleted() || time.time() > 0.5) {
                turn.completed();
                stage++;
                time.reset();
                turn = null;
            }
        }

        if (stage == 7) { // Make a 6 degree turn (wiggle back) to attempt to strighten up on the beacon
            if (turn == null) {
                GyroUtils.Direction turnDirection = (beaconUtils.getCurrentPosition().equals(BeaconUtils.ServoPosition.TRIGGER_LEFT)) ?
                        GyroUtils.Direction.COUNTERCLOCKWISE : GyroUtils.Direction.CLOCKWISE;
                turn = new EncoderTurn(driveTrain, 6, turnDirection);
                turn.run();
            }
            if (turn.isCompleted() || time.time() > 0.5) {
                turn.completed();
                stage++;
                time.reset();
                turn = null;
            }
        }
        if (stage == 8){
            if (distance2 < backUpDistance){
                driveTrain.powerLeft(-0.13);
                driveTrain.powerRight(-0.13);
            }
            else {
                stage++;
                driveTrain.stopRobot();
            }
        }*/

        telemetry.addData("optical", optical);
        telemetry.addData("ultrasonic", ultrasonic);
        telemetry.addData("combined", combined);
        telemetry.addData("Stage", stage);

    }
}
