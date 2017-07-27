package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.RobotLog;

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
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.NewEncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.NewEncoderTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.RangeUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.StateMachineOpMode;

/**
 * Created by Dynamic Signals on 10/16/2016.
 */

@Autonomous(name = "beaconBlueClose", group = "AWorking")
public class beaconBlueClose extends StateMachineOpMode {

    ColorUtils.Color actedColor;

    DriveTrain driveTrain;
    ColorUtils colorUtils;
    BeaconUtils beaconUtils;
    Intake intake;
    FlyWheel flyWheel;
    RangeUtils rangeUtils;
    GyroUtils gyroUtils;

    EncoderDrive drive;
    EncoderTurn turn;

    boolean capBallGet = false;
    boolean corner = false;
    /* Selector variables */
    private String alliance = "Blue";
    private String beaconAmount = "2";
    private int shoot = 2;
    private int moveType = 0;
    //0 is for gyro with encoder backup
    //1 is for encoders and gyro
    //2 is for encoders and navX
    private double wheelPower = 0;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        colorUtils = new ColorUtils(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);
        beaconUtils = new BeaconUtils(hardwareMap, colorUtils, alliance);
        rangeUtils = new RangeUtils(hardwareMap);
        new Lift(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        gyroUtils.gyro.calibrate();
        intake.openStopper();

    }

    @Override
    public void loop() {

        if (stage == 0) { //Gyro Calibration
            if (!gyroUtils.gyro.isCalibrating()) {
                stage++;
                time.reset();
            }
            telemetry.addData("Calibrating", String.valueOf(gyroUtils.gyro.isCalibrating()));
            if (driveTrain.getVoltage() > 12)
                wheelPower = .9;
            else
                wheelPower = .9;
        }

        if (stage == 1) {//drive forward X cm/825 ticks to shooting position&start flywheel
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 650, 0.5);
                drive.run();
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                time.reset();
                stage += .5;
            }
        }

        if (stage == 1.5){
            if (shoot > 0) {
                flyWheel.currentPower = flyWheel.defaultStartingPower;
                flyWheel.currentlyRunning = true;
            }
            if (time.time() > 1){
                time.reset();
                stage += .5;
            }
        }

        flyWheel.powerMotor(); // Update flywheel values

        if (stage == 2) {//shoot particles and wait 2.5 seconds
            if (shoot == 1) {
                intake.setIntakePower(Intake.IntakeSpec.A, 1);
            }
            if (shoot == 2) {
                intake.setIntakePower(Intake.IntakeSpec.A, 1);
            }
            if (time.time() > 2.5 || shoot <= 0) {
                time.reset();
                stage += .5;
                intake.stopIntake(Intake.IntakeSpec.A);
                intake.setIntakePower(Intake.IntakeSpec.A, -1);
                flyWheel.currentlyRunning = false;
            }
        }

        if (stage == 2.5){
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, -150, 0.5);
                drive.run();
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                time.reset();
                stage += .5;
            }
        }

        if (stage == 3) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                next();
            }
        }

        /**
         * Corner close routine 1000 - 1004
         */
        if (corner){
            if (stage == 4) {
                if(moveType == 0){
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 160, GyroUtils.Direction.CLOCKWISE);
                        turn.run();
                    }
                    if (((gyroUtils.gyro.getHeading() > 10) && (gyroUtils.gyro.getHeading() < 270)) || turn.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                //NewEncoderTurn.createTurn(this, 160, GyroUtils.Direction.CLOCKWISE);
            }

            if (stage == 5) {
                NewEncoderDrive.createDrive(this, 1700);
            }

            if (stage == 6) {
                if(moveType == 0){
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 85, GyroUtils.Direction.CLOCKWISE);
                        turn.run();
                    }
                    if (((gyroUtils.gyro.getHeading() > 10) && (gyroUtils.gyro.getHeading() < 225)) || turn.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                //NewEncoderTurn.createTurn(this, 85, GyroUtils.Direction.CLOCKWISE);
            }

            if (stage == 7) {
                NewEncoderDrive.createDrive(this, 1500);
            }

            if (stage == 8) {
                if (time.time() > 2) {
                    intake.stopIntake(Intake.IntakeSpec.BOTH);
                }
            }
        }

        if (!corner){
            if (stage == 4) { // Drive backwards X cm/625 ticks to get a better angle at the white line
                //NewEncoderDrive.createDrive(this, -625, 0.2);
                next();
            }

            if (stage == 5) { // Wait
                if (time.time() > AutonomousUtils.WAITTIME) {
                    next();
                }
            }
            if (stage == 6) {// Turn 27 degrees to point at the white line for Beacon 1
                if(moveType == 0){
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 200, GyroUtils.Direction.CLOCKWISE);
                        turn.run();
                    }
                    if (((gyroUtils.gyro.getHeading() > 10) && (gyroUtils.gyro.getHeading() < 331)) || turn.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                //NewEncoderTurn.createTurn(this, 75, GyroUtils.Direction.CLOCKWISE);
            }
            if (stage == 7) { // Wait
                if (time.time() > AutonomousUtils.WAITTIME) {
                    next();
                }
            }
            if (stage == 8) { // Drive until the color sensor sees the white line of Beacon 1
                if (drive == null) {
                    drive = new EncoderDrive(driveTrain, 4500, 0.45);
                }
                drive.runWithDecrementPower(0.0003); //slows down gradually to hit white line
                if (colorUtils.aboveWhiteLine()) {
                    driveTrain.stopRobot();
                    stage++;
                    time.reset();
                }
                if (drive.isCompleted()) { //fail safe if we miss white line
                    drive = null;
                    stage = 908;
                    driveTrain.stopRobot();
                }
            }
            if (stage == 9) { // Wait
                if (time.time() > AutonomousUtils.WAITTIME) {
                    next();
                }
            }
            if (stage == 10) { // Backup to lines
                //NewEncoderDrive.createDrive(this, -100, 0.4);
                next();
            }
            if (stage == 11) { // Turn to face beacon 1
                if(moveType == 0){
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 120, GyroUtils.Direction.CLOCKWISE);
                        turn.run();
                    }
                    if (((gyroUtils.gyro.getHeading() > 10) && (gyroUtils.gyro.getHeading() < 290)) || turn.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                //NewEncoderTurn.createTurn(this, 81, GyroUtils.Direction.CLOCKWISE);
            }
            if (stage == 12) { // Wait
                if (time.time() > .25) {
                    next();
                }
            }
            if (stage == 13) { // Drive until we see a beacon color
                if (colorUtils.beaconColor().equals(ColorUtils.Color.NONE) && rangeUtils.rangeSensor.getDistance(DistanceUnit.CM) > 20) {
                    driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    driveTrain.powerLeft(0.25);
                    driveTrain.powerRight(0.25);
                } else {
                    RobotLog.d("Attempted to stop robot at " + rangeUtils.rangeSensor.getDistance(DistanceUnit.CM));
                    driveTrain.stopRobot();
                    next();
                }
            }

            if (stage == 14) { // Wait
                if (time.time() > 0.5) {
                    next();
                }
            }

            if (stage == 15) { // Act on beacon with color sensor/Flip the sunglasses
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
            if (stage == 16) { // Get the range to the wall in cm + ticks more, set encoders and drive to the wall
                if (drive == null) {
                    int counts = (int) (rangeUtils.rangeSensor.getDistance(DistanceUnit.CM) - 4) * 19; // Get the distance to the wall in enc counts, -4 ajusts for chaisi
                    counts += 500;
                    drive = new EncoderDrive(driveTrain, counts, 0.2); // Just a little umph to hit the button
                    drive.run();
                }
                if (drive.isCompleted() || time.time() > 1.5) { // Time failsafe just in case we need to bail
                    driveTrain.stopRobot();
                    next();
                }
            }


            if (stage == 17) { // Make a 6 degree turn (wiggle) to make sure we hit the button for beacon 1
                if (turn == null) {
                    GyroUtils.Direction turnDirection = (beaconUtils.getCurrentPosition().equals(BeaconUtils.ServoPosition.TRIGGER_LEFT)) ?
                            GyroUtils.Direction.CLOCKWISE : GyroUtils.Direction.COUNTERCLOCKWISE;
                    turn = new EncoderTurn(driveTrain, 12, turnDirection);
                    turn.run();
                }
                if (turn.isCompleted() || time.time() > 0.5) {
                    turn.completed();
                    next();
                }
            }


            if (stage == 18) { // Make a 6 degree turn (wiggle back) to attempt to straighten up on beacon 1
                if (turn == null) {
                    GyroUtils.Direction turnDirection = (beaconUtils.getCurrentPosition().equals(BeaconUtils.ServoPosition.TRIGGER_LEFT)) ?
                            GyroUtils.Direction.COUNTERCLOCKWISE : GyroUtils.Direction.CLOCKWISE;
                    turn = new EncoderTurn(driveTrain, 12, turnDirection);
                    turn.run();
                }
                if (turn.isCompleted() || time.time() > 0.5) {
                    turn.completed();
                    next();
                }
            }

            if (stage == 19) { //Back up from Beacon 13cm with Range Sensor to prepare for turn
                if (rangeUtils.getDistance(DistanceUnit.CM, -1) <= 11) {
                    driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    driveTrain.powerLeft(-0.55);
                    driveTrain.powerRight(-0.55);

                } else {
                    driveTrain.stopRobot();
                    beaconUtils.rotateServo(BeaconUtils.ServoPosition.CENTER);
                    if (beaconAmount.equals("1")) stage = AutonomousUtils.COMPLETED;
                    if (beaconAmount.equals("2")) stage++;
                }
            }

            if (stage == 20) { // Turn towards the white line of the second beacon// Turn 27 degrees to point at the white line for Beacon 1
                if(moveType == 0){
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 180, GyroUtils.Direction.COUNTERCLOCKWISE);
                        turn.run();
                    }
                    if ((gyroUtils.gyro.getHeading() > 10) && (gyroUtils.gyro.getHeading() > 350) || turn.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                //NewEncoderTurn.createTurn(this, 160, GyroUtils.Direction.COUNTERCLOCKWISE);
            }

            if (stage == 21) { // Wait
                if (time.time() > AutonomousUtils.WAITTIME) {
                    next();
                }
            }

            if (stage == 22) { // Drive until the color sensor sees the white line of Beacon 2
                if (drive == null) {
                    drive = new EncoderDrive(driveTrain, 3100, 0.7);
                }
                drive.runWithDecrementPower(0.0125); // slows down gradually to hit white line
                if (colorUtils.aboveWhiteLine() && Math.abs(driveTrain.RightFrontMotor.getCurrentPosition()) > 1000) {
                    driveTrain.stopRobot();
                    stage++;
                    time.reset();
                }
                if (drive.isCompleted()) { //fail safe if we miss white line
                    stage = 922;
                    driveTrain.stopRobot();
                    AutonomousUtils.failSafeError(hardwareMap);
                }
            }

            if (stage == 23) { // Wait
                if (time.time() > AutonomousUtils.WAITTIME) {
                    next();
                }
            }

            if (stage == 24) { // Back up x cm/75 Ticks since we ran past the while line
                NewEncoderDrive.createDrive(this, -105, 0.3);
            }

            if (stage == 25) { // Wait
                if (time.time() > AutonomousUtils.WAITTIME) {
                    next();
                }
            }

            if (stage == 26) { // Turn to face beacon 2
                if(moveType == 0){
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 160, GyroUtils.Direction.CLOCKWISE);
                        turn.run();
                    }
                    if (((gyroUtils.gyro.getHeading() > 10) && (gyroUtils.gyro.getHeading() < 270)) || turn.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                //NewEncoderTurn.createTurn(this, 160, GyroUtils.Direction.CLOCKWISE);
            }

            if (stage == 27) { // Wait
                if (time.time() > AutonomousUtils.WAITTIME) {
                    next();
                }
            }
            if (stage == 28) { // Drive until we see a beacon color
                if (colorUtils.beaconColor().equals(ColorUtils.Color.NONE) && rangeUtils.rangeSensor.getDistance(DistanceUnit.CM) > 20) {
                    driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    driveTrain.powerLeft(0.25);
                    driveTrain.powerRight(0.25);
                } else {
                    RobotLog.d("Attempted to stop robot at " + rangeUtils.rangeSensor.getDistance(DistanceUnit.CM));
                    driveTrain.stopRobot();
                    next();
                }
            }

            if (stage == 29) { // Act on beacon with color sensor/Flip the sunglasses
                if (!colorUtils.beaconColor().equals(ColorUtils.Color.NONE)) {
                    beaconUtils.actOnBeaconWithColorSensor();
                    actedColor = beaconUtils.actOnBeaconWithColorSensor();
                    stage++;
                    time.reset();
                } else {
                    stage = 626;
                    AutonomousUtils.failSafeError(hardwareMap);
                    time.reset();
                }

            }

            if (stage == 30) { // Get the range to the wall in cm + X ticks more, set encoders and drive to the wall
                if (drive == null) {
                    int counts = (int) (rangeUtils.rangeSensor.getDistance(DistanceUnit.CM) - 4) * 19; // Get the distance to the wall in enc counts, -4 ajusts for chaisi
                    counts += 300;
                    drive = new EncoderDrive(driveTrain, counts, 0.225); // Just a little umph to hit the button
                    drive.run();
                }
                if (drive.isCompleted() || time.time() > 1.5) { // Time failsafe just in case we need to bail
                    driveTrain.stopRobot();
                    next();
                }
            }


            if (stage == 31) { // Make a 6 degree turn (wiggle) to make sure we hit the button for beacon 6
                if (turn == null) {
                    GyroUtils.Direction turnDirection = (beaconUtils.getCurrentPosition().equals(BeaconUtils.ServoPosition.TRIGGER_LEFT)) ?
                            GyroUtils.Direction.CLOCKWISE : GyroUtils.Direction.COUNTERCLOCKWISE;
                    turn = new EncoderTurn(driveTrain, 10, turnDirection);
                    turn.run();
                }
                if (turn.isCompleted() || time.time() > 0.5) {
                    turn.completed();
                    next();
                }
            }

            if (stage == 32) { //wait
                if (time.time() > AutonomousUtils.WAITTIME) {
                    next();
                }
            }

            if (stage == 33) { ///Back up 10 cm with prox to prepare for turn
                if (rangeUtils.getDistance(DistanceUnit.CM, -1) <= 10) {
                    driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    driveTrain.powerLeft(-0.55);
                    driveTrain.powerRight(-0.55);
                } else {
                    driveTrain.stopRobot();
                    beaconUtils.rotateServo(BeaconUtils.ServoPosition.CENTER);
                    if (capBallGet) stage++;
                    if (!capBallGet) stage = AutonomousUtils.COMPLETED;
                    time.reset();
                }
            }
            if (stage == 34) {//Turn 31 degrees to point at cap ball
                if(moveType == 0){
                    if (turn == null) {
                        turn = new EncoderTurn(driveTrain, 400, GyroUtils.Direction.CLOCKWISE);
                        turn.run();
                    }
                    if (((gyroUtils.gyro.getHeading() > 10) && (gyroUtils.gyro.getHeading() < 163)) || turn.isCompleted()) {
                        driveTrain.stopRobot();
                        stage++;
                        time.reset();
                    }
                }
                //NewEncoderTurn.createTurn(this, 240, GyroUtils.Direction.CLOCKWISE);
                //NewEncoderDrive.teardown();
            }
            if (stage == 35) {//drive to X cm/-3100 ticks to hit cap ball and park
                NewEncoderDrive.createDrive(this, 3500, 1);
            }
        }

        if (stage == 908) {
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 80, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                stage++;
                turn = null;
                time.reset();
            }
        }

        if (stage == 909) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, -3600, 0.25);
                drive.run();
            }
            if (colorUtils.aboveWhiteLine()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
            if (drive.isCompleted()) { //fail safe if we miss white line
                stage = 9909;
                drive = null;
                turn = null;
                driveTrain.stopRobot();
                AutonomousUtils.failSafeError(hardwareMap);
            }
        }
        if (stage == 910) {
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 160, GyroUtils.Direction.CLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                drive = null;
                turn = null;
                stage = 12;
            }
        }
        if (stage == 922) {
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, -3600, 0.25);
                drive.run();
            }
            if (colorUtils.aboveWhiteLine()) {
                driveTrain.stopRobot();
                next();
            }
            if (drive.isCompleted()) { //fail safe if we miss the second white line
                stage = 9922;
                drive = null;
                turn = null;
                driveTrain.stopRobot();
                AutonomousUtils.failSafeError(hardwareMap);
            }
        }
        if (stage == 921){
            if (drive == null){
                drive = new EncoderDrive(driveTrain, 200, .25);
                drive.run();
            }
            if (drive.isCompleted()){
                drive.completed();
                stage = 25;
                time.reset();
            }
        }

        telemetry.addData("F", driveTrain.LeftFrontMotor.getCurrentPosition() + ":" + driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("B", driveTrain.LeftBackMotor.getCurrentPosition() + ":" + driveTrain.RightBackMotor.getCurrentPosition());
        telemetry.addData("Range", rangeUtils.rangeSensor.getDistance(DistanceUnit.CM));
        telemetry.addData("Beacon", colorUtils.beaconColor().toString());
        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Time", String.valueOf(time.time()));
        telemetry.addData("Heading", gyroUtils.gyro.getHeading());
    }

    @Override
    public void next() {
        super.next();
        turn = null;
        drive = null;
    }
}


