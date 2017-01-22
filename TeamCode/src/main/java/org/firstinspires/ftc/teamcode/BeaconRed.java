package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.BeaconUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "BeaconRed", group = "Encoder Autonomous")
public class BeaconRed extends OpMode {

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
    private String alliance = "Red";

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        colorUtils = new ColorUtils(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);
        beaconUtils = new BeaconUtils(hardwareMap, colorUtils, alliance);

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
                stage++;
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

        if (stage == 9) { // Turn
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 13, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                stage++;
                time.reset();
            }
        }

        if (stage == 10) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                turn = null;
                time.reset();
            }
        }


        if (stage == 11) { //drives forward 33 inches in seconds // OUTDATED LENGTH
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 3000, .75);
                drive.run();
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 12) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                drive = null;
                time.reset();
            }
        }

        if (stage == 13) { // Turn to 145
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 5, GyroUtils.Direction.CLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                stage++;
                time.reset();
            }
        }

        if (stage == 14) {
            if (time.time() > AutonomousUtils.WAITTIME){
                stage++;
                turn = null;
                time.reset();
            }
        }
        if (stage == 15) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 1400, .2);
                drive.run();
            }
            if (drive.isCompleted() || colorUtils.aboveWhiteLine()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 16) {
            if (time.time() > .5) {
                stage++;
                drive = null;
                time.reset();
            }
        }

        if (stage == 17){
            if (turn == null){
                turn = new EncoderTurn(driveTrain, 39, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted())
            {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 18) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 300, 0.35);
                drive.run();
            }
            beaconUtils.actOnBeaconWithColorSensor();
            if (drive.isCompleted() || time.time() > 2) { // 5 second time override
                driveTrain.stopRobot();
                turn = null;
                stage++;
                time.reset();
            }
        }
        if (stage == 19) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                drive = null;
                time.reset();
                stage++;
            }
        }
        if (stage == 20) {
            if (drive == null){
                drive = new EncoderDrive(driveTrain, 300, .25);
                drive.run();
            }
            if (drive.isCompleted() || time.time() > 3) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 21){
            if (time.time() > 0.25){
                drive = null;
                time.reset();
                stage++;
            }
        }

        if (stage == 22){
            if (drive == null){
                drive = new EncoderDrive(driveTrain, -400, .5);
                drive.run();
            }
            if (drive.isCompleted()){
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }

        if (stage == 23){
            if (time.time() > .25){
                drive = null;
                time.reset();
                stage++;
            }
        }



        telemetry.addData("Left Front Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Left Back Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Right Front Position: ", driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("Right Back Position: ", driveTrain.RightBackMotor.getCurrentPosition());
        telemetry.addData("Beacon", colorUtils.beaconColor().toString());
        telemetry.addData("Stage", String.valueOf(stage));

    }
}