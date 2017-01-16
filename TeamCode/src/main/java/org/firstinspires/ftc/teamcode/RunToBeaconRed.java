package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.FlyWheel;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.Intake;

/**
 * Created by Leo on 10/16/2016.
 */

@Autonomous(name = "BeaconCloseRed", group = "Testing")
@Disabled
public class RunToBeaconRed extends OpMode {

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
            double flyWheelLaunchPower = 0.5;
            flyWheel.FlyWheelMotor.setPower(flyWheelLaunchPower);
            stage++;
        }

        if (stage == 4) {
            if (time.time() > 2) {
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

        if (stage == 6) {
            if (time.time() < .35)
                intake.setIntakePower(Intake.IntakeSpec.A, 1);
            else {
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
            int difference = 11;
            int angle = 90;
            if (gyro.getHeading() < (angle - difference) || gyro.getHeading() > 350) {
                driveTrain.powerLeft(.15);
                driveTrain.powerRight(-.15);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 10) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }


        if (stage == 11) { //drives forward 33 inches in seconds // OUTDATED LENGTH
            if (time.time() <= 0.8) {
                driveTrain.driveStraight(-1);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 12) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }

        if (stage == 13) { // Turn to 145
            int difference = 9;
            int angle = 145;
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

        if (stage == 14) {
            if (!colorUtils.aboveWhiteLine()) {
                driveTrain.driveStraight(-.3);
            } else {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }
        if (stage == 15) {
            if (time.time() < AutonomousUtils.WAITTIME) {
                time.reset();
                stage++;
            }
        }

        if (stage == 16) {
            if (time.time() < .15) {
                driveTrain.driveStraight(.3);
            } else {
                driveTrain.stopRobot();
                time.reset();
                stage++;
            }
        }

        if (stage == 17) {
            int difference = 9;
            int angle = 90;
            if (!gyroUtils.isGyroInTolerance(angle, difference)) {
                driveTrain.powerLeft(-.15);
                driveTrain.powerRight(.15);
            } else {
                RobotLog.d("15." + "Statement true at gyro degree " + gyro.getHeading());
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 18) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }
        if (stage == 19) {
            if (time.time() < 0.75) {
                driveTrain.driveStraight(-.3);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }

        if (stage == 20) {
            if (time.time() > .5) {
                stage++;
                time.reset();
            }
        }

        // Initialize the beacon subroutine from BeaconSlamTest

        if (stage == 21) {
            if (time.time() < 0.75) {
                driveTrain.driveStraight(-0.5);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 22) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }
        if (stage == 23) {
            if (time.time() < 0.1) {
                driveTrain.driveStraight(0.5);
            } else {
                driveTrain.stopRobot();
                stage++;
                time.reset();
            }
        }
        if (stage == 24) {
            if (time.time() > AutonomousUtils.WAITTIME) {
                stage++;
                time.reset();
            }
        }
        if (stage == 25) {
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


        RobotLog.d(String.valueOf(stage + " Gyro: " + gyro.getHeading() + " Time:" + time.time()));
        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("Gyro", String.valueOf(gyro.getHeading()));
        telemetry.addData("Time", String.valueOf(time.time()));
        telemetry.addData("White Line", String.valueOf(colorUtils.aboveWhiteLine()));
        telemetry.addData("Beacon Color: ", String.valueOf(colorUtils.beaconColor()));

    }
}