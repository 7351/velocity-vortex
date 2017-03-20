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
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.StateMachine;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "CapBallFarBlue", group = "Encoder Autonomous")
public class CapBallFarBlue extends OpMode implements StateMachine {

    int stage = 0;
    ElapsedTime time = new ElapsedTime();
    DriveTrain driveTrain;
    GyroUtils gyroUtils;
    ColorUtils colorUtils;
    //GyroSensor gyro;
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
        colorUtils = new ColorUtils(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);
        beaconUtils = new BeaconUtils(hardwareMap, colorUtils, alliance);
        new Lift(hardwareMap);
    }

    @Override
    public void start() {
        colorUtils.lineColorSensor.enableLed(true);
    }

    @Override
    public void loop() {

        if (stage == 0) {
            next(); // Save this for where the gyro should go
        }

        // TODO complete and find the driving distances and turning angles

        if (stage == 1) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 475, .75);
                drive.run();
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                next();
            }

        }

        if (stage == 2) {
            if (time.time() > 0.35) {
                next();
            }
        }
        if (stage == 3) {
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 86, GyroUtils.Direction.CLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                next();
            }
        }
        if (stage == 4) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }

        if (stage == 5) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 1300, 0.5);
                drive.run();
                if (shoot > 0) {
                    flyWheel.currentPower = flyWheel.defaultStartingPower;
                    flyWheel.currentlyRunning = true;
                }
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                stage++;
            }
        }

        flyWheel.powerMotor(); // Update flywheel values

        if (stage == 6) {
            if (time.time() > 1) {
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
        }

        // Missing turn statement

        if (stage == 7) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 1400, 0.5);
                drive.run();
            }
            if (drive.isCompleted()) {
                drive.completed();
                next();
            }
        }

        if (stage == 8) {
            if (time.time() > 2) {
                intake.stopIntake(Intake.IntakeSpec.A);
                next();
            }
        }

        telemetry.addData("Left Front Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Left Back Position: ", driveTrain.LeftBackMotor.getCurrentPosition());
        telemetry.addData("Right Front Position: ", driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("Right Back Position: ", driveTrain.RightBackMotor.getCurrentPosition());
        telemetry.addData("Stage", String.valueOf(stage));

    }

    @Override
    public void next() {
        stage++;
        time.reset();
        drive = null;
        turn = null;
    }
}