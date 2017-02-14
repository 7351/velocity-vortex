package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
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

@Autonomous(name = "CapBallFarBlue", group = "Encoder Autonomous")
public class CapBallFarBlue extends OpMode {

    int stage = 0;
    ElapsedTime time = new ElapsedTime();
    DriveTrain driveTrain;
    GyroUtils gyroUtils;
    ColorUtils colorUtils;
    GyroSensor gyro;
    Intake intake;
    BeaconUtils beaconUtils;
    FlyWheel flyWheel;
    EncoderDrive drive;
    EncoderTurn turn;
    private String alliance = "Red";
    private int shoot = 2;


    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        colorUtils = new ColorUtils(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);
        beaconUtils = new BeaconUtils(hardwareMap, colorUtils, alliance);

        driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        gyro = gyroUtils.gyro;
        gyro.calibrate();
        beaconUtils.rotateServo(BeaconUtils.ServoPosition.CENTER);
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
                driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                driveTrain.LeftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
            telemetry.addData("Calibrating", String.valueOf(gyro.isCalibrating()));
        }

        if (stage == 1) { //drives forward 0.25 seconds
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 400, .75);
                drive.run();
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }

        }

        if (stage == 2) {
            if (time.time() > 0.35) {
                drive = null;
                stage++;
                time.reset();
            }
        }
        if (stage == 3) {
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 33, GyroUtils.Direction.CLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                stage++;
                time.reset();
            }
        }
        if (stage == 4) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                turn = null;
                stage++;
                time.reset();
            }
        }

        if (stage == 5) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 1600, 0.5);
                drive.run();
                if (shoot > 0) {
                    flyWheel.currentPower = flyWheel.defaultStartingPower;
                    flyWheel.currentlyRunning = true;
                }
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                drive = null;
                time.reset();
                stage++;
            }
        }

        flyWheel.powerMotor(); // Update flywheel values

        if (stage == 6) {
            if (shoot == 1) {
                intake.setIntake(Intake.IntakeSpec.A, Intake.IntakeDirection.IN);
            }
            if (shoot == 2) {
                intake.setIntake(Intake.IntakeSpec.BOTH, Intake.IntakeDirection.IN);
            }
            if (time.time() > 2.5 || shoot <= 0) {
                stage++;
                time.reset();
                intake.stopIntake(Intake.IntakeSpec.BOTH);
                intake.setIntake(Intake.IntakeSpec.A, Intake.IntakeDirection.OUT);
                flyWheel.currentlyRunning = false;
            }
        }

        if (stage == 7) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 1400, 0.5);
                drive.run();
            }
            if (drive.isCompleted()) {
                drive.completed();
                time.reset();
                stage++;
            }
        }

        if (stage == 8) {
            if (time.time() > 2) {
                intake.stopIntake(Intake.IntakeSpec.A);
                stage++;
                time.reset();
            }
        }

        telemetry.addData("Left Front Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Left Back Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Right Front Position: ", driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("Right Back Position: ", driveTrain.RightBackMotor.getCurrentPosition());
        /*telemetry.addData("Color: ", String.valueOf(colorUtils.lineColorSensor.red()));*/
        telemetry.addData("Stage", String.valueOf(stage));
        /*telemetry.addData("Gyro", String.valueOf(gyro.getHeading()));
        telemetry.addData("Time", String.valueOf(time.time()));
        // telemetry.addData("Fly Wheel Power: ", String.valueOf())*/

    }
}