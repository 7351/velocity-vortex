package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

@Autonomous(name = "Encoder2BeaconsBlue", group = "Encoder")
@Disabled // TODO: This program is to be worked on for Meet 4
public class Encoder2BeaconsBlue extends OpMode {

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
                drive = new EncoderDrive(driveTrain, 300, .6);
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

        if (stage == 3) { // Turn to 90
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 25, GyroUtils.Direction.CLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted() || colorUtils.aboveWhiteLine()) {
                turn.completed();
                stage++;
                time.reset();
            }
        }

        if (stage == 4) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                turn = null;
                time.reset();
            }
        }


        if (stage == 5) { //drives forward 33 inches in seconds // OUTDATED LENGTH
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

        if (stage == 6) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                drive = null;
                time.reset();
            }
        }

        if (stage == 7) { // Turn to 145
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 38, GyroUtils.Direction.CLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                stage++;
                time.reset();
            }
        }

        if (stage == 8) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                turn = null;
                time.reset();
            }
        }
        if (stage == 9) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 600, .5);
                drive.run();
            }
            if (drive.isCompleted()){
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 10) {
            if (time.time() > .5) {
                stage++;
                drive = null;
                time.reset();
            }
        }

        // Initialize the beacon subroutine from BeaconSlamTest

        if (stage == 11) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 50, .25);
                drive.run();
            }
            if (drive.isCompleted()){
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
        if (stage == 13) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, -75, .5);
                drive.run();
            }
            if (drive.isCompleted()){
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 14) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                drive = null;
                time.reset();
            }
        }
        if (stage == 15) {
            switch (colorUtils.beaconColor()) {
                case RED:
                    switch (alliance) {
                        case "Blue":
                            if (time.time() > 5.1) {
                                time.reset();
                                stage = 11;
                            }
                            break;
                        case "Red":
                            time.reset();
                            stage++;
                            break;
                    }
                    break;
                case BLUE:
                    switch (alliance) {
                        case "Blue":
                            time.reset();
                            stage++;
                            break;
                        case "Red":
                            if (time.time() > 5.1) {
                                time.reset();
                                stage = 11;
                            }
                            break;
                    }
                    break;
                case NONE:
                    switch (alliance) {
                        case "Blue":
                            if (time.time() > 5.1) {
                                time.reset();
                                stage = 21;
                            }
                            break;
                        case "Red":
                            time.reset();
                            stage++;
                            break;
                    }
            }
        }

        if (stage == 16) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, -600, .5);
                drive.run();
            }
            if (drive.isCompleted()){
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 17) {
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 70, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()){
                turn.completed();
                time.reset();
                drive = null;
                stage++;
            }
        }

        if (stage == 18) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 2000, .65);
                drive.run();
            }
            if (drive.isCompleted()){
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }

        if (stage == 19) { // Turn to 145
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 70, GyroUtils.Direction.CLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                stage = 19;
                time.reset();
            }
        }

        if (stage == 20) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                turn = null;
                time.reset();
            }
        }
        if (stage == 21) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 600, .5);
                drive.run();
            }
            if (drive.isCompleted()){
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 22) {
            if (time.time() > .5) {
                stage++;
                drive = null;
                time.reset();
            }
        }

        // Initialize the beacon subroutine from BeaconSlamTest

        if (stage == 23) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 50, .25);
                drive.run();
            }
            if (drive.isCompleted()){
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 24) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                drive = null;
                time.reset();
            }
        }
        if (stage == 25) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, -75, .5);
                drive.run();
            }
            if (drive.isCompleted()){
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 26) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                drive = null;
                time.reset();
            }
        }
        if (stage == 27) {
            switch (colorUtils.beaconColor()) {
                case RED:
                    switch (alliance) {
                        case "Blue":
                            if (time.time() > 5.1) {
                                time.reset();
                                stage = 23;
                            }
                            break;
                        case "Red":
                            time.reset();
                            stage++;
                            break;
                    }
                    break;
                case BLUE:
                    switch (alliance) {
                        case "Blue":
                            time.reset();
                            stage++;
                            break;
                        case "Red":
                            if (time.time() > 5.1) {
                                time.reset();
                                stage = 23;
                            }
                            break;
                    }
                    break;
                case NONE:
                    switch (alliance) {
                        case "Blue":
                            if (time.time() > 5.1) {
                                time.reset();
                                stage = 23;
                            }
                            break;
                        case "Red":
                            time.reset();
                            stage++;
                            break;
                    }
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