package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.Intake;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "RedBeaconCorner", group = "Testing")
public class CloseTwoBeaconsRed extends OpMode {


    private final static String TAG = CloseTwoBeaconsRed.class.getName();
    private int stage = 0;
    private ElapsedTime time = new ElapsedTime();
    private DriveTrain driveTrain;
    private GyroUtils gyroUtils;
    private ColorUtils colorUtils;
    private GyroSensor gyro;
    private Intake intake;
    private FlyWheel flyWheel;



    private String alliance = "Red";

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

        if (stage == 1) { //drives forward 33 inches in seconds
            if (time.time() <= 0.64) {
                driveTrain.driveStraight();
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 2) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }

        if (stage == 3) {
            double flyWheelLaunchPower = 0.28;
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
                intake.setIntakePower(Intake.IntakeSpec.B, 1);
            } else {
                time.reset();
                stage++;
            }
        }

        if (stage == 6)
        {
            if (time.time() > 1.2)
            {
                time.reset();
                stage++;
            }
        }

        if (stage == 7) {
            if (time.time() < .35)
                intake.setIntakePower(Intake.IntakeSpec.A, 1);
            else {
                time.reset();
                stage++;
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
            int difference = 13;
            int angle = 270;
            if (gyro.getHeading() > (angle + difference) || gyro.getHeading() < 10) {
                driveTrain.powerLeft(-.15);
                driveTrain.powerRight(.15);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 11) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }


        if (stage == 12) { //drives forward 33 inches in seconds // OUTDATED LENGTH
            if (time.time() <= 0.8) {
                driveTrain.driveStraight(1);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 13) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }

        if (stage == 14) { // Turn to 145
            int difference = 9;
            int angle = 335;
            if (!gyroUtils.isGyroInTolerance(angle, difference)) {
                driveTrain.powerLeft(.15);
                driveTrain.powerRight(-.15);
            } else {
                RobotLog.d("13." + "Statement true at gyro degree " + gyro.getHeading());
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 15) {
            if (!colorUtils.aboveWhiteLine()) {
                driveTrain.driveStraight(.3);
            } else {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }
        if (stage == 16) {
            if (time.time() < AutonomousUtils.WAITTIME) {
                time.reset();
                stage++;
            }
        }

        if (stage == 17) {
            if (time.time() < .3) {
                driveTrain.driveStraight(-.3);
            } else {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }

        if (stage == 18) { // Turn to 90
            int difference = 13;
            int angle = 270;
            if (gyro.getHeading() > (angle + difference) || gyro.getHeading() < 10) {
                driveTrain.powerLeft(-.15);
                driveTrain.powerRight(.15);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 19) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }
        if (stage == 20) {
            if (time.time() < 1) {
                driveTrain.driveStraight(.3);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 21) {
            if (time.time() > .5) {
                stage++;
                time.reset();
            }
        }

        // Initialize the beacon subroutine from BeaconSlamTest

        if (stage == 22) {
            if (time.time() < 1) {
                driveTrain.driveStraight(0.3);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 23) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }
        if (stage == 24) {
            if (time.time() < 0.15) {
                driveTrain.driveStraight(-0.5);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 25) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
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
            }
        }

        if(stage == 27)
        {
            if (time.time() < 0.5) {
                driveTrain.driveStraight(-0.5);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if(stage == 28)
        {
            int difference = 13;
            int angle = 0;
            if (gyro.getHeading() > 210) {
                driveTrain.powerLeft(-.15);
                driveTrain.powerRight(.15);
            } else {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }

        if (stage == 29)
        {
            if (time.time() < 1.25)
            {
                driveTrain.powerRight(1);
                driveTrain.powerLeft(1);
            }
            else
            {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }

        /*if (stage == 29)
        {
            if (time.time() < .5)
            {
                driveTrain.driveStraight(.3);
            }
            else
            {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 30)
        {
            if(time.time() > .15)
            {
                time.reset();
                stage++;
            }
        }

        if (stage == 31) {
            if (!colorUtils.aboveWhiteLine()) {
                driveTrain.driveStraight(.3);
            } else {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }
        if (stage == 32) {
            if (time.time() < AutonomousUtils.WAITTIME) {
                time.reset();
                stage++;
            }
        }

        if (stage == 33) {
            if (time.time() < .25) {
                driveTrain.driveStraight(-.3);
            } else {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }

        if (stage == 34) { // Turn to 90
            int difference = 13;
            int angle = 270;
            if (gyro.getHeading() > (angle + difference) || gyro.getHeading() < 10) {
                driveTrain.powerLeft(-.15);
                driveTrain.powerRight(.15);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 35) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }
        if (stage == 36) {
            if (time.time() < 1) {
                driveTrain.driveStraight(.3);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 37) {
            if (time.time() > .5) {
                stage++;
                time.reset();
            }
        }

        // Initialize the beacon subroutine from BeaconSlamTest

        if (stage == 38) {
            if (time.time() < 1) {
                driveTrain.driveStraight(0.3);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 39) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }
        if (stage == 40) {
            if (time.time() < 0.1) {
                driveTrain.driveStraight(-0.5);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 41) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }
        if (stage == 42) {
            switch (colorUtils.beaconColor()) {
                case RED:
                    switch (alliance) {
                        case "Blue":
                            if (time.time() > 5.1) {
                                time.reset();
                                stage = 33;
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
                                stage = 33;
                            }
                            break;
                    }
                    break;
            }
        }

        if (stage == 43)
        {
            if (time.time() < .3)
            {
                driveTrain.powerLeft(-.3);
                driveTrain.powerRight(-.3);
            }
            else
            {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }

        if (stage == 44)
        {
            if (gyro.getHeading() > 150)
            {
                driveTrain.powerLeft(-.15);
                driveTrain.powerRight(.15);
            }
            else
            {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }

        if (stage == 45)
        {
            if (time.time() < .3)
            {
                driveTrain.driveStraight();
            }
            else
            {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }

        if (stage == 46)
        {
            if (time.time() > .3)
            {
                time.reset();
                stage++;
            }
        }

        if (stage == 47)
        {
            if (!colorUtils.aboveRedLine() && time.time() < 3)
            {
                driveTrain.powerLeft(.3);
                driveTrain.powerRight(.3);
            }
            else
            {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }*/


        Log.d(TAG, String.valueOf(stage + " Gyro: " + gyro.getHeading() + " Time:" + this.time + " Stage: " + stage));
        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Gyro", String.valueOf(gyro.getHeading()));
        telemetry.addData("Time", String.valueOf(time.time()));
        telemetry.addData("White Line", String.valueOf(colorUtils.aboveWhiteLine()));
        telemetry.addData("Beacon Color: ", String.valueOf(colorUtils.beaconColor()));

    }
}