package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.BeaconUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.RangeUtils;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "BeaconRedAlt", group = "Encoder Autonomous")
public class BeaconRedAlt extends OpMode {

    int stage = 0;
    ElapsedTime time = new ElapsedTime();
    DriveTrain driveTrain;
    GyroUtils gyroUtils;
    ColorUtils colorUtils;
    BeaconUtils beaconUtils;
    GyroSensor gyro;
    Intake intake;
    FlyWheel flyWheel;
    EncoderDrive drive;
    EncoderTurn turn;
    RangeUtils rangeUtils;
    private String alliance = "Red";

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        colorUtils = new ColorUtils(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);
        beaconUtils = new BeaconUtils(hardwareMap, colorUtils, alliance);
        rangeUtils = new RangeUtils(hardwareMap);

        gyro = gyroUtils.gyro;
        gyro.calibrate();

    }

    @Override
    public void start() {

        gyro.calibrate();
        colorUtils.lineColorSensor.enableLed(true);
    }

    @Override
    public void loop() {
        if (stage == 0) {//calibrates to 0
            if (!gyro.isCalibrating()) {
                stage++;
                time.reset();
            }
            telemetry.addData("Calibrating", String.valueOf(gyro.isCalibrating()));
        }

        if (stage == 1) { //drives forward a little bit
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 1300, .6);
                drive.run();
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }

        }

        if (stage == 2) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage = 9;
                drive = null;
                turn = null;
                time.reset();
            }
        }

        if (stage == 3) { // Start flywheel
            double flyWheelLaunchPower = 1;
            flyWheel.FlyWheelMotor.setPower(flyWheelLaunchPower);
            stage++;
        }

        if (stage == 4) { // Wait 2 seconds
            if (time.time() > 2) {
                time.reset();
                stage++;
            }
        }

        if (stage == 5) { // Start intakes
            if (time.time() < 1) {
                intake.setIntakePower(Intake.IntakeSpec.B, -1);
                intake.setIntakePower(Intake.IntakeSpec.A, 1);
            } else {
                time.reset();
                stage++;
            }
        }

        if (stage == 6) { // Wait almost 1 second
            if (time.time() > .7) {
                time.reset();
                stage++;
            }
        }


        if (stage == 7) {
            if (time.time() > 2) { // Wait 2 seconds then turn off intakes and flywheel
                intake.stopIntake(Intake.IntakeSpec.BOTH);
                flyWheel.FlyWheelMotor.setPower(0);
                time.reset();
                stage++;
            }
        }

        if (stage == 8) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }

        if (stage == 9) { // Drive backwards a wee bit
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, -400, 0.2);
                drive.run();
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 10) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
                drive = null;
                turn = null;
            }
        }
        if (stage == 11) {
            if (turn == null) { // Turn to the right direction to get to the white line
                turn = new EncoderTurn(driveTrain, 43, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 12) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
                drive = null;
                turn = null;
            }
        }
        if (stage == 13) { // Drive until white line
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 3500, 0.45);
            }
            drive.runWithDecrementPower(0.00035);
            if (drive.isCompleted() || colorUtils.aboveWhiteLine()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 14) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
                drive = null;
                turn = null;
            }
        }
        if (stage == 15) { // Turn to face the beacon
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 25, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 16) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
                drive = null;
                turn = null;
                driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }
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
                drive = null;
                turn = null;
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
                drive = null;
                turn = null;
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


        telemetry.addData("F", driveTrain.LeftFrontMotor.getCurrentPosition() + ":" + driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("B", driveTrain.LeftBackMotor.getCurrentPosition() + ":" + driveTrain.RightBackMotor.getCurrentPosition());
        telemetry.addData("Range", rangeUtils.getDistance(DistanceUnit.INCH));
        telemetry.addData("Beacon", colorUtils.beaconColor().toString());
        telemetry.addData("Stage", String.valueOf(stage));

    }
}