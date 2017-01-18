package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;
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
    Servo beaconServo;
    GyroUtils gyroUtils;
    ColorUtils colorUtils;
    GyroSensor gyro;
    Intake intake;
    FlyWheel flyWheel;
    EncoderDrive drive;
    EncoderTurn turn;
    private String alliance = "Red";
    double rightButtonPosition = 0.2;
    double leftButtonPosition = 0.5;
    double middlePosition = 0.5;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        colorUtils = new ColorUtils(hardwareMap);
        beaconServo = hardwareMap.servo.get("BeaconServo");
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);

        gyro = gyroUtils.gyro;
        gyro.calibrate();

    }

    @Override
    public void start() {

        gyro.calibrate();
        colorUtils.lineColorSensor.enableLed(true);
        beaconServo.setPosition(middlePosition);
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

        if (stage == 1) { //drives forward 0.25 seconds
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

        if (stage == 2) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                drive = null;
                turn = null;
                time.reset();
            }
        }

        if (stage == 3) {
            double flyWheelLaunchPower = 1;
            flyWheel.FlyWheelMotor.setPower(flyWheelLaunchPower);
            stage++;
        }

        if (stage == 4) {
            if (time.time() > 3) {
                time.reset();
                stage++;
            }
        }

        if (stage == 5) {
            if (time.time() < 2) {
                intake.setIntakePower(Intake.IntakeSpec.B, -1);
                intake.setIntakePower(Intake.IntakeSpec.A, 1);
            } else {
                time.reset();
                stage++;
            }
        }

        if (stage == 6) {
            if (time.time() > 1.2) {
                time.reset();
                stage++;
            }
        }


        if (stage == 7) {
            if (time.time() > 2) {
                intake.stopIntake(Intake.IntakeSpec.A);
                intake.stopIntake(Intake.IntakeSpec.B);
                flyWheel.FlyWheelMotor.setPower(0);
                time.reset();
                stage++;
            }
        }

        if (stage == 8) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }

        if (stage == 9) { // Turn to 90
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 45, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                stage++;
                time.reset();
            }
        }

        if (stage == 10) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                turn = null;
                time.reset();
            }
        }


        if (stage == 11) { //drives forward 33 inches in seconds // OUTDATED LENGTH
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 2700, .75);
                drive.run();
            }
            if (drive.isCompleted() || colorUtils.aboveWhiteLine()) {
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
                turn = new EncoderTurn(driveTrain, 11, GyroUtils.Direction.COUNTERCLOCKWISE);
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
                drive = new EncoderDrive(driveTrain, 350, .5);
                drive.run();
            }
            if (drive.isCompleted() || time.time() > 2) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 16) {
            if (time.time() > .5) {
                stage = 18;
                drive = null;
                time.reset();
            }
        }

        // Initialize the beacon subroutine with beacon pusher
        /*if (stage == 17) {
            switch (colorUtils.beaconColor()) {
                case RED:
                    switch (alliance) {
                        case "Blue":
                            beaconServo.setPosition(rightButtonPosition);
                            time.reset();
                            stage++;
                            break;
                        case "Red":
                            beaconServo.setPosition(leftButtonPosition);
                            time.reset();
                            stage++;
                            break;
                    }
                    break;
                case BLUE:
                    switch (alliance) {
                        case "Blue":
                            beaconServo.setPosition(leftButtonPosition);
                            time.reset();
                            stage++;
                            break;
                        case "Red":
                            beaconServo.setPosition(rightButtonPosition);
                            time.reset();
                            stage++;
                            break;
                    }
            }
        }*/

        if (stage == 18) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 200, 0.35);
                drive.run();
            }
            if (drive.isCompleted() || time.time() > 2) { // 5 second time override
                driveTrain.stopRobot();
                beaconServo.setPosition(middlePosition);
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
                drive = new EncoderDrive(driveTrain, -500, .5);
                drive.run();
            }
            if (drive.isCompleted()) {
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
        if (stage == 21){
            if (turn == null){
                turn = new EncoderTurn(driveTrain, 70, GyroUtils.Direction.CLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()){
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 22){
            if (time.time() > 0.25)
            {
                turn = null;
                time.reset();
                stage++;
            }
        }
        if (stage == 23){
            if (drive == null){
                drive = new EncoderDrive(driveTrain, 3000, .5);
                drive.run();
            }
            if (drive.isCompleted() || colorUtils.aboveWhiteLine()){
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 24){
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