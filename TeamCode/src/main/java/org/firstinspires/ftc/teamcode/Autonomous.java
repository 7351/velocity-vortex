package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.EncoderTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.TBDName;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Dynamic Signals on 11/10/2016.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous", group = "Autonomous")
public class Autonomous extends OpMode {

    TBDName tbdName; // Robot object

    private int stage = -1; // First stage is -1 (wait time)
    private ElapsedTime time = new ElapsedTime(); // regular match time variable minus gyro cal. and wait
    private ElapsedTime delayTime = new ElapsedTime(); // delay time elapsed time

    // Selector variables
    private double delay;
    private String alliance;
    private int shoot;
    private String target;

    private DriveTrain driveTrain;
    private GyroUtils gyroUtils;
    private ColorUtils colorUtils;
    private Intake intake;
    private FlyWheel flyWheel;
    private Lift lift;

    private EncoderTurn turn;
    private EncoderDrive drive;

    @Override
    public void init() {

        tbdName = new TBDName(hardwareMap, telemetry, false);

        driveTrain = tbdName.driveTrain;
        gyroUtils = tbdName.gyroUtils;
        colorUtils = tbdName.colorUtils;
        intake = tbdName.intake;
        flyWheel = tbdName.flyWheel;
        lift = tbdName.lift;

        delay = tbdName.das.getNumberDouble("delay", 0); // 0 default wait time
        alliance = tbdName.das.getRadio("alliance", "ns"); // Not selected - ns
        shoot = tbdName.das.getNumberInt("shoot", 2); // We want to shoot twice by default
        target = tbdName.das.getRadio("target", "ns"); // Not selected - ns
    }

    @Override
    public void init_loop() {

        // We log all the selector choices just to show the drive team if what they type in was correct
        Iterator it = tbdName.das.getSelectorChoices().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            telemetry.addData((String) pair.getKey(), pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

    }

    @Override
    public void start() {

        tbdName.start();
        time.reset();
        delayTime.reset();

    }

    @Override
    public void loop() {

        if (stage == -1) {
            if (delayTime.time() > delay) {
                stage++;
            }
        }
        if (stage == 0) {
            if (!tbdName.gyroUtils.gyro.isCalibrating()) {
                stage++;
                time.reset();
            }
            telemetry.addData("Calibrating", String.valueOf(tbdName.gyroUtils.gyro.isCalibrating()));
        }
        if (alliance.equals("Red")) {
            if (target.equals("Cap ball close")) {
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
                    double flyWheelLaunchPower = 0.65;
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
                        stage = 8;
                    }
                }

                if (stage == 8) {
                    if (time.time() > 2) {
                        intake.stopIntake(Intake.IntakeSpec.A);
                        intake.stopIntake(Intake.IntakeSpec.B);
                        flyWheel.FlyWheelMotor.setPower(0);
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 9) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 10) { // Turn to z90
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 35, GyroUtils.Direction.CLOCKWISE);
                        turn.run();
                    }
                    if (turn.isCompleted()) {
                        turn.completed();
                        stage++;
                        time.reset();
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
                        drive = new EncoderDrive(driveTrain, 1000, .6);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        driveTrain.stopRobot();
                        time.reset();
                        stage++;
                    }

                }

                if (stage == 13) {
                    if (time.time() > .5) {
                        stage++;
                        drive = null;
                        time.reset();
                    }
                }
                if (stage == 14) {
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 70, GyroUtils.Direction.COUNTERCLOCKWISE);
                        turn.run();
                    }
                    if (turn.isCompleted()) {
                        driveTrain.stopRobot();
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 15) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        turn = null;
                        time.reset();
                    }
                }

                if (stage == 16) {
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

                if (stage == 17) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        drive = null;
                        time.reset();
                    }
                }
            }
            if (target.equals("Cap ball far")) {
                if (stage == 1) { //drives forward 0.25 seconds
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 300, .75);
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
                        turn = new EncoderTurn(driveTrain, 35, GyroUtils.Direction.COUNTERCLOCKWISE);
                        turn.run();
                    }
                    if (turn.isCompleted()) {
                        turn.completed();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 4) {
                    if (time.time() > 0.15) {
                        turn = null;
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 5) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 1400, 0.5);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        drive.completed();
                        time.reset();
                        flyWheel.FlyWheelMotor.setPower(.65);
                        stage++;
                    }
                }

                if (stage == 6) {
                    if (time.time() > 3) {
                        drive = null;
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 7) {
                    if (time.time() < 2.5) {
                        intake.setIntakePower(Intake.IntakeSpec.B, -0.7);
                    } else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 8) {
                    if (time.time() < .35)
                        intake.setIntakePower(Intake.IntakeSpec.A, 1);
                    else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 9) {
                    if (time.time() > 4) {
                        intake.stopIntake(Intake.IntakeSpec.A);
                        intake.stopIntake(Intake.IntakeSpec.B);
                        flyWheel.FlyWheelMotor.setPower(0);
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 10) {
                    if (time.time() > .25) {
                        time.reset();
                        stage++;
                    }
                }


                if (stage == 11) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 1600, 0.5);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        drive.completed();
                        time.reset();
                        stage++;
                    }
                }
            }
            if (target.equals("Beacon")) {
                if (stage == 1) { //drives forward 0.25 seconds
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

                if (stage == 2) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        drive = null;
                        turn = null;
                        time.reset();
                    }
                }

                if (stage == 3) {
                    double flyWheelLaunchPower = 0.65;
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
                        stage = 8;
                    }
                }


                if (stage == 8) {
                    if (time.time() > 2) {
                        intake.stopIntake(Intake.IntakeSpec.A);
                        intake.stopIntake(Intake.IntakeSpec.B);
                        flyWheel.FlyWheelMotor.setPower(0);
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 9) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 10) { // Turn to 90
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 40, GyroUtils.Direction.COUNTERCLOCKWISE);
                        turn.run();
                    }
                    if (turn.isCompleted()) {
                        turn.completed();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 11) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        turn = null;
                        time.reset();
                    }
                }


                if (stage == 12) { //drives forward 33 inches in seconds // OUTDATED LENGTH
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 2400, .75);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 13) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        drive = null;
                        time.reset();
                    }
                }

                if (stage == 14) { // Turn to 145
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 17, GyroUtils.Direction.COUNTERCLOCKWISE);
                        turn.run();
                    }
                    if (turn.isCompleted()) {
                        turn.completed();
                        stage = 19;
                        time.reset();
                    }
                }

                if (stage == 19) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        turn = null;
                        time.reset();
                    }
                }
                if (stage == 20) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 500, .5);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 21) {
                    if (time.time() > .5) {
                        stage++;
                        drive = null;
                        time.reset();
                    }
                }

                // Initialize the beacon subroutine from BeaconSlamTest

                if (stage == 22) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 500, 0.15);
                        drive.run();
                    }
                    if (drive.isCompleted() || time.time() > 5) { // 5 second time override
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 23) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        drive = null;
                        time.reset();
                    }
                }
                if (stage == 24) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, -175, 0.5);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 25) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        drive = null;
                        time.reset();
                    }
                }
                if (stage == 26) {
                    switch (colorUtils.beaconColor()) {
                        case RED:
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
                                        stage = 21;
                                    }
                                    break;
                            }
                            break;
                        case NONE:
                            switch (alliance) {
                                case "Blue":
                                    time.reset();
                                    stage++;
                                    break;
                                case "Red":
                                    if (time.time() > 5.1) {
                                        time.reset();
                                        stage = 21;
                                    }
                                    break;
                            }
                    }
                }

                if (stage == 27) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, -800, .5);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 28) {
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 75, GyroUtils.Direction.COUNTERCLOCKWISE);
                        turn.run();
                    }
                    if (turn.isCompleted()) {
                        turn.completed();
                        time.reset();
                        drive = null;
                        stage++;
                    }
                }

                if (stage == 29) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 2400, .65);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        driveTrain.stopRobot();
                        time.reset();
                        stage++;
                    }
                }
            }
        }
        if (alliance.equals("Blue")) {
            if (target.equals("Cap ball close")) {
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
                    double flyWheelLaunchPower = 0.65;
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
                        stage = 8;
                    }
                }

                if (stage == 8) {
                    if (time.time() > 2) {
                        intake.stopIntake(Intake.IntakeSpec.A);
                        intake.stopIntake(Intake.IntakeSpec.B);
                        flyWheel.FlyWheelMotor.setPower(0);
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 9) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 10) { // Turn to z90
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

                if (stage == 11) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        turn = null;
                        time.reset();
                    }
                }

                if (stage == 12) {
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

                if (stage == 13) {
                    if (time.time() > .5) {
                        stage++;
                        drive = null;
                        time.reset();
                    }
                }
                if (stage == 14) {
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

                if (stage == 15) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        turn = null;
                        time.reset();
                    }
                }

                if (stage == 16) {
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

                if (stage == 17) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        drive = null;
                        time.reset();
                    }
                }
            }
            if (target.equals("Cap ball far")) {
                if (stage == 1) { //drives forward 0.25 seconds
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 300, .75);
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
                        turn = new EncoderTurn(driveTrain, 35, GyroUtils.Direction.CLOCKWISE);
                        turn.run();
                    }
                    if (turn.isCompleted()) {
                        turn.completed();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 4) {
                    if (time.time() > 0.15) {
                        turn = null;
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 5) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 1400, 0.5);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        drive.completed();
                        time.reset();
                        flyWheel.FlyWheelMotor.setPower(.65);
                        stage++;
                    }
                }

                if (stage == 6) {
                    if (time.time() > 3) {
                        drive = null;
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 7) {
                    if (time.time() < 2.5) {
                        intake.setIntakePower(Intake.IntakeSpec.B, -0.7);
                    } else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 8) {
                    if (time.time() < .35)
                        intake.setIntakePower(Intake.IntakeSpec.A, 1);
                    else {
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 9) {
                    if (time.time() > 4) {
                        intake.stopIntake(Intake.IntakeSpec.A);
                        intake.stopIntake(Intake.IntakeSpec.B);
                        flyWheel.FlyWheelMotor.setPower(0);
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 10) {
                    if (time.time() > .25) {
                        time.reset();
                        stage++;
                    }
                }


                if (stage == 11) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 1600, 0.5);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        drive.completed();
                        time.reset();
                        stage++;
                    }
                }
            }
            if (target.equals("Beacon")) {
                if (stage == 1) { //drives forward 0.25 seconds
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 600, .6);
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
                    double flyWheelLaunchPower = 0.65;
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
                        stage = 8;
                    }
                }

                if (stage == 8) {
                    if (time.time() > 2) {
                        intake.stopIntake(Intake.IntakeSpec.A);
                        intake.stopIntake(Intake.IntakeSpec.B);
                        flyWheel.FlyWheelMotor.setPower(0);
                        time.reset();
                        stage++;
                    }
                }

                if (stage == 9) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 10) { // Turn to 90
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 25, GyroUtils.Direction.CLOCKWISE);
                        turn.run();
                    }
                    if (turn.isCompleted()) {
                        turn.completed();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 11) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        turn = null;
                        time.reset();
                    }
                }


                if (stage == 12) { //drives forward 33 inches in seconds // OUTDATED LENGTH
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 2600, .75);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 13) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        drive = null;
                        time.reset();
                    }
                }

                if (stage == 14) { // Turn to 145
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 38, GyroUtils.Direction.CLOCKWISE);
                        turn.run();
                    }
                    if (turn.isCompleted()) {
                        turn.completed();
                        stage = 19;
                        time.reset();
                    }
                }

                if (stage == 19) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        turn = null;
                        time.reset();
                    }
                }
                if (stage == 20) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 600, .5);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 21) {
                    if (time.time() > .5) {
                        stage++;
                        drive = null;
                        time.reset();
                    }
                }

                // Initialize the beacon subroutine from BeaconSlamTest

                if (stage == 22) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 200, .25);
                        drive.run();
                    }
                    if (drive.isCompleted() || time.time() > 5) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 23) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        drive = null;
                        time.reset();
                    }
                }
                if (stage == 24) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, -75, .5);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                if (stage == 25) {
                    if (time.time() > AutonomousUtils.WAITTIME) {
                        stage++;
                        drive = null;
                        time.reset();
                    }
                }
                if (stage == 26) {
                    switch (colorUtils.beaconColor()) {
                        case RED:
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
                                        stage = 21;
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

                if (stage == 27) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, -800, .5);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }

                if (stage == 28) {
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 75, GyroUtils.Direction.CLOCKWISE);
                        turn.run();
                    }
                    if (turn.isCompleted()) {
                        turn.completed();
                        time.reset();
                        drive = null;
                        stage++;
                    }
                }

                if (stage == 29) {
                    if (drive == null) {
                        drive = new EncoderDrive(driveTrain, 2000, .65);
                        drive.run();
                    }
                    if (drive.isCompleted()) {
                        driveTrain.stopRobot();
                        time.reset();
                        stage++;
                    }
                }
            }
        }


        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Gyro", String.valueOf(tbdName.gyroUtils.gyro.getHeading()));
        telemetry.addData("Time", String.valueOf(time.time()));
        telemetry.addData("Beacon Color: ", String.valueOf(tbdName.colorUtils.beaconColor()));

    }
}
