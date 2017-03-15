package org.firstinspires.ftc.teamcode;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.BeaconUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderDrive;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Lift;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.PID;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.RangeUtils;

import static org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils.COMPLETED;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "BeaconRed", group = "Encoder Autonomous")
public class BeaconRed extends OpMode {
    ColorUtils.Color actedColor;

    int stage = 0;
    ElapsedTime time = new ElapsedTime();
    DriveTrain driveTrain;
    ColorUtils colorUtils;
    BeaconUtils beaconUtils;
    Intake intake;
    FlyWheel flyWheel;
    RangeUtils rangeUtils;
    AHRS navx;

    EncoderDrive drive;
    EncoderTurn turn;
    GyroTurn gyroTurn;

    boolean capBallGet = false;
    /* Selector variables */
    private String alliance = "Red";
    private String beaconAmount = "2";
    private int shoot = 2;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        colorUtils = new ColorUtils(hardwareMap);
        flyWheel = new FlyWheel(hardwareMap);
        intake = new Intake(hardwareMap);
        beaconUtils = new BeaconUtils(hardwareMap, colorUtils, alliance);
        rangeUtils = new RangeUtils(hardwareMap);
        new Lift(hardwareMap);
        navx = AHRS.getInstance(hardwareMap);

    }

    @Override
    public void start() {

        colorUtils.lineColorSensor.enableLed(true);
    }

    @Override
    public void loop() {
        if (stage == 0) {//Hold for Gyro Calibration
            stage++;
        }


        if (stage == 1) {//drive forward X cm/725 ticks to shooting position&start flywheel
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 725, 0.5);
                drive.run();
                if (shoot > 0) {
                    flyWheel.currentPower = flyWheel.defaultStartingPower;
                    flyWheel.currentlyRunning = true;
                }
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }

        flyWheel.powerMotor(); // Update flywheel values

        if (stage == 2) {//shoot particles and wait 2.5 seconds
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

        if (stage == 3) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                drive = null;
                turn = null;
                time.reset();
            }
        }

        if (stage == 4) { // Drive backwards 225 ticks to get a better angle at the white line
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, -225, 0.2);
                drive.run();
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 5) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
                drive = null;
                turn = null;
            }
        }
        if (stage == 6) {// Turn 28 degrees to point at the white line for Beacon 1
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 28, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 7) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
                drive = null;
                turn = null;
            }
        }
        if (stage == 8) { // // Drive until the color sensor sees the white line of Beacon 1
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 3600, 0.45);
                drive.run();
            }
            drive.runWithDecrementPower(0.000325); //slows down gradually to hit white line
            if (colorUtils.aboveWhiteLine()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
            if (drive.isCompleted()) { //fail safe if we miss white line
                drive = null;
                stage = 908;
                driveTrain.stopRobot();
                //AutonomousUtils.failSafeError(hardwareMap);
            }
        }
        if (stage == 9) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
                drive = null;
                turn = null;
            }
        }
        if (stage == 10) { // Turn 28 degrees to face beacon 1
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 28, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 11) { //wait
            if (time.time() > .5) {
                stage++;
                time.reset();
                drive = null;
                turn = null;

            }
        }
        if (stage == 12) { // Drive until we see a beacon color
            if (colorUtils.beaconColor().equals(ColorUtils.Color.NONE) && rangeUtils.rangeSensor.getDistance(DistanceUnit.CM) > 17) {
                driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                driveTrain.powerLeft(0.25);
                driveTrain.powerRight(0.25);
            } else {
                RobotLog.d("Attempted to stop robot at " + rangeUtils.rangeSensor.getDistance(DistanceUnit.CM));
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 13) { // Wait
            if (time.time() > 1) {
                stage++;
                time.reset();
                drive = null;
                turn = null;
            }
        }

        if (stage == 14) { // Act on beacon with color sensor/Flip the sunglasses
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

        if (stage == 15) { // Get the range to the wall in cm + 120 ticks more, set encoders and drive to the wall
            if (drive == null) {
                int counts = (int) (rangeUtils.rangeSensor.getDistance(DistanceUnit.CM) - 4) * 19; // Get the distance to the wall in enc counts, -4 ajusts for chaisi
                counts += 120;
                drive = new EncoderDrive(driveTrain, counts, 0.225); // Just a little umph to hit the button
                drive.run();
            }
            if (drive.isCompleted() || time.time() > 2) { // Time failsafe just in case we need to bail
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 16) { // Make a 6 degree turn (wiggle) to make sure we hit the button for beacon 1
            if (turn == null) {
                GyroUtils.Direction turnDirection = (beaconUtils.getCurrentPosition().equals(BeaconUtils.ServoPosition.TRIGGER_LEFT)) ?
                        GyroUtils.Direction.CLOCKWISE : GyroUtils.Direction.COUNTERCLOCKWISE;
                turn = new EncoderTurn(driveTrain, 6, turnDirection);
                turn.run();
            }
            if (turn.isCompleted() || time.time() > 0.5) {
                turn.completed();
                stage++;
                time.reset();
                turn = null;
            }
        }

        if (stage == 17) { // Make a 6 degree turn (wiggle back) to attempt to strighten up on the beacon
            if (turn == null) {
                GyroUtils.Direction turnDirection = (beaconUtils.getCurrentPosition().equals(BeaconUtils.ServoPosition.TRIGGER_LEFT)) ?
                        GyroUtils.Direction.COUNTERCLOCKWISE : GyroUtils.Direction.CLOCKWISE;
                turn = new EncoderTurn(driveTrain, 6, turnDirection);
                turn.run();
            }
            if (turn.isCompleted() || time.time() > 0.5) {
                turn.completed();
                stage++;
                time.reset();
                turn = null;
            }
        }

        if (stage == 18) { //Back up from Beacon 12cm with Range Sensor to prepare for turn
            if (rangeUtils.getDistance(DistanceUnit.CM, -1) <= 12) {
                driveTrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                driveTrain.powerLeft(-0.55);
                driveTrain.powerRight(-0.55);
            } else {
                driveTrain.stopRobot();
                beaconUtils.rotateServo(BeaconUtils.ServoPosition.CENTER);
                if (beaconAmount.equals("1")) stage = COMPLETED;
                if (beaconAmount.equals("2")) stage++;
            }
        }

        if (stage == 19) { // Turn towards the white line of the second beacon
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 74, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 20) { //wait and set RUN_WITHOUT_ENCODER for next stage and set zero power brake
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
                drive = null;
                turn = null;
                driveTrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            }
        }

        if (stage == 21) { // Drive until the color sensor sees the white line of Beacon 2
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, -3100, 0.45);
            }
            drive.runWithDecrementPower(0.000325); // slows down gradually to hit white line
            if (colorUtils.aboveWhiteLine() && Math.abs(driveTrain.RightFrontMotor.getCurrentPosition()) > 1000) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
            if (drive.isCompleted()) { //fail safe if we miss white line
                stage = AutonomousUtils.DEADBEEF;
                driveTrain.stopRobot();
                AutonomousUtils.failSafeError(hardwareMap);
            }
        }

        if (stage == 22) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
                drive = null;
                turn = null;
            }
        }

        if (stage == 23) { // Back up X cm/165 Ticks since we ran past the while line
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 165, 0.3);
                drive.run();
            }
            if (drive.isCompleted()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 24) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
                drive = null;
                turn = null;
            }
        }

        if (stage == 25) { // Turn 71.5 degrees to face beacon 2
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 72.7, GyroUtils.Direction.CLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 26) { // Wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage ++;
                time.reset();
                drive = null;
                turn = null;
            }
        }


        if (stage == 27) { // Drive until we see a beacon color
            if (colorUtils.beaconColor().equals(ColorUtils.Color.NONE) && rangeUtils.rangeSensor.getDistance(DistanceUnit.CM) > 20) {
                driveTrain.powerLeft(0.25);
                driveTrain.powerRight(0.25);
            } else {
                RobotLog.d("Attempted to stop robot at " + rangeUtils.rangeSensor.getDistance(DistanceUnit.CM));
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }



        if (stage == 28) { // Act on beacon with color sensor/Flip the sunglasses
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

        if (stage == 29) { // Get the range to the wall in cm + 150 ticks more, set encoders and drive to the wall
            if (drive == null) {
                int counts = (int) (rangeUtils.rangeSensor.getDistance(DistanceUnit.CM) - 4) * 19;// Get the distance to the wall in enc counts, -4 ajusts for chaisi
                counts += 150;
                drive = new EncoderDrive(driveTrain, counts + 25, 0.225); // Just a little umph to hit the button
                drive.run();
            }
            if (drive.isCompleted() || time.time() > 2) { // Time failsafe just in case we need to bail
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }


        if (stage == 30) { // Make a 6 degree turn (wiggle) to make sure we hit the button for beacon 6
            if (turn == null) {
                GyroUtils.Direction turnDirection = (beaconUtils.getCurrentPosition().equals(BeaconUtils.ServoPosition.TRIGGER_LEFT)) ?
                        GyroUtils.Direction.CLOCKWISE : GyroUtils.Direction.COUNTERCLOCKWISE;
                turn = new EncoderTurn(driveTrain, 6, turnDirection);
                turn.run();
            }
            if (turn.isCompleted() || time.time() > 0.5) {
                turn.completed();
                stage++;
                time.reset();
                turn = null;
            }
        }
        if (stage == 31) { //wait
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
                drive = null;
                turn = null;
            }
        }

        if (stage == 32) { //Back up 10 cm with prox to prepare for turn
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

        if (stage == 33) {//Turn 113 degrees to point at cap ball
            if (turn == null) {
                turn = new EncoderTurn(driveTrain, 113, GyroUtils.Direction.COUNTERCLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                stage++;
                drive = null;
                time.reset();
            }
        }

        if (stage == 34) {//drive to X cm/2700 ticks to hit cap ball and park
            if (capBallGet) {
                if (drive == null) {
                    drive = new EncoderDrive(driveTrain, 2700, 1);
                    drive.run();
                }
                if (drive.isCompleted()) {
                    driveTrain.stopRobot();
                    stage++;
                    turn = null;
                    time.reset();
                }
            } else {
                stage++;
                time.reset();
            }
        }

        if(stage == 908)
        {
            if (turn == null){
                turn = new EncoderTurn(driveTrain, 80, GyroUtils.Direction.CLOCKWISE);
                turn.run();
            }
            if (turn.isCompleted()){
                turn.completed();
                stage++;
                turn = null;
                time.reset();
            }
        }

        if (stage == 909){
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, -3600, 0.45);
                drive.run();
            }
            if (colorUtils.aboveWhiteLine()) {
                driveTrain.stopRobot();
                intake.stopIntake(Intake.IntakeSpec.BOTH);
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


        telemetry.addData("F", driveTrain.LeftFrontMotor.getCurrentPosition() + ":" + driveTrain.RightFrontMotor.getCurrentPosition());
        telemetry.addData("B", driveTrain.LeftBackMotor.getCurrentPosition() + ":" + driveTrain.RightBackMotor.getCurrentPosition());
        telemetry.addData("Range", rangeUtils.rangeSensor.getDistance(DistanceUnit.CM));
        telemetry.addData("Beacon", colorUtils.beaconColor().toString());
        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Time", String.valueOf(time.time()));
    }
}