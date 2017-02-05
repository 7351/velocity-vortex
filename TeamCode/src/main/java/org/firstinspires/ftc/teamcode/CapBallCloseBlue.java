package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
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

@Autonomous(name = "CapBallCloseBlue", group = "Encoder Autonomous")
public class CapBallCloseBlue extends OpMode {

    int stage = 0;
    ElapsedTime time = new ElapsedTime();
    DriveTrain driveTrain;
    GyroUtils gyroUtils;
    ColorUtils colorUtils;
    GyroSensor gyro;
    Intake intake;
    FlyWheel flyWheel;
    EncoderDrive drive;
    EncoderTurn turn;
    private String alliance = "Blue";
    private int shoot = 2;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        colorUtils = new ColorUtils(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);

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

        if (stage == 1) { //drives forward 0.25 seconds
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 700, .6);
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
            if (shoot > 0) {
                flyWheel.currentPower = flyWheel.defaultStartingPower;
                flyWheel.currentlyRunning = true;
            }
            stage++;
        }

        flyWheel.powerMotor(); // Update flywheel values

        if (stage == 4) {
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
                flyWheel.currentlyRunning = false;
            }
        }

        if (stage == 5) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }

        if (stage == 6) { // Turn to z90
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 35, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                stage++;
                time.reset();
            }
        }

        if (stage == 7) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                turn = null;
                time.reset();
            }
        }

        if (stage == 8) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 1000, .6);
                drive.run();
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }

        }

        if (stage == 9) {
            if (time.time() > .5) {
                stage++;
                drive = null;
                time.reset();
            }
        }
        if (stage == 10) {
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 70, GyroUtils.Direction.CLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }

        if (stage == 11) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                turn = null;
                time.reset();
            }
        }

        if (stage == 12) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 1500, .6);
                drive.run();
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }

        }

        if (stage == 13) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                drive = null;
                time.reset();
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