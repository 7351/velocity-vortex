package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.GyroSensor;
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
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.StateMachineOpMode;


/**
 * Created by Kate on 3/24/2017.
 */

@Autonomous(name = "cornerRedClose", group = "AWorking")
public class cornerRedClose extends OpMode implements StateMachine {

    int stage = 0;
    ElapsedTime time = new ElapsedTime();
    DriveTrain driveTrain;
    GyroUtils gyroUtils;
    ColorUtils colorUtils;
    Intake intake;
    FlyWheel flyWheel; //0.86
    EncoderDrive drive;
    BeaconUtils beaconUtils;
    EncoderTurn turn;
    private String alliance = "Red";
    private int shoot = 2;


    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        beaconUtils = new BeaconUtils(hardwareMap, colorUtils, alliance);
        colorUtils = new ColorUtils(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);
        new Lift(hardwareMap);

    }

    @Override
    public void start() {
        colorUtils.lineColorSensor.enableLed(true);
    }

    @Override
    public void loop() {
        if (stage == 0) {
            next();
        }

        if (stage == 1) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 390, .75);//410, .75);
                drive.run();
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                next();
            }

        }

        if (stage == 2) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }
        if (stage == 3) {
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 86, GyroUtils.Direction.COUNTERCLOCKWISE);
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
                drive = new EncoderDrive(driveTrain, 1375, 0.5);
                drive.run();
                if (shoot > 0) {
                    flyWheel.currentPower = flyWheel.defaultStartingPower;
                    flyWheel.currentlyRunning = true;
                }
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                next();
            }
        }

        flyWheel.powerMotor(); // Update flywheel values

        if (stage == 6) {
            if (time.time() > .25) {
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
        if (stage == 7) {
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 112, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                next();
            }
        }
        if (stage == 8){
            if (time.time() > 12) {
                next();
            }
        }
        if (stage == 9) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 3800, 0.5);
                drive.run();
            }
            if (drive.isCompleted()) {
                drive.completed();
                next();
            }
        }

        if (stage == 10) {
            if (time.time() > 2) {
                intake.stopIntake(Intake.IntakeSpec.A);
               // next();
            }
        }

        telemetry.addData("Stage", String.valueOf(stage));
    }

    @Override
    public void next() {
        time.reset();
        stage++;
        turn = null;
        drive = null;
    }
}